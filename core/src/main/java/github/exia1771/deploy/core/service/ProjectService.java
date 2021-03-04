package github.exia1771.deploy.core.service;

import github.exia1771.deploy.common.service.BaseService;
import github.exia1771.deploy.core.entity.Project;

import java.util.List;

public interface ProjectService extends BaseService<String, Project> {

    List<Project> findByCurrentUser();

    List<Project> findByUserId(String userId);
}
