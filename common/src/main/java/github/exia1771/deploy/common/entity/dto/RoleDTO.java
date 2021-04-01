package github.exia1771.deploy.common.entity.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RoleDTO extends AbstractDTO<String> {

	private String name;
	private Boolean createPri;
	private Boolean deletePri;
	private Boolean publishPri;
	private Boolean updatePri;

}
