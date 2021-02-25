package github.exia1771.deploy.core.service;

import github.exia1771.deploy.common.service.BaseService;
import github.exia1771.deploy.core.entity.ProjectUser;

import java.util.List;

public interface ProjectUserService extends BaseService<String, ProjectUser> {

    ProjectUser addProjectMember(ProjectUser projectUser);

    default ProjectUser addProjectMember(String projectId, String userId){
        return addProjectMember(new ProjectUser(){{
            setProjectId(projectId);
            setUserId(userId);
        }});
    }

    List<ProjectUser> batchAddProjectMember(List<ProjectUser> projectUsers);

    int removeProjectMember(ProjectUser projectUser);

    int batchRemoveProjectMember(List<ProjectUser> projectUsers);
}
