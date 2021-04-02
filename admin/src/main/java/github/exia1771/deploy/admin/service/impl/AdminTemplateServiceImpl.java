package github.exia1771.deploy.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import github.exia1771.deploy.admin.entity.SearchEntity;
import github.exia1771.deploy.admin.service.AdminTemplateService;
import github.exia1771.deploy.common.service.BaseService;
import github.exia1771.deploy.core.entity.ImageTemplate;
import github.exia1771.deploy.core.service.ImageTemplateService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AdminTemplateServiceImpl implements AdminTemplateService {

	private final ImageTemplateService imageTemplateService;

	private static final String TEMPLATE_NAME_COLUMN = "template_name";
	private static final String TEMPLATE_TAG_COLUMN = "template_tag";

	@Override
	public QueryWrapper<ImageTemplate> getWrapper(SearchEntity entity) {
		QueryWrapper<ImageTemplate> wrapper = new QueryWrapper<>();
		wrapper.like(TEMPLATE_NAME_COLUMN, entity.getSearchText());
		wrapper.or(q -> q.like(TEMPLATE_TAG_COLUMN, entity.getSearchText()));
		return wrapper;
	}

	@Override
	public BaseService<String, ImageTemplate> getService() {
		return imageTemplateService;
	}

	@Override
	public void deleteById(String id) {

	}

	@Override
	public List<String> findName() {
		return imageTemplateService.findDistinctTemplateName();
	}

	@Override
	public List<ImageTemplate> findByName(String name) {
		return imageTemplateService.findByName(name);
	}

	@Override
	public ImageTemplate findById(String id) {
		return imageTemplateService.findById(id);
	}
}
