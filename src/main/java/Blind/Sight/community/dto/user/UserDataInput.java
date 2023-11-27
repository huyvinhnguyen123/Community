package Blind.Sight.community.dto.user;

import Blind.Sight.community.dto.validate.birthdate.ValidBirthDate;
import Blind.Sight.community.dto.validate.password.ValidPassword;
import Blind.Sight.community.dto.validate.username.ValidUsername;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UserDataInput {
    @NotNull(message = "{User.birthDay.notNull}")
    @NotEmpty(message = "{User.birthDay.notEmpty}")
    @ValidBirthDate
    private String birthDate;
    @ValidUsername
    private String name;
    @NotNull(message = "{User.loginId.notNull}")
    @NotEmpty(message = "{User.loginId.notEmpty}")
    @Email
    private String email;
    @NotNull(message = "{User.password.notNull}")
    @NotEmpty(message = "{User.password.notEmpty}")
    @ValidPassword
    private String password;
    private MultipartFile file;
    private String fileId;
}
