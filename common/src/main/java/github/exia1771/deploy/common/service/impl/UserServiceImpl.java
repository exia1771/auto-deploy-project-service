package github.exia1771.deploy.common.service.impl;

import cn.hutool.core.util.IdUtil;
import github.exia1771.deploy.common.config.GlobalConfig;
import github.exia1771.deploy.common.entity.User;
import github.exia1771.deploy.common.mapper.UserMapper;
import github.exia1771.deploy.common.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class UserServiceImpl extends BaseServiceImpl<Long, User> implements UserService {

    private final UserMapper mapper;

    public UserServiceImpl(UserMapper mapper) {
        super(mapper);
        this.mapper = mapper;
    }

    @Override
    public boolean save(User user) {
        if (user.getId() == null) {
            user.setId(IdUtil.getSnowflake(GlobalConfig.WORK_ID, GlobalConfig.DATA_ID).nextId());
        }
        return super.save(user);
    }

    @Override
    public boolean isExistedName(String name) {
        String USER_NAME = "username";
        Map<String, Object> params = new HashMap<String, Object>() {{
            put(USER_NAME, name);
        }};
        return isExisted(params);
    }
}
