package Blind.Sight.community.domain.repository.postgresql;

import Blind.Sight.community.domain.entity.User;
import Blind.Sight.community.domain.repository.postgresql.query.UserDataForStatistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepositoryPg extends JpaRepository<User, String> {
    Optional<User> findUserByEmail(String email);
    Optional<User> findUserByOldLoginId(String email);

    @Query(value = """
            SELECT u.username, u.birthdate, u.email
            FROM users u
            WHERE TO_CHAR(u.createat, 'YYYY-MM-DD') = TO_CHAR(NOW(), 'YYYY-MM-DD');
            """, nativeQuery = true)
    List<UserDataForStatistic> statisticUsersToday();

    @Query(value = """
            SELECT u.username, u.birthdate, u.email
            FROM users u
            WHERE EXTRACT(WEEK FROM u.createat) = EXTRACT(WEEK FROM CURRENT_DATE)
            """, nativeQuery = true)
    List<UserDataForStatistic> statisticUsersThisWeek();

    @Query(value = """
            SELECT u.username, u.birthdate, u.email
            FROM users u
            WHERE EXTRACT(MONTH FROM u.createat) = EXTRACT(MONTH FROM CURRENT_DATE)
            """, nativeQuery = true)
    List<UserDataForStatistic> statisticUsersThisMonth();

    @Query(value = """
            SELECT u.username, u.birthdate, u.email
            FROM users u
            WHERE EXTRACT(YEAR FROM u.createat) = EXTRACT(YEAR FROM CURRENT_DATE)
            """, nativeQuery = true)
    List<UserDataForStatistic> statisticUsersThisYear();
}
