package Blind.Sight.community.dto.user;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserDataForUser {
    private String id;
    private String name;
    private LocalDate birthDate;
    private String email;
    private String imageId;
    private String imageName;
    private String imagePath;
}
