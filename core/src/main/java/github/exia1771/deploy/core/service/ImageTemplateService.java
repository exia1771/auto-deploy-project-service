package github.exia1771.deploy.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import github.exia1771.deploy.common.service.BaseService;
import github.exia1771.deploy.core.entity.ImageTemplate;

public interface ImageTemplateService extends BaseService<Long, ImageTemplate> {

    Page<ImageTemplate> pageByName(ImageTemplate template);

}
