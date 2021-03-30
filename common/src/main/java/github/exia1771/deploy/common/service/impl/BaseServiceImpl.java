package github.exia1771.deploy.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import github.exia1771.deploy.common.entity.AbstractEntity;
import github.exia1771.deploy.common.entity.Role;
import github.exia1771.deploy.common.service.BaseService;
import github.exia1771.deploy.common.util.DateUtil;
import github.exia1771.deploy.common.util.Users;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Transactional
public abstract class BaseServiceImpl<K extends Serializable, T extends AbstractEntity<K>> implements BaseService<K, T> {

	private final BaseMapper<T> mapper;

	public BaseServiceImpl(BaseMapper<T> mapper) {
		this.mapper = mapper;
	}

	protected void beforeSave(T t) {
	}

	protected void beforeInsert(T t) {
	}

	protected void beforeUpdate(T t) {
	}

	protected void validateCreatePri() {
		if (getRole() != null && (getRole().getCreatePri() == null || !getRole().getCreatePri())) {
			throw new ValidationException("该账号缺少创建权限");
		}
	}

	protected void validateUpdatePri() {
		if (getRole() != null && (getRole().getUpdatePri() == null || !getRole().getUpdatePri())) {
			throw new ValidationException("该账号缺少修改权限");
		}
	}

	protected void validateDeletePri(K id) {
		if (getRole() != null && (getRole().getDeletePri() == null || !getRole().getDeletePri())) {
			throw new ValidationException("该账号缺少删除权限");
		}
	}

	public Role getRole() {
		return null;
	}

	public Users.SimpleUser getCurrentUser() {
		return null;
	}

	@Override
	public T save(T t) {
		beforeSave(t);
		if (t.getId() == null) {
			validateCreatePri();
			if (getCurrentUser() != null) {
				t.setCreatorId(getCurrentUser().getUserId());
				t.setUpdaterId(getCurrentUser().getUserId());
			}

			t.setCreationTime(DateUtil.now());
			t.setUpdateTime(DateUtil.now());
			beforeInsert(t);
			mapper.insert(t);
		} else {
			validateUpdatePri();
			beforeUpdate(t);

			if (getCurrentUser() != null) {
				t.setUpdaterId(getCurrentUser().getUserId());
			}

			t.setUpdateTime(DateUtil.now());
			mapper.updateById(t);
		}
		return t;
	}

	protected void beforeDelete() {
	}


	@Override
	public Boolean deleteById(K id) {
		validateDeletePri(id);
		beforeDelete();
		int rows = mapper.deleteById(id);
		return rows != 0;
	}

	protected void validateBatchDeletePri(List<K> idList) {

	}

	@Override
	public int batchDeleteByIdList(List<K> idList) {
		validateBatchDeletePri(idList);
		beforeDelete();
		return mapper.deleteBatchIds(idList);
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
	public IPage<T> pageAll(Page<T> page, QueryWrapper<T> wrapper) {
		return mapper.selectPage(page, wrapper);
	}

	@Override
	public Boolean isExisted(Map<String, Object> params) {
		QueryWrapper<T> wrapper = new QueryWrapper<>();
		wrapper.allEq(params);
		return isExisted(wrapper);
	}

	public Boolean isExisted(Wrapper<T> wrapper) {
		List<T> list = mapper.selectList(wrapper);
		return list != null && list.size() >= 1;
	}
}
