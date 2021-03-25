package github.exia1771.deploy.core.service;

import github.exia1771.deploy.common.service.BaseService;
import github.exia1771.deploy.core.entity.Namespace;

import java.util.List;

public interface NamespaceService extends BaseService<String, Namespace> {

	List<Namespace> findByKeyword(String keyword);

}
