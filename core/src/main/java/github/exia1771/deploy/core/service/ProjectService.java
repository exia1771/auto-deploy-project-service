package github.exia1771.deploy.core.service;

import github.exia1771.deploy.common.service.BaseService;
import github.exia1771.deploy.core.dto.ProjectDTO;
import github.exia1771.deploy.core.entity.Project;

import java.util.List;

public interface ProjectService extends BaseService<String, Project> {

    long DEFAULT_CURRENT = 0L;
    long DEFAULT_SIZE = 10L;

    List<ProjectDTO> findProjectsByCurrentUser(Project project);

    default List<ProjectDTO> findProjectsByCurrentUser() {
        return findProjectsByCurrentUser(new Project() {{
            setCurrent(DEFAULT_CURRENT);
            setSize(DEFAULT_SIZE);
        }});
    }

    default List<Project> findProjectsByUserId(String userId) {
        return findProjectsByUserId(userId, DEFAULT_CURRENT, DEFAULT_SIZE);
    }


    List<Project> findProjectsByUserId(String userId, long current, long size);

}
