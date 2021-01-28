package github.exia1771.deploy.common.service.impl;

import github.exia1771.deploy.common.entity.User;
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
    private static final String USER_NAME = "username";

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
    public String submit(User user) {
        save(user);

        String[] params = {"userId", "roleId"};
        Map<String, Object> claims = new HashMap<>();

        claims.put(params[0], user.getId());
        claims.put(params[1], user.getRoleId());

        return Tokens.create(claims);
    }

}
