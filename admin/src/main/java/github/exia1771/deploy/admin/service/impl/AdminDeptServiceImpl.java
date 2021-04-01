package github.exia1771.deploy.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import github.exia1771.deploy.admin.entity.SearchEntity;
import github.exia1771.deploy.admin.service.AdminDeptService;
import github.exia1771.deploy.common.entity.Dept;
import github.exia1771.deploy.common.service.BaseService;
import github.exia1771.deploy.common.service.DeptService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdminDeptServiceImpl implements AdminDeptService {

	private final DeptService deptService;

	@Override
	public BaseService<String, Dept> getService() {
		return deptService;
	}

	@Override
	public QueryWrapper<Dept> getWrapper(SearchEntity entity) {
		return null;
	}

	@Override
	public void deleteById(String id) {
		deptService.deleteById(id);
	}
}
