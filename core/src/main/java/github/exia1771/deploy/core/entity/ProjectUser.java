package github.exia1771.deploy.core.entity;

import github.exia1771.deploy.common.entity.AbstractEntity;
import github.exia1771.deploy.core.dto.ProjectUserDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProjectUser extends AbstractEntity<String> {

    private String projectId;
    private String userId;

    @Override
    public ProjectUserDTO toDTO() {
        ProjectUserDTO projectUserDTO = new ProjectUserDTO();
        BeanUtils.copyProperties(this, projectUserDTO);
        return projectUserDTO;
    }
}
