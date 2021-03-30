package github.exia1771.deploy.core.entity;

import github.exia1771.deploy.common.entity.AbstractEntity;
import github.exia1771.deploy.core.dto.ProjectConfigDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProjectConfig extends AbstractEntity<String> {

	private String namespaceId;
	private String projectId;
	private long memory = 1024;
	private long core = 1;
	private String port;


	@Override
	public ProjectConfigDTO toDTO() {
		ProjectConfigDTO projectConfigDTO = new ProjectConfigDTO();
		BeanUtils.copyProperties(this, projectConfigDTO);
		return projectConfigDTO;
	}
}
