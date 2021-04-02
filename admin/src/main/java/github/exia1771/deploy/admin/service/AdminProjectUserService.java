package github.exia1771.deploy.admin.service;

import github.exia1771.deploy.core.entity.ProjectUser;

import java.util.List;

public interface AdminProjectUserService {

	void batchUpdate(String projectId, List<String> userIdList);

	List<ProjectUser> findUsersByProjectId(String projectId);
}
