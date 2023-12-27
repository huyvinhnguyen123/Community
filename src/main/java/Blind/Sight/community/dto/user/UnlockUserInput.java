package Blind.Sight.community.dto.user;

import Blind.Sight.community.dto.validate.password.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnlockUserInput {
    @NotNull(message = "{User.email.notNull}")
    @NotEmpty(message = "{User.email.notEmpty}")
    @Email
    private String email;

    @NotNull(message = "{User.password.notNull}")
    @NotEmpty(message = "{User.password.notEmpty}")
    @ValidPassword
    private String password;

    @NotNull(message = "{User.password.notNull}")
    @NotEmpty(message = "{User.password.notEmpty}")
    private String retypePassword;

    @NotNull(message = "The code cannot be null")
    @NotEmpty(message = "The code cannot be empty")
    private String unlockCode;
}
