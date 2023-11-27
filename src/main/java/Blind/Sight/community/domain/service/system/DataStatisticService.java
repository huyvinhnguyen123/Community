package Blind.Sight.community.domain.service.system;

import Blind.Sight.community.domain.repository.postgresql.UserRepositoryPg;
import Blind.Sight.community.domain.repository.postgresql.query.UserDataForStatistic;
import Blind.Sight.community.dto.statistic.UserDataStatistic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DataStatisticService {
    private final UserRepositoryPg userRepositoryPg;
    private static final String LOG_SUCCESS = "Statistic user success";

    public List<UserDataStatistic> statisticUsersToday() {
        List<UserDataForStatistic> userDataForStatistics = userRepositoryPg.statisticUsersToday();
        List<UserDataStatistic> userDataStatistics = new ArrayList<>();

        for(UserDataForStatistic userDataForStatistic: userDataForStatistics) {
            UserDataStatistic userDataStatistic = new UserDataStatistic();
            userDataStatistic.setName(userDataForStatistic.getUsername());
            userDataStatistic.setBirthDate(userDataForStatistic.getBirthDate());
            userDataStatistic.setEmail(userDataForStatistic.getEmail());

            userDataStatistics.add(userDataStatistic);
        }

        log.info(LOG_SUCCESS);
        return userDataStatistics;
    }

    public List<UserDataStatistic> statisticUsersThisWeek() {
        List<UserDataForStatistic> userDataForStatistics = userRepositoryPg.statisticUsersThisWeek();
        List<UserDataStatistic> userDataStatistics = new ArrayList<>();

        for(UserDataForStatistic userDataForStatistic: userDataForStatistics) {
            UserDataStatistic userDataStatistic = new UserDataStatistic();
            userDataStatistic.setName(userDataForStatistic.getUsername());
            userDataStatistic.setBirthDate(userDataForStatistic.getBirthDate());
            userDataStatistic.setEmail(userDataForStatistic.getEmail());

            userDataStatistics.add(userDataStatistic);
        }

        log.info(LOG_SUCCESS);
        return userDataStatistics;
    }

    public List<UserDataStatistic> statisticUsersThisMonth() {
        List<UserDataForStatistic> userDataForStatistics = userRepositoryPg.statisticUsersThisMonth();
        List<UserDataStatistic> userDataStatistics = new ArrayList<>();

        for(UserDataForStatistic userDataForStatistic: userDataForStatistics) {
            UserDataStatistic userDataStatistic = new UserDataStatistic();
            userDataStatistic.setName(userDataForStatistic.getUsername());
            userDataStatistic.setBirthDate(userDataForStatistic.getBirthDate());
            userDataStatistic.setEmail(userDataForStatistic.getEmail());

            userDataStatistics.add(userDataStatistic);
        }

        log.info(LOG_SUCCESS);
        return userDataStatistics;
    }

    public List<UserDataStatistic> statisticUsersThisYear() {
        List<UserDataForStatistic> userDataForStatistics = userRepositoryPg.statisticUsersThisYear();
        List<UserDataStatistic> userDataStatistics = new ArrayList<>();

        for(UserDataForStatistic userDataForStatistic: userDataForStatistics) {
            UserDataStatistic userDataStatistic = new UserDataStatistic();
            userDataStatistic.setName(userDataForStatistic.getUsername());
            userDataStatistic.setBirthDate(userDataForStatistic.getBirthDate());
            userDataStatistic.setEmail(userDataForStatistic.getEmail());

            userDataStatistics.add(userDataStatistic);
        }

        log.info(LOG_SUCCESS);
        return userDataStatistics;
    }
}
