package github.exia1771.deploy.core.service.impl;

import github.exia1771.deploy.common.entity.Role;
import github.exia1771.deploy.common.entity.User;
import github.exia1771.deploy.common.service.RoleService;
import github.exia1771.deploy.common.service.UserService;
import github.exia1771.deploy.common.service.impl.BaseServiceImpl;
import github.exia1771.deploy.common.util.Commons;
import github.exia1771.deploy.common.util.Users;
import github.exia1771.deploy.common.util.Validators;
import github.exia1771.deploy.core.dto.ProjectDTO;
import github.exia1771.deploy.core.entity.ImageTemplate;
import github.exia1771.deploy.core.entity.Project;
import github.exia1771.deploy.core.entity.ProjectUser;
import github.exia1771.deploy.core.mapper.ProjectMapper;
import github.exia1771.deploy.core.service.ImageTemplateService;
import github.exia1771.deploy.core.service.ProjectService;
import github.exia1771.deploy.core.service.ProjectUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl extends BaseServiceImpl<String, Project> implements ProjectService {

    private User master;
    private final ProjectMapper mapper;

    @Autowired
    private UserService userService;
    @Autowired
    private ImageTemplateService imageTemplateService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private ProjectUserService projectUserService;

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
        Validators.requireLength("工程名称", project.getName(), 1, 255);
        Validators.requireLength("工程标识", project.getIdentification(), 1, 255);
        Validators.requireMatchRegex("工程标识", project.getIdentification(), "^[\\w-]+$");
        Validators.requireLength("Git地址", project.getGitUrl(), 1, 255);
        Validators.requireMatchRegex("Git地址", project.getGitUrl(), "^(http|https):\\/\\/.*?\\/.*?.git$");
        Validators.requireLength("所属用户名", project.getUsername(), 1, 255);
        Validators.requireLength("相关描述", project.getDescription(), 0, 255);

        master = userService.findByName(project.getUsername());
        if (master == null || master.getUsername() == null) {
            throw new ValidationException("用户名不存在，请输入正确的用户名");
        }

        ImageTemplate imageTemplate = imageTemplateService.findById(project.getTemplateId());
        if (imageTemplate == null) {
            throw new ValidationException("不存在的模板类型，请重新选择");
        }
    }

    @Override
    protected void beforeInsert(Project project) {
        project.setId(Commons.getId());
        projectUserService.addProjectMember(project.getId(), master.getId());
    }

    @Override
    public List<ProjectDTO> findProjectsByCurrentUser(Project project) {
        return findProjectsByUserId(getCurrentUser().getUserId()).stream().map(Project::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<Project> findProjectsByUserId(String userId, long current, long size) {
        List<String> projectIdList = projectUserService.findByUserId(userId).stream().map(ProjectUser::getProjectId).collect(Collectors.toList());
        return mapper.findListById(projectIdList, current, size);
    }
}
