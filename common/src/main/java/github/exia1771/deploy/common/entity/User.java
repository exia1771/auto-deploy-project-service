package github.exia1771.deploy.common.entity;

import github.exia1771.deploy.common.entity.dto.UserDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

@Data
@EqualsAndHashCode(callSuper = false)
public class User extends AbstractEntity<Long> {

    private Long roleId;
    private String username;
    private String password;
    private String email;
    private String telephone;


    public User() {
    }

    @Override
    public UserDTO toDTO() {
        UserDTO target = new UserDTO();
        BeanUtils.copyProperties(this, target);
        return target;
    }
}
