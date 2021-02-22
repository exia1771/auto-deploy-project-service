package github.exia1771.deploy.common.entity.dto;

import github.exia1771.deploy.common.entity.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserDTO extends AbstractDTO<String> {

    private RoleDTO role;
    private String username;
    private String avatarAddress;
    private String email;
    private String telephone;

}
