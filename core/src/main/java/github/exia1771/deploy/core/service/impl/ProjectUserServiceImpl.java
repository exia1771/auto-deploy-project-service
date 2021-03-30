package github.exia1771.deploy.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import github.exia1771.deploy.common.entity.Role;
import github.exia1771.deploy.common.service.RoleService;
import github.exia1771.deploy.common.service.impl.BaseServiceImpl;
import github.exia1771.deploy.common.util.Users;
import github.exia1771.deploy.common.util.Validators;
import github.exia1771.deploy.core.entity.ProjectUser;
import github.exia1771.deploy.core.mapper.ProjectUserMapper;
import github.exia1771.deploy.core.service.ProjectUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectUserServiceImpl extends BaseServiceImpl<String, ProjectUser> implements ProjectUserService {

	@Autowired
	private RoleService roleService;

	private final ProjectUserMapper mapper;

	private final static String PROJECT_ID_COLUMN = "project_id";
	private final static String USER_ID_COLUMN = "user_id";
	private final static String ID_COLUMN = "id";


	public ProjectUserServiceImpl(ProjectUserMapper mapper) {
		super(mapper);
		this.mapper = mapper;
	}

	@Override
	public Users.SimpleUser getCurrentUser() {
		return Users.getSimpleUser();
	}

	@Override
	public Role getRole() {
		return roleService.findById(getCurrentUser().getRoleId());
	}

	@Override
	public int addProjectMember(ProjectUser projectUser) {
		ProjectUser save = save(projectUser);
		return save == null ? 0 : 1;
	}

	@Override
	public int batchAddProjectMember(List<ProjectUser> projectUsers) {
		projectUsers.forEach(projectUser -> {
			projectUser.setCreatorId(getCurrentUser().getUserId());
			projectUser.setUpdaterId(getCurrentUser().getUserId());
		});
		return mapper.batchAddProjectMember(projectUsers);
	}

	@Override
	public int removeProjectMember(ProjectUser projectUser) {
		QueryWrapper<ProjectUser> queryWrapper = new QueryWrapper<>();

		queryWrapper.eq(PROJECT_ID_COLUMN, projectUser.getProjectId());
		queryWrapper.eq(USER_ID_COLUMN, projectUser.getUserId());

		return mapper.delete(queryWrapper);
	}

	@Override
	public int batchRemoveProjectMember(List<ProjectUser> projectUsers) {
		return mapper.batchRemoveProjectMember(projectUsers);
	}

	@Override
	public List<ProjectUser> findByUserId(String userId) {
		QueryWrapper<ProjectUser> wrapper = new QueryWrapper<>();
		wrapper.eq(USER_ID_COLUMN, userId);
		return mapper.selectList(wrapper);
	}

	@Override
	public List<ProjectUser> findByProjectId(String projectId) {
		QueryWrapper<ProjectUser> wrapper = new QueryWrapper<>();
		wrapper.eq(PROJECT_ID_COLUMN, projectId);
		return mapper.selectList(wrapper);
	}

	@Override
	public int batchUpdate(String projectId, List<String> userId) {
		Validators.requireNotBlank("工程ID", projectId);
		if (userId.isEmpty()) {
			throw new ValidationException("用户ID列表不能为空");
		}

		List<ProjectUser> projectUsers = findByProjectId(projectId);
		List<String> id = projectUsers.stream().map(ProjectUser::getId).collect(Collectors.toList());
		List<String> originUserId = projectUsers.stream().map(ProjectUser::getUserId).collect(Collectors.toList());

		if (userId.equals(originUserId)) {
			return 0;
		}

		// 可优化
		batchDeleteByIdList(id);
		List<ProjectUser> collect = userId.stream().map(user -> {
			ProjectUser temp = new ProjectUser();
			temp.setUserId(user);
			temp.setProjectId(projectId);
			return temp;
		}).collect(Collectors.toList());
		return batchAddProjectMember(collect);
	}
}
