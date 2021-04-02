package github.exia1771.deploy.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import github.exia1771.deploy.common.entity.Role;
import github.exia1771.deploy.common.exception.ServiceException;
import github.exia1771.deploy.common.service.RoleService;
import github.exia1771.deploy.common.service.impl.BaseServiceImpl;
import github.exia1771.deploy.common.util.Commons;
import github.exia1771.deploy.common.util.StringUtil;
import github.exia1771.deploy.common.util.Users;
import github.exia1771.deploy.core.dto.ImageTemplateDTO;
import github.exia1771.deploy.core.entity.ImageTemplate;
import github.exia1771.deploy.core.mapper.ImageTemplateMapper;
import github.exia1771.deploy.core.service.ImageTemplateService;
import github.exia1771.deploy.core.service.docker.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ImageTemplateServiceImpl extends BaseServiceImpl<String, ImageTemplate> implements ImageTemplateService {

	@Autowired
	private ImageService imageService;

	@Autowired
	private RoleService roleService;

	private final ImageTemplateMapper mapper;
	private static final String TEMPLATE_NAME_COLUMN = "template_name";
	private static final String TEMPLATE_TAG_COLUMN = "template_tag";
	private static final String TEMPLATE_ID_COLUMN = "id";

	public ImageTemplateServiceImpl(ImageTemplateMapper imageTemplateMapper) {
		super(imageTemplateMapper);
		this.mapper = imageTemplateMapper;
	}


	private void validImageTemplate(ImageTemplate template) {

		if (template.getTemplateName() == null
				|| template.getTemplateTag() == null
				|| template.getDockerImageId() == null) {
			throw new ValidationException("模板名称，模板标签，镜像ID不能为NULL");
		}

		if (!StringUtil.isLengthBetween(template.getTemplateName(), 1, 255) ||
				!StringUtil.isLengthBetween(template.getTemplateTag(), 1, 255) ||
				!StringUtil.isLengthBetween(template.getDockerImageId(), 1, 255)) {
			throw new ValidationException("字符长度必须在 1 ~ 255 之间");
		}

		JSONObject inspect = imageService.inspect(template.getDockerImageId());
		if (inspect == null) {
			throw new ValidationException("不存在的镜像ID");
		}

		Map<String, Object> params = new HashMap<>();
		params.put(TEMPLATE_NAME_COLUMN, template.getTemplateName());
		params.put(TEMPLATE_TAG_COLUMN, template.getTemplateTag());

		if (isExisted(params)) {
			throw new ValidationException("已经存在相同的模板名称与标签");
		}

	}

	public Role getRole() {
		String roleId = Users.getSimpleUser().getRoleId();
		return roleService.findById(roleId);
	}

	@Override
	protected void beforeSave(ImageTemplate template) {
		validImageTemplate(template);
	}

	@Override
	protected void beforeInsert(ImageTemplate template) {
		Users.SimpleUser user = Users.getSimpleUser();
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

	@Override
	public List<String> findTemplateNameByKeyword(String keyword) {
		QueryWrapper<ImageTemplate> queryWrapper = new QueryWrapper<>();
		queryWrapper.groupBy(TEMPLATE_NAME_COLUMN);
		queryWrapper.like(TEMPLATE_NAME_COLUMN, keyword);
		return mapper.selectList(queryWrapper).stream().map(ImageTemplate::getTemplateName).collect(Collectors.toList());
	}

	@Override
	public List<String> findDistinctTemplateName() {
		QueryWrapper<ImageTemplate> queryWrapper = new QueryWrapper<>();
		queryWrapper.groupBy(TEMPLATE_NAME_COLUMN);
		List<ImageTemplate> imageTemplates = mapper.selectList(queryWrapper);
		return imageTemplates.stream().map(ImageTemplate::getTemplateName).collect(Collectors.toList());
	}

	@Override
	public List<ImageTemplateDTO> findTagsByTemplateName(String templateName) {
		QueryWrapper<ImageTemplate> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq(TEMPLATE_NAME_COLUMN, templateName);
		queryWrapper.select(TEMPLATE_ID_COLUMN, TEMPLATE_NAME_COLUMN, TEMPLATE_TAG_COLUMN);
		return mapper.selectList(queryWrapper).stream().map(ImageTemplate::toDTO).collect(Collectors.toList());
	}

	@Override
	public ImageTemplate findIdByTemplateNameAndTag(ImageTemplate template) {
		QueryWrapper<ImageTemplate> wrapper = new QueryWrapper<>();
		wrapper.eq(TEMPLATE_NAME_COLUMN, template.getTemplateName());
		wrapper.eq(TEMPLATE_TAG_COLUMN, template.getTemplateTag());
		return mapper.selectOne(wrapper);
	}

	@Override
	public List<ImageTemplate> findByName(String name) {
		QueryWrapper<ImageTemplate> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq(TEMPLATE_NAME_COLUMN, name);
		queryWrapper.select(TEMPLATE_ID_COLUMN, TEMPLATE_NAME_COLUMN, TEMPLATE_TAG_COLUMN);
		return mapper.selectList(queryWrapper);
	}
}
