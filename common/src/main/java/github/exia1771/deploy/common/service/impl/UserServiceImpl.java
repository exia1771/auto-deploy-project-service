package github.exia1771.deploy.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import github.exia1771.deploy.common.entity.User;
import github.exia1771.deploy.common.entity.dto.UserDTO;
import github.exia1771.deploy.common.exception.ServiceException;
import github.exia1771.deploy.common.mapper.UserMapper;
import github.exia1771.deploy.common.service.UserService;
import github.exia1771.deploy.common.util.Commons;
import github.exia1771.deploy.common.util.Tokens;
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
    private static final String[] params = {"userId", "roleId"};


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
        user.setRoleId(1L);
    }


    @Override
    public UserDTO submit(User user) {
        save(user);

        Map<String, Object> claims = new HashMap<>();

        claims.put(params[0], user.getId());
        claims.put(params[1], user.getRoleId());

        UserDTO userDTO = user.toDTO();
        userDTO.setToken(Tokens.create(claims));
        return userDTO;
    }

    @Override
    public User findByName(String name) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq(USER_NAME, name);
        return mapper.selectOne(wrapper);
    }

    @Override
    public UserDTO login(User user) {
        User savedUser = findByName(user.getUsername());
        if(savedUser == null){
            throw new ServiceException(USER_NAME_NOT_EXISTED);
        }
        if (!savedUser.getPassword().equals(user.getPassword())) {
            throw new ServiceException(PASSWORD_NOT_CORRECT);
        }
        UserDTO userDTO = savedUser.toDTO();

        Map<String, Object> map = new HashMap<>();
        map.put(params[0], savedUser.getId());
        map.put(params[1], savedUser.getRoleId());

        userDTO.setToken(Tokens.create(map));

        return userDTO;
    }

    @Override
    public UserDTO login(String token) {
        Map<String, Object> parse = Tokens.parse(token);
        Object userId = parse.getOrDefault(params[0], null);
        if(userId == null){
            throw new ServiceException(Tokens.TOKEN_ILLEGAL);
        }
        return findById(Long.valueOf(userId.toString())).toDTO();
    }
}
