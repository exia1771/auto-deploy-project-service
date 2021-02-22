package github.exia1771.deploy.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import github.exia1771.deploy.common.entity.Role;
import github.exia1771.deploy.common.exception.ServiceException;
import github.exia1771.deploy.common.service.RoleService;
import github.exia1771.deploy.common.service.impl.BaseServiceImpl;
import github.exia1771.deploy.common.util.Commons;
import github.exia1771.deploy.common.util.Strings;
import github.exia1771.deploy.common.util.Users;
import github.exia1771.deploy.core.entity.ImageTemplate;
import github.exia1771.deploy.core.mapper.ImageTemplateMapper;
import github.exia1771.deploy.core.service.ImageService;
import github.exia1771.deploy.core.service.ImageTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class ImageTemplateServiceImpl extends BaseServiceImpl<String, ImageTemplate> implements ImageTemplateService {

    @Autowired
    private ImageService imageService;

    @Autowired
    private RoleService roleService;

    private final ImageTemplateMapper mapper;
    private static final String TEMPLATE_NAME_COLUMN = "template_name";
    private static final String TEMPLATE_TAG_COLUMN = "template_tag";

    public ImageTemplateServiceImpl(BaseMapper<ImageTemplate> mapper, ImageTemplateMapper imageTemplateMapper) {
        super(mapper);
        this.mapper = imageTemplateMapper;
    }


    private void validImageTemplate(ImageTemplate template) {
        Role role = getRole();
        if(role.getCreatePri() == null || !role.getCreatePri()){
            throw new ValidationException("该账号缺少当前模块的创建权限");
        }

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

        JSONObject inspect = imageService.inspect(template.getDockerImageId());
        if(inspect == null){
            throw new ValidationException("不存在的镜像ID");
        }

        Map<String, Object> params =  new HashMap<>();
        params.put(TEMPLATE_NAME_COLUMN, template.getTemplateName());
        params.put(TEMPLATE_TAG_COLUMN, template.getTemplateTag());

        if(isExisted(params)){
            throw new ValidationException("已经存在相同的模板名称与标签");
        }

    }

    private Role getRole() {
        String roleId = Users.getSimpleUser().getRoleId();
        return roleService.findById(roleId);
    }

    @Override
    protected void beforeInsert(ImageTemplate template) {
        validImageTemplate(template);

        Users.SimpleUser user = Users.getSimpleUser();
        template.setId(Commons.getId());
        template.setCreatorId(user.getUserId());
        template.setUpdaterId(user.getUserId());
    }

    @Override
    protected void beforeUpdate(ImageTemplate template) {
        if(getRole().getUpdatePri() != null || !getRole().getUpdatePri()){
            throw new ValidationException("该账号缺少当前模块的修改权限");
        }
    }

    @Override
    protected void beforeDelete(String id) {
        if(getRole().getDeletePri() != null || !getRole().getDeletePri()){
            throw new ValidationException("该账号缺少当前模块的删除权限");
        }
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
