package github.exia1771.deploy.common.service.impl;

import github.exia1771.deploy.common.entity.Dept;
import github.exia1771.deploy.common.mapper.DeptMapper;
import github.exia1771.deploy.common.service.DeptService;
import org.springframework.stereotype.Service;

@Service
public class DeptServiceImpl extends BaseServiceImpl<String, Dept> implements DeptService {

	private final DeptMapper mapper;

	public DeptServiceImpl(DeptMapper mapper) {
		super(mapper);
		this.mapper = mapper;
	}
}
