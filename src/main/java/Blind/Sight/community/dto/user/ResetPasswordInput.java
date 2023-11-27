package Blind.Sight.community.dto.user;

import Blind.Sight.community.dto.validate.password.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordInput {
    @NotNull(message = "{User.email.notNull}")
    @NotEmpty(message = "{User.email.notEmpty}")
    @Email
    private String email;
    @NotNull(message = "{User.password.notNull}")
    @NotEmpty(message = "{User.password.notEmpty}")
    @ValidPassword
    private String oldPassword;
    @NotNull(message = "{User.password.notNull}")
    @NotEmpty(message = "{User.password.notEmpty}")
    @ValidPassword
    private String newPassword;
    @NotNull(message = "{User.password.notNull}")
    @NotEmpty(message = "{User.password.notEmpty}")
    @ValidPassword
    private String retypePassword;
    @NotNull(message = "You must input otp to reset password")
    @NotEmpty(message = "You must input otp to reset password")
    private String otpCode;
}
