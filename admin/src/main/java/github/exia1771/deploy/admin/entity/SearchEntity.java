package github.exia1771.deploy.admin.entity;

import github.exia1771.deploy.common.entity.AbstractSearchEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SearchEntity extends AbstractSearchEntity {

	private String searchText;

}
