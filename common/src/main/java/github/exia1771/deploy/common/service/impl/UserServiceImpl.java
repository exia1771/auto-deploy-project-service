package github.exia1771.deploy.common.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import github.exia1771.deploy.common.entity.FileRequest;
import github.exia1771.deploy.common.entity.Password;
import github.exia1771.deploy.common.entity.Role;
import github.exia1771.deploy.common.entity.User;
import github.exia1771.deploy.common.entity.dto.FileDTO;
import github.exia1771.deploy.common.entity.dto.UserDTO;
import github.exia1771.deploy.common.exception.ServiceException;
import github.exia1771.deploy.common.mapper.UserMapper;
import github.exia1771.deploy.common.service.FileService;
import github.exia1771.deploy.common.service.RoleService;
import github.exia1771.deploy.common.service.UserService;
import github.exia1771.deploy.common.util.Commons;
import github.exia1771.deploy.common.util.Tokens;
import github.exia1771.deploy.common.util.Users;
import github.exia1771.deploy.common.util.Validators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends BaseServiceImpl<String, User> implements UserService {

	private final UserMapper mapper;
	private final HttpServletResponse response;
	private static final String USER_NAME_EXISTED = "用户名已存在!";
	private static final String USER_NAME_NOT_EXISTED = "用户名不存在!";
	private static final String USER_NAME = "username";
	private static final String PASSWORD_NOT_CORRECT = "密码错误!";
	private static final Long DEFAULT_ROLE_ID = 1L;
	private static final String DEPT_ID_COLUMN = "dept_id";
	private static final String ID_COLUMN = "id";

	@Autowired
	private FileService fileService;

	@Autowired
	private RoleService roleService;

	public UserServiceImpl(UserMapper mapper, HttpServletResponse response) {
		super(mapper);
		this.mapper = mapper;
		this.response = response;
	}

	@Override
	public Boolean isExistedName(String name) {
		Map<String, Object> params = new HashMap<String, Object>() {{
			put(USER_NAME, name);
		}};
		if (isExisted(params)) {
			throw new ServiceException(USER_NAME_EXISTED);
		}
		return false;
	}

	@Override
	protected void beforeSave(User user) {
		if (isExistedName(user.getUsername())) {
			throw new ServiceException(USER_NAME_EXISTED);
		}
	}

	@Override
	protected void beforeInsert(User user) {
		user.setId(Commons.getId());
		user.setRoleId(DEFAULT_ROLE_ID.toString());
	}


	@Override
	public UserDTO submit(User user) throws UnsupportedEncodingException {
		save(user);

		UserDTO userDTO = user.toDTO();
		Tokens.setCookie(response, Users.getUserToken(user));
		return userDTO;
	}

	@Override
	public User findByName(String name) {
		QueryWrapper<User> wrapper = new QueryWrapper<>();
		wrapper.eq(USER_NAME, name);
		return mapper.selectOne(wrapper);
	}

	@Override
	public void logout() {
		Users.SimpleUser user = Users.getSimpleUser();
		Users.discardUserToken(user.getUserId());
	}

	@Override
	public UserDTO login(User user) throws UnsupportedEncodingException {
		User savedUser = findByName(user.getUsername());
		if (savedUser == null) {
			throw new ServiceException(USER_NAME_NOT_EXISTED);
		}
		if (!savedUser.getPassword().equals(user.getPassword())) {
			throw new ServiceException(PASSWORD_NOT_CORRECT);
		}
		UserDTO userDTO = savedUser.toDTO();

		String token = Users.getUserToken(savedUser);
		Tokens.setCookie(response, token);

		Role role = roleService.findById(savedUser.getRoleId());
		userDTO.setRole(role.toDTO());

		return userDTO;
	}

	@Override
	public UserDTO login() {
		Users.SimpleUser user = Users.getSimpleUser();

		UserDTO userDTO = findById(user.getUserId()).toDTO();
		Role role = roleService.findById(user.getRoleId());
		userDTO.setRole(role.toDTO());

		return userDTO;
	}

	private void validateUpdate(User user) {
		String email = user.getEmail();
		if (StrUtil.isNotBlank(email)) {
			if (255 < email.length() || email.length() < 6) {
				throw new ValidationException("电子邮件的长度必须在 6~255 个字符");
			}

			String pattern = "^\\w+@[a-zA-Z0-9]{2,}(?:\\.[a-z]{1,4}){1,3}$";
			if (!Pattern.matches(pattern, email)) {
				throw new ValidationException("请输入正确的电子邮件");
			}
		}

		String telephone = user.getTelephone();
		if (StrUtil.isNotBlank(telephone)) {
			if (255 < telephone.length() || telephone.length() < 6) {
				throw new ValidationException("电子邮件的长度必须在 6~255 个字符");
			}

			String pattern = "^(([0-9]{1,11})|([0-9\\\\-]))$";
			if (!Pattern.matches(pattern, telephone)) {
				throw new ValidationException("请输入正确的联系电话");
			}
		}
	}

	@Override
	public UserDTO updateBasicInfo(User user) {
		validateUpdate(user);

		User saved = getUser();
		saved.setEmail(user.getEmail());
		saved.setTelephone(user.getTelephone());
		saved = save(saved);

		return saved.toDTO();
	}

	private User getUser() {
		Users.SimpleUser simpleUser = Users.getSimpleUser();
		return findById(simpleUser.getUserId());
	}


	@Override
	public FileDTO uploadAvatar(MultipartFile file) {
		Users.SimpleUser user = Users.getSimpleUser();
		long maxSize = 1024 * 1024 * 4;
		String directory = FileDTO.FILE_SEPARATOR + "avatar" + FileDTO.FILE_SEPARATOR;
		String separator = "-";

		FileRequest fileRequest = new FileRequest() {{
			setMaxSize(maxSize);
			setDirectory(directory);
			setFileName(user.getUserId() + separator + file.getOriginalFilename());
		}};

		FileDTO upload = fileService.upload(file, fileRequest);
		updateAvatar(new User() {{
			setAvatarAddress(upload.getUrl());
		}});
		return upload;
	}

	@Override
	public UserDTO updateAvatar(User user) {

		User saved = getUser();
		saved.setAvatarAddress(user.getAvatarAddress());
		saved = save(saved);

		return saved.toDTO();
	}

	private void validatePassword(User user, Password password) {
		if (!password.getNewPassword().equals(password.getConfirmedPassword())) {
			throw new ServiceException("新密码与确认密码输入不一致!");
		}

		if (!user.getPassword().equals(password.getOldPassword())) {
			throw new ServiceException("旧密码输入不正确!");
		}

		if (user.getPassword().equals(password.getNewPassword())) {
			throw new ServiceException("新密码不能与旧密码相同!");
		}
	}

	@Override
	public void changePassword(Password password) {
		User user = getUser();
		validatePassword(user, password);

		user.setPassword(password.getNewPassword());
		save(user);
	}

	@Override
	public List<UserDTO> findByDeptId(String id) {
		QueryWrapper<User> wrapper = new QueryWrapper<>();
		wrapper.eq(DEPT_ID_COLUMN, id);
		List<User> users = mapper.selectList(wrapper);
		return users.stream().map(User::toDTO).collect(Collectors.toList());
	}

	@Override
	public List<UserDTO> findByNotHaveDeptId() {
		QueryWrapper<User> wrapper = new QueryWrapper<>();
		wrapper.eq(DEPT_ID_COLUMN, "");
		wrapper.or(q -> q.isNull(DEPT_ID_COLUMN));
		List<User> users = mapper.selectList(wrapper);
		return users.stream().map(User::toDTO).collect(Collectors.toList());
	}

	@Override
	public void batchUpdateDeptId(String deptId, List<String> userId) {
		Validators.requireLength("部门ID", deptId, 1, 255, true);
		for (String s : userId) {
			Validators.requireLength("用户ID", s, 1, 255, true);
		}

		QueryWrapper<User> wrapper = new QueryWrapper<>();
		wrapper.in(ID_COLUMN, userId);
		List<User> users = mapper.selectList(wrapper);

		if (users.size() < userId.size()) {
			throw new ServiceException("指定的用户ID个别不存在");
		}


		List<UserDTO> originUser = findByDeptId(deptId);
		Map<String, User> map = new HashMap<>();

		// 老部门员工的部门ID全部置为null
		for (UserDTO userDTO : originUser) {
			User temp = new User();
			temp.setId(userDTO.getId());
			temp.setDeptId(null);
			map.put(userDTO.getId(), temp);
		}

		int before = map.size();
		int after = 0;
		// 根据传入的用户ID查找出的部门ID全部置为deptId，map的Key就是需要更新的用户列表
		for (User user : users) {
			user.setDeptId(deptId);
			if (map.containsKey(user.getId())) {
				after++;
			}
			map.put(user.getId(), user);
		}

		// 如果传入的ID列表个数与数据库一致 且 更新前后数量相同则不需要更新
		if (originUser.size() == userId.size() && before == after) {
			return;
		}

		mapper.batchUpdateDeptId(new ArrayList<>(map.values()));
	}


	@Override
	public void batchUpdateDeptId(List<User> users) {
		for (User user : users) {
			Validators.requireLength("用户ID", user.getId(), 1, 255, true);
		}
		mapper.batchUpdateDeptId(users);
	}
}
