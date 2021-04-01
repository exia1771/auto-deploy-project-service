package github.exia1771.deploy.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import github.exia1771.deploy.common.service.BaseService;
import github.exia1771.deploy.core.dto.ImageTemplateDTO;
import github.exia1771.deploy.core.entity.ImageTemplate;

import java.util.List;

public interface ImageTemplateService extends BaseService<String, ImageTemplate> {

	Page<ImageTemplate> pageByName(ImageTemplate template);

	List<String> findDistinctTemplateName();

	List<String> findTemplateNameByKeyword(String keyword);

	List<ImageTemplateDTO> findTagsByTemplateName(String templateName);

	ImageTemplate findIdByTemplateNameAndTag(ImageTemplate template);
}
