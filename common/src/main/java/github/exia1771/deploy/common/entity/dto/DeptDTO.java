package github.exia1771.deploy.common.entity.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DeptDTO extends AbstractDTO<String> {

	private String name;

}
