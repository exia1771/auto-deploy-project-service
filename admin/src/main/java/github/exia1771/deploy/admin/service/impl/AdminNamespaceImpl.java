package github.exia1771.deploy.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import github.exia1771.deploy.admin.entity.SearchEntity;
import github.exia1771.deploy.admin.service.AdminNamespaceService;
import github.exia1771.deploy.common.service.BaseService;
import github.exia1771.deploy.core.entity.Namespace;
import github.exia1771.deploy.core.service.NamespaceService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdminNamespaceImpl implements AdminNamespaceService {

	private final NamespaceService namespaceService;
	private static final String NAME_COLUMN = "name";

	@Override
	public QueryWrapper<Namespace> getWrapper(SearchEntity entity) {
		QueryWrapper<Namespace> wrapper = new QueryWrapper<>();
		wrapper.like(NAME_COLUMN, entity.getSearchText());
		return wrapper;
	}

	@Override
	public BaseService<String, Namespace> getService() {
		return namespaceService;
	}

	@Override
	public void deleteById(String id) {
		namespaceService.deleteById(id);
	}
}
