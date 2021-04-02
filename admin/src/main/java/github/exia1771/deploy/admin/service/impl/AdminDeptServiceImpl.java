package github.exia1771.deploy.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import github.exia1771.deploy.admin.entity.SearchEntity;
import github.exia1771.deploy.admin.service.AdminDeptService;
import github.exia1771.deploy.common.entity.Dept;
import github.exia1771.deploy.common.entity.User;
import github.exia1771.deploy.common.service.BaseService;
import github.exia1771.deploy.common.service.DeptService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AdminDeptServiceImpl implements AdminDeptService {

	private final DeptService deptService;

	private static final String NAME_COLUMN = "name";

	@Override
	public BaseService<String, Dept> getService() {
		return deptService;
	}

	@Override
	public QueryWrapper<Dept> getWrapper(SearchEntity entity) {
		QueryWrapper<Dept> wrapper = new QueryWrapper<>();
		wrapper.like(NAME_COLUMN, entity.getSearchText());
		return wrapper;
	}

	@Override
	public void deleteById(String id) {
		deptService.deleteById(id);
	}

	@Override
	public List<User> findUserByDeptId(String id) {
		return null;
	}
}
