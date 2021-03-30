package github.exia1771.deploy.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import github.exia1771.deploy.common.entity.Role;
import github.exia1771.deploy.common.entity.User;
import github.exia1771.deploy.common.exception.ServiceException;
import github.exia1771.deploy.common.service.RoleService;
import github.exia1771.deploy.common.service.UserService;
import github.exia1771.deploy.common.service.impl.BaseServiceImpl;
import github.exia1771.deploy.common.util.DateUtil;
import github.exia1771.deploy.common.util.StringUtil;
import github.exia1771.deploy.common.util.Users;
import github.exia1771.deploy.common.util.Validators;
import github.exia1771.deploy.core.dto.ProjectContainerDTO;
import github.exia1771.deploy.core.entity.BuildType;
import github.exia1771.deploy.core.entity.Project;
import github.exia1771.deploy.core.entity.ProjectContainer;
import github.exia1771.deploy.core.entity.ProjectContainerSearch;
import github.exia1771.deploy.core.mapper.ProjectContainerMapper;
import github.exia1771.deploy.core.service.BuildTypeService;
import github.exia1771.deploy.core.service.ProjectContainerService;
import github.exia1771.deploy.core.service.ProjectService;
import github.exia1771.deploy.core.service.jenkins.JenkinsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProjectContainerServiceImpl extends BaseServiceImpl<String, ProjectContainer> implements ProjectContainerService {

	private final ProjectContainerMapper mapper;

	private static final String REPOSITORY_SUFFIX = ".git";
	private static final String GITHUB_API_URL = "https://api.github.com/repos/";
	private static final String GITHUB_URL_PREFIX = "https://github.com/";
	private static final String GITHUB_TAGS_API_SUFFIX = "/git/refs/tags";

	private static final String TAG_COLUMN = "git_tag";
	private static final String PUB_DATE_COLUMN = "pub_date";
	private static final String PUBLISHER_COLUMN = "publisher";
	private static final String NAMESPACE_COLUMN = "namespace_id";
	private static final String PROJECT_ID_COLUMN = "project_id";

	private static final BlockingQueue<ProjectContainer> BUILD_BLOCKING_QUEUE = new ArrayBlockingQueue<>(2);

	@Autowired
	private ExecutorService fixedThreadPool;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private BuildTypeService buildTypeService;

	@Autowired
	private List<JenkinsService> jenkinsServices;

	@Autowired
	private UserService userService;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Autowired
	private RoleService roleService;

	public ProjectContainerServiceImpl(ProjectContainerMapper mapper) {
		super(mapper);
		this.mapper = mapper;
	}

	@Override
	public String getBuildLog(String projectContainerId) {
		ProjectContainer container = findById(projectContainerId);
		BuildType buildType = buildTypeService.findById(container.getBuildTypeId());
		String buildLog = "";
		for (JenkinsService jenkinsService : jenkinsServices) {
			if (jenkinsService.getBuildType().equals(buildType.getName())) {
				try {
					buildLog = jenkinsService.getBuildLog(container.getJenkinsJobName(), container.getJenkinsBuildNumber());
				} catch (IOException e) {
					buildLog = "";
					log.error("获取日志失败=>", e);
				}
			}
		}
		return buildLog;
	}

	@Override
	public List<String> findRepositoryTags(String projectId, String keyword) {
		Project project = projectService.findById(projectId);
		String gitUrl = project.getGitUrl();
		String suffix = gitUrl.split(GITHUB_URL_PREFIX)[1];
		suffix = suffix.replaceAll(REPOSITORY_SUFFIX, "");
		try {
			String json = restTemplate.getForObject(GITHUB_API_URL + suffix + GITHUB_TAGS_API_SUFFIX, String.class);
			JSONArray array = JSON.parseArray(json);
			if (array == null) {
				return new ArrayList<>();
			}
			return array.stream()
					.map(o -> {
						JSONObject temp = (JSONObject) o;
						String ref = temp.getString("ref");
						return ref.substring(ref.lastIndexOf("/") + 1);
					})
					.filter(s -> s.contains(keyword))
					.collect(Collectors.toList());
		} catch (HttpClientErrorException.NotFound e) {
			return new ArrayList<>();
		}
	}

	@Override
	protected void beforeSave(ProjectContainer projectContainer) {
		Validators.requireLength("名称空间", projectContainer.getNamespaceId(), 1, 255, true);
		Validators.requireLength("GitTag", projectContainer.getGitTag(), 1, 255, true);
		Validators.requireLength("工程构建命令", projectContainer.getNamespaceId(), 1, 65535, true);
		Validators.requireLength("拷贝至容器目录", projectContainer.getCpToContainerPath(), 0, 65535, false);
	}

	@Override
	protected void beforeInsert(ProjectContainer projectContainer) {
		User user = userService.findById(getCurrentUser().getUserId());
		projectContainer.setPublisher(user.getUsername());
		projectContainer.setPubDate(DateUtil.toLocalDateTime(DateUtil.now()));
		projectContainer.setStatus(ProjectContainer.Status.WAIT.getValue());
		BoundSetOperations<String, Object> setOps = redisTemplate.boundSetOps("build");
		Boolean build = setOps.isMember(projectContainer.getProjectId());
		if (build != null && build) {
			throw new ServiceException("所选的工程中有正在部署的任务，请稍后再试");
		} else {
			setOps.add(projectContainer.getProjectId());
			try {
				BUILD_BLOCKING_QUEUE.put(projectContainer);
			} catch (InterruptedException e) {
				log.error("阻塞队列put异常=>", e);
				throw new ServiceException("出现错误，请稍后再试");
			}
			this.runBuild();
		}
	}

	private void runBuild() {
		try {
			ProjectContainer projectContainer = BUILD_BLOCKING_QUEUE.take();
			BuildType buildType = buildTypeService.findById(projectContainer.getBuildTypeId());
			for (JenkinsService jenkinsService : jenkinsServices) {
				if (jenkinsService.getBuildType().equals(buildType.getName())) {
					try {
						jenkinsService.build(projectContainer);
					} catch (IOException e) {
						projectContainer.setStatus(ProjectContainer.Status.FAILURE.getValue());
						log.error("{}.工程部署失败=>", projectContainer.getProjectId(), e);
					}
					break;
				}
			}
			BoundSetOperations<String, Object> setOps = redisTemplate.boundSetOps("build");
			setOps.remove(projectContainer.getProjectId());
		} catch (InterruptedException e) {
			log.error("阻塞队列获取实例异常", e);
		}

	}

	@Override
	public IPage<ProjectContainerDTO> findBySpecificFields(ProjectContainerSearch search) {
		if (StringUtil.isBlank(search.getProjectId())) {
			throw new ServiceException("工程ID 不能为空");
		}
		QueryWrapper<ProjectContainer> wrapper = new QueryWrapper<>();
		wrapper.eq(PROJECT_ID_COLUMN, search.getProjectId());
		if (StringUtil.isNotBlank(search.getGitTag())) {
			wrapper.eq(TAG_COLUMN, search.getGitTag());
		}
		if (StringUtil.isNotBlank(search.getNamespaceId())) {
			wrapper.eq(NAMESPACE_COLUMN, search.getNamespaceId());
		}
		if (StringUtil.isNotBlank(search.getPublisher())) {
			wrapper.like(PUBLISHER_COLUMN, search.getPublisher());
		}
		if (StringUtil.isNotBlank(search.getPubStartDate())) {
			wrapper.gt(PUB_DATE_COLUMN, search.getPubStartDate());
		}
		if (StringUtil.isNotBlank(search.getPubEndDate())) {
			wrapper.lt(PUB_DATE_COLUMN, search.getPubEndDate());
		}
		Page<ProjectContainer> page = new Page<>(search.getCurrent(), search.getSize());
		return pageAll(page, wrapper).convert(ProjectContainer::toDTO);
	}

	@Override
	public Users.SimpleUser getCurrentUser() {
		return Users.getSimpleUser();
	}

	@Override
	public Role getRole() {
		return roleService.findById(getCurrentUser().getRoleId());
	}

}
