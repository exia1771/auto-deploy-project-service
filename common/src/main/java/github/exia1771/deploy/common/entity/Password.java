package github.exia1771.deploy.common.entity;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class Password {

    @NotNull
    @Size(min = 6, max = 255)
    private String oldPassword;

    @NotNull
    @Size(min = 6, max = 255)
    private String newPassword;

    @NotNull
    @Size(min = 6, max = 255)
    private String confirmedPassword;

}
