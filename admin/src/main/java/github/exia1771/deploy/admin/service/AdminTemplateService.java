package github.exia1771.deploy.admin.service;

import github.exia1771.deploy.core.entity.ImageTemplate;

import java.util.List;

public interface AdminTemplateService extends SearchPagedService<ImageTemplate, String> {

	List<String> findName();

	List<ImageTemplate> findByName(String name);

	ImageTemplate findById(String id);
}
