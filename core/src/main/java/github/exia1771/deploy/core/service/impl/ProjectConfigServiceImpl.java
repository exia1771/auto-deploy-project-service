package github.exia1771.deploy.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import github.exia1771.deploy.common.entity.Role;
import github.exia1771.deploy.common.service.RoleService;
import github.exia1771.deploy.common.service.impl.BaseServiceImpl;
import github.exia1771.deploy.common.util.Commons;
import github.exia1771.deploy.common.util.Users;
import github.exia1771.deploy.common.util.Validators;
import github.exia1771.deploy.core.dto.ProjectConfigDTO;
import github.exia1771.deploy.core.entity.ProjectConfig;
import github.exia1771.deploy.core.mapper.ProjectConfigMapper;
import github.exia1771.deploy.core.service.ProjectConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ProjectConfigServiceImpl extends BaseServiceImpl<String, ProjectConfig> implements ProjectConfigService {

	private final ProjectConfigMapper mapper;
	private static final String NAMESPACE_COLUMN = "namespace_id";
	private static final String PROJECT_COLUMN = "project_id";

	@Autowired
	private RoleService roleService;

	public ProjectConfigServiceImpl(ProjectConfigMapper mapper) {
		super(mapper);
		this.mapper = mapper;
	}

	@Override
	protected void beforeSave(ProjectConfig projectConfig) {
		Validators.requireNotNull("工程ID", projectConfig.getProjectId());
		Validators.requireNotNull("名称空间ID", projectConfig.getNamespaceId());
		Validators.requireSize("内存大小", projectConfig.getMemory(), 1024, 2048);
		Validators.requireSize("CPU核心数量", projectConfig.getCore(), 1, 4);
		if (projectConfig.getPort() != null) {
			Validators.requireClassType("端口号", projectConfig.getPort(), JSON::parseArray);
		}
	}

	@Override
	protected void beforeInsert(ProjectConfig projectConfig) {
		projectConfig.setId(Commons.getId());
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
	public ProjectConfigDTO findByProjectIdAndNamespaceId(String projectId, String namespaceId) {
		QueryWrapper<ProjectConfig> wrapper = new QueryWrapper<>();
		wrapper.eq(PROJECT_COLUMN, projectId);
		wrapper.eq(NAMESPACE_COLUMN, namespaceId);
		ProjectConfig projectConfig = mapper.selectOne(wrapper);
		if (projectConfig == null) {
			return new ProjectConfig().toDTO();
		}
		return projectConfig.toDTO();
	}
}
