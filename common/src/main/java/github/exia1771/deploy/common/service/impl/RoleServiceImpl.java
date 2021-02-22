package github.exia1771.deploy.common.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import github.exia1771.deploy.common.entity.Role;
import github.exia1771.deploy.common.mapper.RoleMapper;
import github.exia1771.deploy.common.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends BaseServiceImpl<String, Role> implements RoleService {

    private final RoleMapper roleMapper;

    public RoleServiceImpl(RoleMapper roleMapper, BaseMapper<Role> mapper) {
        super(mapper);
        this.roleMapper = roleMapper;
    }

}
