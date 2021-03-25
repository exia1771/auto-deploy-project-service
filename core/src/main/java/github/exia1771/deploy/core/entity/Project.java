package github.exia1771.deploy.core.entity;

import github.exia1771.deploy.common.entity.AbstractEntity;
import github.exia1771.deploy.core.Pageable;
import github.exia1771.deploy.core.dto.ProjectDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

@Data
@EqualsAndHashCode(callSuper = true)
public class Project extends AbstractEntity<String> implements Pageable {

    private String username;
    private String templateId;
	private String name;
    private String description;
    private String identification;
    private String gitUrl;
    private int state;

    private transient Long current;
    private transient Long size;

    @Override
    public ProjectDTO toDTO() {
        ProjectDTO projectDTO = new ProjectDTO();
        BeanUtils.copyProperties(this, projectDTO);
        return projectDTO;
    }
}
