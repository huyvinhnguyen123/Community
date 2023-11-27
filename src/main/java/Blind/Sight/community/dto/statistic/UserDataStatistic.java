package Blind.Sight.community.dto.statistic;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserDataStatistic {
    private String name;
    private LocalDate birthDate;
    private String email;
}
