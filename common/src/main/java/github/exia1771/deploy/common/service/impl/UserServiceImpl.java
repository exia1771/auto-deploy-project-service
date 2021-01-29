package github.exia1771.deploy.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import github.exia1771.deploy.common.entity.User;
import github.exia1771.deploy.common.entity.dto.UserDTO;
import github.exia1771.deploy.common.exception.ServiceException;
import github.exia1771.deploy.common.mapper.UserMapper;
import github.exia1771.deploy.common.service.UserService;
import github.exia1771.deploy.common.util.Commons;
import github.exia1771.deploy.common.util.Users;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class UserServiceImpl extends BaseServiceImpl<Long, User> implements UserService {

    private final UserMapper mapper;
    private static final String USER_NAME_EXISTED = "用户名已存在!";
    private static final String USER_NAME_NOT_EXISTED = "请输入正确的用户名!";
    private static final String USER_NAME = "username";
    private static final String PASSWORD_NOT_CORRECT = "密码错误!";
    private static final Long DEFAULT_ROLE_ID = 1L;

    public UserServiceImpl(UserMapper mapper) {
        super(mapper);
        this.mapper = mapper;
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
        user.setRoleId(DEFAULT_ROLE_ID);
    }


    @Override
    public UserDTO submit(User user) {

        save(user);
        UserDTO userDTO = user.toDTO();
        userDTO.setToken(Users.getUserToken(user));
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
        Users.SimpleUser user = Users.getUser();
        Users.discardUserToken(user.getUserId().toString());
    }

    @Override
    public UserDTO login(User user) {
        User savedUser = findByName(user.getUsername());
        if (savedUser == null) {
            throw new ServiceException(USER_NAME_NOT_EXISTED);
        }
        if (!savedUser.getPassword().equals(user.getPassword())) {
            throw new ServiceException(PASSWORD_NOT_CORRECT);
        }
        UserDTO userDTO = savedUser.toDTO();

        userDTO.setToken(Users.getUserToken(savedUser));

        return userDTO;
    }

    @Override
    public UserDTO login() {
        Users.SimpleUser user = Users.getUser();
        return findById(user.getUserId()).toDTO();
    }
}
