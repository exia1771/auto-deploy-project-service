package github.exia1771.deploy.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import github.exia1771.deploy.common.entity.AbstractEntity;
import github.exia1771.deploy.common.service.BaseService;

import java.io.Serializable;
import java.util.Map;

public class BaseServiceImpl<K extends Serializable, T extends AbstractEntity<K>> implements BaseService<K, T> {

    private final BaseMapper<T> mapper;

    public BaseServiceImpl(BaseMapper<T> mapper) {
        this.mapper = mapper;
    }

    @Override
    public boolean save(T t) {
        int result;
        if (t.getId() == null) {
            result = mapper.insert(t);
        } else {
            result = mapper.updateById(t);
        }
        return result != 0;
    }

    @Override
    public T findById(K id) {
        return mapper.selectById(id);
    }

    @Override
    public boolean isExisted(Map<String, Object> params) {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        wrapper.allEq(params);
        return mapper.selectOne(wrapper) != null;
    }
}
