package github.exia1771.deploy.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import github.exia1771.deploy.common.entity.AbstractEntity;
import github.exia1771.deploy.common.entity.Role;
import github.exia1771.deploy.common.entity.User;
import github.exia1771.deploy.common.service.BaseService;
import github.exia1771.deploy.common.util.Dates;
import github.exia1771.deploy.common.util.Users;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public abstract class BaseServiceImpl<K extends Serializable, T extends AbstractEntity<K>> implements BaseService<K, T> {

    private final BaseMapper<T> mapper;

    public BaseServiceImpl(BaseMapper<T> mapper) {
        this.mapper = mapper;
    }

    protected void beforeInsert(T t) {
    }

    protected void beforeUpdate(T t) {
    }

    @Override
    public T save(T t) {
        if (t.getId() == null) {
            beforeInsert(t);
            t.setCreationTime(Dates.now());
            t.setUpdateTime(Dates.now());
            mapper.insert(t);
        } else {
            beforeUpdate(t);
            t.setUpdateTime(Dates.now());
            mapper.updateById(t);
        }
        return t;
    }

    protected void beforeDelete(K id){}

    @Override
    public Boolean deleteById(K id) {
        beforeDelete(id);
        int rows = mapper.deleteById(id);
        return rows != 0;
    }

    @Override
    public T findById(K id) {
        return mapper.selectById(id);
    }

    @Override
    public List<T> findAll() {
        return mapper.selectList(null);
    }

    @Override
    public IPage<T> pageAll(Page<T> page) {
        return mapper.selectPage(page, null);
    }

    @Override
    public Boolean isExisted(Map<String, Object> params) {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        wrapper.allEq(params);
        List<T> list = mapper.selectList(wrapper);
        return list != null && list.size() >= 1;
    }
}
