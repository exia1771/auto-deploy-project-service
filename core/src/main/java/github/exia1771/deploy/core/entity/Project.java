package github.exia1771.deploy.core.entity;

import github.exia1771.deploy.common.entity.AbstractEntity;
import github.exia1771.deploy.core.dto.ProjectDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

@Data
@EqualsAndHashCode(callSuper = true)
public class Project extends AbstractEntity<String> {

    private String username;
    private String templateId;
    private String name;
    private String description;
    private String identification;
    private String gitUrl;
    private String fileLocation;
    private int state;

    @Override
    public ProjectDTO toDTO() {
        ProjectDTO projectDTO = new ProjectDTO();
        BeanUtils.copyProperties(this,projectDTO);
        return projectDTO;
    }
}
