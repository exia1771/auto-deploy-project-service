package github.exia1771.deploy.core.service;

import github.exia1771.deploy.common.service.BaseService;
import github.exia1771.deploy.core.entity.ProjectUser;

import java.util.List;

public interface ProjectUserService extends BaseService<String, ProjectUser> {

	List<ProjectUser> findByUserId(String userId);

	List<ProjectUser> findByProjectId(String projectId);

	int addProjectMember(ProjectUser projectUser);

	default int addProjectMember(String projectId, String userId) {
		return addProjectMember(new ProjectUser() {{
			setProjectId(projectId);
			setUserId(userId);
		}});
	}

	int batchAddProjectMember(List<ProjectUser> projectUsers);

	int removeProjectMember(ProjectUser projectUser);

	int batchRemoveProjectMember(List<ProjectUser> projectUsers);

	int batchUpdate(String projectId, List<String> userId);

	void deleteByProjectId(String projectId);
}
