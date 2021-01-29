package github.exia1771.deploy.common.entity.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserDTO extends AbstractDTO<Long>{

    private Long roleId;
    private String username;
    private String email;
    private String telephone;
    private String token;

}
