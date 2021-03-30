package github.exia1771.deploy.core.dto;

import github.exia1771.deploy.common.entity.dto.AbstractDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProjectContainerDTO extends AbstractDTO<String> {

	private String projectId;
	private String namespaceId;
	private String buildTypeId;
	//	private String jenkinsJobName;
//	private Integer jenkinsBuildNumber;
	private String dockerContainerId;
	private String gitTag;
	private String buildCommand;
	private String runCommand;
	private String cpToContainerPath;
	private String publisher;
	private LocalDateTime pubDate;
	private int status;

}
