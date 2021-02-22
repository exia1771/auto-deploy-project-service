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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Service
@Transactional
public class UserServiceImpl extends BaseServiceImpl<String, User> implements UserService {

    private final UserMapper mapper;
    private final HttpServletResponse response;
    private static final String USER_NAME_EXISTED = "用户名已存在!";
    private static final String USER_NAME_NOT_EXISTED = "请输入正确的用户名!";
    private static final String USER_NAME = "username";
    private static final String PASSWORD_NOT_CORRECT = "密码错误!";
    private static final Long DEFAULT_ROLE_ID = 1L;

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
    protected void beforeInsert(User user) {
        if (isExistedName(user.getUsername())) {
            throw new ServiceException(USER_NAME_EXISTED);
        }
        user.setId(Commons.getId());
        user.setCreatorId(user.getId());
        user.setUpdaterId(user.getId());
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
        Users.discardUserToken(user.getUserId().toString());
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
        String scheme = "http://";
        Users.SimpleUser user = Users.getSimpleUser();
        long maxSize = 1024 * 1024 * 4;
        String directory = FileDTO.FILE_SEPARATOR + "avatar" + FileDTO.FILE_SEPARATOR;
        String separator = "-";

        FileRequest fileRequest = new FileRequest() {{
            setMaxSize(maxSize);
            setDirectory(directory);
            setFileName(user.getUserId().toString() + separator + file.getOriginalFilename());
        }};

        FileDTO upload = fileService.upload(file, fileRequest);
        upload.setUrl(scheme + upload.getUrl());
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
}
