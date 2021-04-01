package github.exia1771.deploy.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import github.exia1771.deploy.admin.entity.SearchEntity;
import github.exia1771.deploy.admin.service.AdminUserService;
import github.exia1771.deploy.common.entity.Role;
import github.exia1771.deploy.common.entity.User;
import github.exia1771.deploy.common.entity.dto.RoleDTO;
import github.exia1771.deploy.common.entity.dto.UserDTO;
import github.exia1771.deploy.common.exception.ServiceException;
import github.exia1771.deploy.common.service.BaseService;
import github.exia1771.deploy.common.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class AdminUserServiceImpl implements AdminUserService {

	private final UserService userService;
	private static final String USERNAME_COLUMN = "username";
	private static final String EMAIL_COLUMN = "email";
	private static final String TELEPHONE_COLUMN = "telephone";

	@Override
	public UserDTO login(User user) {
		try {
			UserDTO userDTO = userService.login(user);
			isAdminUser(userDTO);
			return userDTO;
		} catch (UnsupportedEncodingException e) {
			log.error("登录异常=>", e);
			throw new ServiceException("登录失败");
		}
	}

	private void isAdminUser(UserDTO userDTO) {
		RoleDTO role = userDTO.getRole();
		if (!Role.Type.ADMIN.getName().equals(role.getName())) {
			throw new ServiceException("只有管理员角色才能登录");
		}
	}

	@Override
	public UserDTO login() {
		UserDTO login = userService.login();
		isAdminUser(login);
		return login;
	}

	@Override
	public void logout() {
		userService.logout();
	}


	@Override
	public QueryWrapper<User> getWrapper(SearchEntity entity) {
		QueryWrapper<User> wrapper = new QueryWrapper<>();
		String searchText = entity.getSearchText();

		wrapper.like(USERNAME_COLUMN, searchText)
				.or(q -> q.like(EMAIL_COLUMN, searchText))
				.or(q -> q.like(TELEPHONE_COLUMN, searchText));


		return wrapper;
	}

	@Override
	public BaseService<String, User> getService() {
		return userService;
	}

	@Override
	public void deleteById(String id) {
		if (getCurrentUser().getUserId().equals(id)) {
			throw new ServiceException("不能删除自己的账号!");
		} else {
			userService.deleteById(id);
		}
	}

	@Override
	public List<UserDTO> findUsersNotHaveDeptId() {
		return userService.findByNotHaveDeptId();
	}

	@Override
	public List<UserDTO> findUsersByDeptId(String id) {
		return userService.findByDeptId(id);
	}

	@Override
	public void batchUpdateDeptId(String deptId, List<String> userId) {
		userService.batchUpdateDeptId(deptId, userId);
	}
}
