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
import github.exia1771.deploy.core.service.docker.ContainerService;
import github.exia1771.deploy.core.service.jenkins.JenkinsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

	private static final String GITHUB_URL_PREFIX = "https://github.com/";
	private static final String GITHUB_API_URL = "https://api.github.com/repos/";
	private static final String GITHUB_TAGS_API_SUFFIX = "/git/refs/tags";

	private static final String GITEE_URL_PREFIX = "https://gitee.com/";
	private static final String GITEE_API_URL = "https://gitee.com/api/v5/repos/";
	private static final String GITEE_TAGS_API_SUFFIX = "/tags";


	private static final String TAG_COLUMN = "git_tag";
	private static final String PUB_DATE_COLUMN = "pub_date";
	private static final String PUBLISHER_COLUMN = "publisher";
	private static final String NAMESPACE_COLUMN = "namespace_id";
	private static final String PROJECT_ID_COLUMN = "project_id";


	private static final BlockingQueue<ProjectContainer> BUILD_BLOCKING_QUEUE = new ArrayBlockingQueue<>(2);

	@Autowired
	private ExecutorService fixedThreadPool;

	@Autowired
	private ContainerService containerService;

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


		String ownerRepo;
		boolean isGitHub;
		if (gitUrl.startsWith(GITEE_URL_PREFIX)) {
			ownerRepo = gitUrl.split(GITEE_URL_PREFIX)[1];
			isGitHub = false;
		} else if (gitUrl.startsWith(GITHUB_URL_PREFIX)) {
			ownerRepo = gitUrl.split(GITHUB_URL_PREFIX)[1];
			isGitHub = true;
		} else {
			throw new ServiceException("不支持的Git类型，目前只有Gitee, GitHub");
		}

		ownerRepo = ownerRepo.replaceAll(REPOSITORY_SUFFIX, "");

		if (isGitHub) {
			return parseGitHub(keyword, ownerRepo);
		} else {
			return parseGitEE(keyword, ownerRepo);
		}
	}

	private List<String> parseGitHub(String keyword, String ownerRepo) {
		try {
			String json = restTemplate.getForObject(GITHUB_API_URL + ownerRepo + GITHUB_TAGS_API_SUFFIX, String.class);
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

	private List<String> parseGitEE(String keyword, String ownerRepo) {
		try {
			String json = restTemplate.getForObject(GITEE_API_URL + ownerRepo + GITEE_TAGS_API_SUFFIX, String.class);
			JSONArray array = JSON.parseArray(json);
			if (array == null) {
				return new ArrayList<>();
			}
			return array.stream()
					.map(o -> {
						JSONObject temp = (JSONObject) o;
						return temp.getString("name");
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
		Validators.requireLength("工程运行命令", projectContainer.getRunCommand(), 1, 65535, true);
		Validators.requireLength("拷贝至容器目录", projectContainer.getCpToContainerPath(), 1, 255, false);
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
			fixedThreadPool.execute(this::runBuild);
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
			mapper.updateById(projectContainer);
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
		wrapper.orderByDesc(PUB_DATE_COLUMN);
		Page<ProjectContainer> page = new Page<>(search.getCurrent(), search.getSize());
		return pageAll(page, wrapper).convert(ProjectContainer::toDTO);
	}

	@Override
	public String stopBuild(String projectContainerId) {
		ProjectContainer container = findById(projectContainerId);
		BuildType buildType = buildTypeService.findById(container.getBuildTypeId());
		BoundSetOperations<String, Object> abort = redisTemplate.boundSetOps("abort");
		String result = "";
		for (JenkinsService jenkinsService : jenkinsServices) {
			if (jenkinsService.getBuildType().equals(buildType.getName())) {
				try {
					abort.add(projectContainerId);
					result = jenkinsService.stop(container.getJenkinsJobName(), container.getJenkinsBuildNumber());
				} catch (IOException e) {
					container.setStatus(ProjectContainer.Status.ABORT_ERROR.getValue());
					Boolean member = abort.isMember(projectContainerId);
					if (member != null && member) {
						abort.remove(projectContainerId);
					}
					log.error("{}.{}终止构建失败=>{}", container.getJenkinsJobName(), container.getJenkinsBuildNumber(), e.getMessage());
				}
				break;
			}
		}
		if (container.getStatus() == ProjectContainer.Status.ABORT_ERROR.getValue()) {
			mapper.updateById(container);
			throw new ServiceException("终止失败");
		}
		return result;
	}

	@Override
	public String getContainerLog(String projectContainerId, long timestamp) {
		ProjectContainer projectContainer = findById(projectContainerId);
		try {
			return containerService.getContainerLogs(projectContainer.getDockerContainerId(), timestamp);
		} catch (IOException e) {
			log.error("获取容器{}日志失败=>", projectContainer.getDockerContainerId(), e);
		}
		return "";
	}


	@Override
	public void getContainerLogStream(String projectContainerId, long timestamp, HttpServletResponse response) {
		final String EXPOSE_HEADERS = "Access-Control-Expose-Headers";
		ProjectContainer projectContainer = findById(projectContainerId);
		Resource resource = containerService.getContainerLogStream(projectContainer.getDockerContainerId(), timestamp);
		if (resource == null) {
			throw new ServiceException("当前时刻不存在日志");
		}
		try {
			InputStream inputStream = resource.getInputStream();
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[4096];
			int n;
			while ((n = (inputStream.read(buffer))) != -1) {
				byteArrayOutputStream.write(buffer, 0, n);
			}

			response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
			response.setHeader(EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			response.setHeader(HttpHeaders.CONTENT_DISPOSITION, System.currentTimeMillis() + ".log");
			response.getOutputStream().write(byteArrayOutputStream.toByteArray());
			byteArrayOutputStream.close();
		} catch (IOException e) {
			log.error("获取输入流出错", e);
		}
	}

	@Override
	public ContainerService.Status restartContainer(String projectContainerId) {
		BoundSetOperations<String, Object> setOps = redisTemplate.boundSetOps("status");
		Boolean isRestarted = setOps.isMember(projectContainerId);
		if (isRestarted != null && isRestarted) {
			throw new ServiceException("当前容器正在重启，请勿重复点击");
		} else {
			setOps.add(projectContainerId);
		}
		ProjectContainer container = findById(projectContainerId);
		ContainerService.Status status = containerService.restart(container.getDockerContainerId());
		setOps.remove(projectContainerId);
		return status;
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
