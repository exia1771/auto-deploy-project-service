package github.exia1771.deploy.common.entity;

import github.exia1771.deploy.common.entity.dto.RoleDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

@Data
@EqualsAndHashCode(callSuper = true)
public class Role extends AbstractEntity<String> {

    private String name;
    private Boolean createPri;
    private Boolean deletePri;
    private Boolean publishPri;
    private Boolean updatePri;

    public Role() {
    }

    @Override
    public RoleDTO toDTO() {
        RoleDTO target = new RoleDTO();
        BeanUtils.copyProperties(this, target);
        return target;
    }

}
