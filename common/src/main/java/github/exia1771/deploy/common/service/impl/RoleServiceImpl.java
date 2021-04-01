package github.exia1771.deploy.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import github.exia1771.deploy.common.entity.Role;
import github.exia1771.deploy.common.exception.ServiceException;
import github.exia1771.deploy.common.mapper.RoleMapper;
import github.exia1771.deploy.common.service.RoleService;
import github.exia1771.deploy.common.util.Validators;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends BaseServiceImpl<String, Role> implements RoleService {

	private final RoleMapper roleMapper;
	private static final String NAME_COLUMN = "name";

	public RoleServiceImpl(RoleMapper roleMapper) {
		super(roleMapper);
		this.roleMapper = roleMapper;
	}

	@Override
	protected void beforeSave(Role role) {
		Validators.requireLength("名称", role.getName(), 1, 255, true);
		QueryWrapper<Role> wrapper = new QueryWrapper<>();
		wrapper.eq(NAME_COLUMN, role.getName());
		if (isExisted(wrapper)) {
			throw new ServiceException("已经存在的角色名");
		}
	}

	@Override
	protected void beforeDelete(String id) {
		if (getCurrentUser().getRoleId().equals(id)) {
			throw new ServiceException("无法删除自己所属的角色");
		}
	}
}
