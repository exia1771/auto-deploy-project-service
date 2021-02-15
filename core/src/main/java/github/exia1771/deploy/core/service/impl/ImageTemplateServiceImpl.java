package github.exia1771.deploy.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import github.exia1771.deploy.common.exception.ServiceException;
import github.exia1771.deploy.common.service.impl.BaseServiceImpl;
import github.exia1771.deploy.common.util.Commons;
import github.exia1771.deploy.common.util.Strings;
import github.exia1771.deploy.common.util.Users;
import github.exia1771.deploy.core.entity.ImageTemplate;
import github.exia1771.deploy.core.service.ImageTemplateService;
import org.omg.CORBA.OBJ_ADAPTER;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class ImageTemplateServiceImpl extends BaseServiceImpl<Long, ImageTemplate> implements ImageTemplateService {

    private final BaseMapper<ImageTemplate> mapper;
    private static final String TEMPLATE_NAME_COLUMN = "template_name";
    private static final String TEMPLATE_TAG_COLUMN = "template_tag";

    public ImageTemplateServiceImpl(BaseMapper<ImageTemplate> mapper) {
        super(mapper);
        this.mapper = mapper;
    }


    private void validEntity(ImageTemplate template) {
        if (template.getTemplateName() == null
                || template.getTemplateTag() == null
                || template.getDockerImageId() == null) {
            throw new ValidationException("模板名称，模板标签，镜像ID不能为NULL");
        }

        if (!Strings.lengthBetween(template.getTemplateName(), 1, 255) ||
                !Strings.lengthBetween(template.getTemplateTag(), 1, 255) ||
                !Strings.lengthBetween(template.getDockerImageId(), 1, 255)) {
            throw new ValidationException("字符长度必须在 1 ~ 255 之间");
        }


        Map<String, Object> params =  new HashMap<>();
        params.put(TEMPLATE_NAME_COLUMN, template.getTemplateName());
        params.put(TEMPLATE_TAG_COLUMN, template.getTemplateTag());

        if(isExisted(params)){
            throw new ValidationException("已经存在相同的模板名称与标签");
        }


    }

    @Override
    protected void beforeInsert(ImageTemplate template) {
        validEntity(template);

        Users.SimpleUser user = Users.getUser();
        template.setId(Commons.getId());
        template.setCreatorId(user.getUserId());
        template.setUpdaterId(user.getUserId());
    }

    @Override
    public Page<ImageTemplate> pageByName(ImageTemplate template) {
        if (template.getCurrent() == null || template.getSize() == null) {
            throw new ServiceException("当前页与页大小不能为NULL");
        }

        if (template.getCurrent() < 1) {
            template.setCurrent(1L);
        }

        if (template.getSize() < 1) {
            template.setSize(5L);
        }

        Page<ImageTemplate> page = new Page<>(template.getCurrent(), template.getSize());
        QueryWrapper<ImageTemplate> queryWrapper = new QueryWrapper<>();

        queryWrapper.like(TEMPLATE_NAME_COLUMN, template.getTemplateName());

        return mapper.selectPage(page, queryWrapper);
    }
}
