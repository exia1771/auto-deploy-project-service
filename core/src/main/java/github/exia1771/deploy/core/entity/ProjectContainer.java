package github.exia1771.deploy.core.entity;

import github.exia1771.deploy.common.entity.AbstractEntity;
import github.exia1771.deploy.common.util.Pageable;
import github.exia1771.deploy.core.dto.ProjectContainerDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProjectContainer extends AbstractEntity<String> implements Pageable {

	private String projectId;
	private String namespaceId;
	private String buildTypeId;
	private String jenkinsJobName;
	private Integer jenkinsBuildNumber = 1;
	private String dockerContainerId;
	private String gitTag;
	private String buildCommand;
	private String runCommand;
	// 默认根路径部署
	private String cpToContainerPath;
	private String publisher;
	private LocalDateTime pubDate;
	private int status;

	private transient Long current;
	private transient Long size;

	@Override
	public ProjectContainerDTO toDTO() {
		ProjectContainerDTO dto = new ProjectContainerDTO();
		BeanUtils.copyProperties(this, dto);
		return dto;
	}

	@Getter
	public enum Status {
		SUCCESS(0),
		FAILURE(1),
		WAIT(2);
		private final int value;

		Status(int status) {
			this.value = status;
		}
	}
}
