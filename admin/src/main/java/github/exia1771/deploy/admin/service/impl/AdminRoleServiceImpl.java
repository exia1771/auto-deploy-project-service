package github.exia1771.deploy.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import github.exia1771.deploy.admin.entity.SearchEntity;
import github.exia1771.deploy.admin.service.AdminRoleService;
import github.exia1771.deploy.common.entity.Role;
import github.exia1771.deploy.common.service.BaseService;
import github.exia1771.deploy.common.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdminRoleServiceImpl implements AdminRoleService {

	private final RoleService roleService;
	private static final String NAME_COLUMN = "name";

	@Override
	public QueryWrapper<Role> getWrapper(SearchEntity entity) {
		QueryWrapper<Role> wrapper = new QueryWrapper<>();
		wrapper.like(NAME_COLUMN, entity.getSearchText());
		return wrapper;
	}

	@Override
	public BaseService<String, Role> getService() {
		return roleService;
	}

	@Override
	public void deleteById(String id) {
		roleService.deleteById(id);
	}

}
