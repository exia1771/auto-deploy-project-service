package github.exia1771.deploy.common.entity;

import github.exia1771.deploy.common.entity.dto.UserDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@EqualsAndHashCode(callSuper = false)
public class User extends AbstractEntity<String> {

    @NotNull
    @NotBlank
    @Size(min = 6, max = 255)
    private String username;

    private String roleId;

    @NotNull
    @NotBlank
    @Size(min = 6, max = 255)
    private String password;

    private String avatarAddress;

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
