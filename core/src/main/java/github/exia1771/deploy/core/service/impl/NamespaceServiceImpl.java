package github.exia1771.deploy.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import github.exia1771.deploy.common.exception.ServiceException;
import github.exia1771.deploy.common.service.impl.BaseServiceImpl;
import github.exia1771.deploy.core.entity.Namespace;
import github.exia1771.deploy.core.mapper.NamespaceMapper;
import github.exia1771.deploy.core.service.NamespaceService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NamespaceServiceImpl extends BaseServiceImpl<String, Namespace> implements NamespaceService {

	private final NamespaceMapper mapper;

	private static final String NAME_COLUMN = "name";

	public NamespaceServiceImpl(NamespaceMapper mapper) {
		super(mapper);
		this.mapper = mapper;
	}

	@Override
	protected void beforeInsert(Namespace namespace) {
		QueryWrapper<Namespace> wrapper = new QueryWrapper<>();
		wrapper.eq(NAME_COLUMN, namespace.getName());
		if (isExisted(wrapper)) {
			throw new ServiceException("已经存在的名称");
		}
	}

	@Override
	public List<Namespace> findByKeyword(String keyword) {
		QueryWrapper<Namespace> wrapper = new QueryWrapper<>();
		wrapper.like(NAME_COLUMN, keyword);
		return mapper.selectList(wrapper);
	}
}
