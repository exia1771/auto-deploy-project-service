package github.exia1771.deploy.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import github.exia1771.deploy.common.service.BaseService;
import github.exia1771.deploy.core.Pageable;
import github.exia1771.deploy.core.dto.ProjectDTO;
import github.exia1771.deploy.core.entity.Project;


public interface ProjectService extends BaseService<String, Project> {

    long DEFAULT_CURRENT = 0L;
    long DEFAULT_SIZE = 10L;

    IPage<ProjectDTO> findProjectsByCurrentUser(Pageable pageable);

    default IPage<ProjectDTO> findProjectsByCurrentUser() {
        return findProjectsByCurrentUser(new Project() {{
            setCurrent(DEFAULT_CURRENT);
            setSize(DEFAULT_SIZE);
        }});
    }

    IPage<Project> findProjectsByUserId(String userId, long current, long size);

    IPage<ProjectDTO> findPagedProjectsByKeyword(String keyword, Pageable pageable);
}
