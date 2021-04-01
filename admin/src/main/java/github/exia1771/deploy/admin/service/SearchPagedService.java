package github.exia1771.deploy.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import github.exia1771.deploy.admin.entity.SearchEntity;
import github.exia1771.deploy.common.entity.AbstractEntity;
import github.exia1771.deploy.common.entity.dto.AbstractDTO;
import github.exia1771.deploy.common.service.BaseService;
import github.exia1771.deploy.common.util.Users;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public interface SearchPagedService<T extends AbstractEntity<K>, K extends Serializable> {

	default IPage<T> findPaged(SearchEntity entity) {
		Page<T> page = new Page<>(entity.getCurrent(), entity.getSize());
		return getService().pageAll(page, getWrapper(entity));
	}

	default List<T> findAll() {
		return getService().findAll();
	}

	default IPage<AbstractDTO<K>> findPagedDTO(SearchEntity entity) {
		return findPaged(entity).convert(T::toDTO);
	}

	default List<AbstractDTO<K>> findAllDTO() {
		return findAll().stream().map(T::toDTO).collect(Collectors.toList());
	}

	default Users.SimpleUser getCurrentUser() {
		return Users.getSimpleUser();
	}

	default void save(T t) {
		getService().save(t);
	}

	QueryWrapper<T> getWrapper(SearchEntity entity);

	BaseService<K, T> getService();
}
