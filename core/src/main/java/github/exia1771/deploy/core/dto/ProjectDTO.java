package github.exia1771.deploy.core.dto;

import github.exia1771.deploy.common.entity.dto.AbstractDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProjectDTO extends AbstractDTO<String> {

	private String username;
	private String templateId;
	private String name;
	private String description;
	private String identification;
	private String gitUrl;

}
