package github.exia1771.deploy.core.service.impl;

import github.exia1771.deploy.common.entity.Role;
import github.exia1771.deploy.common.entity.User;
import github.exia1771.deploy.common.service.RoleService;
import github.exia1771.deploy.common.service.UserService;
import github.exia1771.deploy.common.service.impl.BaseServiceImpl;
import github.exia1771.deploy.common.util.Commons;
import github.exia1771.deploy.common.util.Users;
import github.exia1771.deploy.core.entity.Project;
import github.exia1771.deploy.core.mapper.ProjectMapper;
import github.exia1771.deploy.core.service.ProjectService;
import github.exia1771.deploy.core.service.ProjectUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;

@Service
@Transactional
public class ProjectServiceImpl extends BaseServiceImpl<String, Project> implements ProjectService {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ProjectUserService projectUserService;

    private final ProjectMapper mapper;
    private User master;

    public ProjectServiceImpl(ProjectMapper mapper) {
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
    protected void beforeSave(Project project) {
        master = userService.findByName(project.getUsername());
        if (master == null || master.getUsername() == null) {
            throw new ValidationException("用户名不存在，请输入正确的用户名");
        }

    }

    @Override
    protected void beforeInsert(Project project) {
        project.setId(Commons.getId());
        projectUserService.addProjectMember(project.getId(), master.getId());
    }


}
