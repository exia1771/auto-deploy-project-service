package github.exia1771.deploy.core.entity;

import github.exia1771.deploy.common.entity.AbstractEntity;
import github.exia1771.deploy.core.dto.ProjectConfigDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProjectConfig extends AbstractEntity<String> {

    private String namespace;
    private String group;
    private String config;

    @Override
    public ProjectConfigDTO toDTO() {
        ProjectConfigDTO projectConfigDTO = new ProjectConfigDTO();
        BeanUtils.copyProperties(this, projectConfigDTO);
        return projectConfigDTO;
    }
}
