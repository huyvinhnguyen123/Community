package Blind.Sight.community.domain.repository.postgresql.query;

import java.time.LocalDate;

public interface UserDataForStatistic {
    String getUsername();
    LocalDate getBirthDate();
    String getEmail();
}
