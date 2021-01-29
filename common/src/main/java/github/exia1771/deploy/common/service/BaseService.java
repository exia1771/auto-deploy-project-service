package github.exia1771.deploy.common.service;

import github.exia1771.deploy.common.entity.AbstractEntity;

import java.io.Serializable;
import java.util.Map;

public interface BaseService<K extends Serializable, T extends AbstractEntity<K>> {

    T save(T t);

    T findById(K id);

    Boolean isExisted(Map<String, Object> params);

}
