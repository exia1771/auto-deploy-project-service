package github.exia1771.deploy.common.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import github.exia1771.deploy.common.entity.AbstractEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface BaseService<K extends Serializable, T extends AbstractEntity<K>> {

	T save(T t);

	T findById(K id);

	Boolean deleteById(K id);

	int batchDeleteByIdList(List<K> idList);

	List<T> findAll();

	IPage<T> pageAll(Page<T> page);

	IPage<T> pageAll(Page<T> page, QueryWrapper<T> wrapper);

	Boolean isExisted(Map<String, Object> params);

	boolean isExisted(Wrapper<T> wrapper);
}
