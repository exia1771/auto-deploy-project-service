package github.exia1771.deploy.common.entity;

import github.exia1771.deploy.common.util.Pageable;
import lombok.Data;

@Data
public abstract class AbstractSearchEntity implements Pageable {

	private Long current;
	private Long size;

}
