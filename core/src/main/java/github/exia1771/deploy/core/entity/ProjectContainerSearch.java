package github.exia1771.deploy.core.entity;

import github.exia1771.deploy.common.entity.AbstractSearchEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class ProjectContainerSearch extends AbstractSearchEntity {

	private String publisher;
	private String gitTag;
	private String pubStartDate;
	private String pubEndDate;
	private String namespaceId;
	private String projectId;

}
