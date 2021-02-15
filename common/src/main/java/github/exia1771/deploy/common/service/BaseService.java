package github.exia1771.deploy.common.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import github.exia1771.deploy.common.entity.AbstractEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface BaseService<K extends Serializable, T extends AbstractEntity<K>> {

    T save(T t);

    T findById(K id);

    List<T> findAll();

    IPage<T> pageAll(Page<T> page);

    Boolean isExisted(Map<String, Object> params);

}
