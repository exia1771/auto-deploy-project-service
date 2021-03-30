package github.exia1771.deploy.core.service.impl;

import github.exia1771.deploy.common.service.impl.BaseServiceImpl;
import github.exia1771.deploy.core.entity.BuildType;
import github.exia1771.deploy.core.mapper.BuildTypeMapper;
import github.exia1771.deploy.core.service.BuildTypeService;
import org.springframework.stereotype.Service;

@Service
public class BuildTypeServiceImpl extends BaseServiceImpl<String, BuildType> implements BuildTypeService {

	private final BuildTypeMapper mapper;

	public BuildTypeServiceImpl(BuildTypeMapper mapper) {
		super(mapper);
		this.mapper = mapper;
	}
}
