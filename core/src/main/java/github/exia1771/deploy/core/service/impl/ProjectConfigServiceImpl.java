package github.exia1771.deploy.core.service.impl;

import github.exia1771.deploy.common.service.impl.BaseServiceImpl;
import github.exia1771.deploy.core.entity.ProjectConfig;
import github.exia1771.deploy.core.mapper.ProjectConfigMapper;
import github.exia1771.deploy.core.service.ProjectConfigService;
import org.springframework.stereotype.Service;

@Service
public class ProjectConfigServiceImpl extends BaseServiceImpl<String, ProjectConfig> implements ProjectConfigService {

    private ProjectConfigMapper mapper;

    public ProjectConfigServiceImpl(ProjectConfigMapper mapper) {
        super(mapper);
        this.mapper = mapper;
    }
}
