package github.exia1771.deploy.admin.service;

import github.exia1771.deploy.common.entity.Role;

public interface AdminRoleService extends SearchPagedService<Role, String> {

	void deleteById(String id);
}
