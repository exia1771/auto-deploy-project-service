package github.exia1771.deploy.core.service;

import github.exia1771.deploy.common.service.BaseService;
import github.exia1771.deploy.core.dto.ProjectConfigDTO;
import github.exia1771.deploy.core.entity.ProjectConfig;

import java.util.List;

public interface ProjectConfigService extends BaseService<String, ProjectConfig> {

	List<ProjectConfigDTO> findDistinctNamespaces();

}
