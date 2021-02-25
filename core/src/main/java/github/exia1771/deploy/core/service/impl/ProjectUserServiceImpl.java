package github.exia1771.deploy.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import github.exia1771.deploy.common.entity.Role;
import github.exia1771.deploy.common.service.RoleService;
import github.exia1771.deploy.common.service.impl.BaseServiceImpl;
import github.exia1771.deploy.common.util.Users;
import github.exia1771.deploy.core.entity.ProjectUser;
import github.exia1771.deploy.core.mapper.ProjectUserMapper;
import github.exia1771.deploy.core.service.ProjectUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ProjectUserServiceImpl extends BaseServiceImpl<String, ProjectUser> implements ProjectUserService {

    @Autowired
    private RoleService roleService;

    private final ProjectUserMapper mapper;

    private final static String PROJECT_ID_COLUMN = "project_id";
    private final static String USER_ID_COLUMN = "user_id";

    public ProjectUserServiceImpl(ProjectUserMapper mapper) {
        super(mapper);
        this.mapper = mapper;
    }

    @Override
    public Users.SimpleUser getCurrentUser() {
        return Users.getSimpleUser();
    }

    @Override
    public Role getRole() {
        return roleService.findById(getCurrentUser().getRoleId());
    }

    @Override
    public ProjectUser addProjectMember(ProjectUser projectUser) {
        return save(projectUser);
    }

    @Override
    public List<ProjectUser> batchAddProjectMember(List<ProjectUser> projectUsers) {
        List<ProjectUser> list = new ArrayList<>();
        projectUsers.stream().unordered().forEach(projectUser -> list.add(addProjectMember(projectUser)));
        return list;
    }

    @Override
    public int removeProjectMember(ProjectUser projectUser) {
        QueryWrapper<ProjectUser> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq(PROJECT_ID_COLUMN, projectUser.getProjectId());
        queryWrapper.eq(USER_ID_COLUMN, projectUser.getUserId());

        return mapper.delete(queryWrapper);
    }

    @Override
    public int batchRemoveProjectMember(List<ProjectUser> projectUsers) {
        int count = 0;

        for (ProjectUser projectUser : projectUsers) {
            count += removeProjectMember(projectUser);
        }

        return count;
    }
}
