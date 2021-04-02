package github.exia1771.deploy.admin.service;

import github.exia1771.deploy.common.entity.User;
import github.exia1771.deploy.common.entity.dto.UserDTO;

import java.util.List;

public interface AdminUserService extends SearchPagedService<User, String> {

	UserDTO login();

	UserDTO login(User user);

	void logout();

	List<UserDTO> findUsersNotHaveDeptId();

	List<UserDTO> findUsersByDeptId(String id);

	void batchUpdateDeptId(String deptId, List<String> userId);
}
