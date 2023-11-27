package Blind.Sight.community.domain.repository.mysql.query;

import java.time.LocalDate;

public interface UserDataForSearching {
    String getUserId();
    String getUsername();
    LocalDate getBirthDate();
    String getEmail();
    String getImageId();
    String getImageName();
    String getImagePath();
}
