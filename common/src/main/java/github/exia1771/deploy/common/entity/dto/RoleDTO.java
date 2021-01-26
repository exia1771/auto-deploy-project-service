package github.exia1771.deploy.common.entity.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class RoleDTO extends AbstractDTO<Long> {

    private String name;
    private Boolean createPri;
    private Boolean deletePri;
    private Boolean publishPri;
    private Boolean updatePri;

}
