package Blind.Sight.community.domain.repository.postgresql;

import Blind.Sight.community.domain.entity.temporary.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepositoryPg extends JpaRepository<Otp, Long> {
    @Query(value = """
            SELECT o.otpid, o.code, o.timecreated, o.timedeleted, o.timespecify, o.userid
            FROM Otps o
            LEFT JOIN Users u ON u.userId = o.userId
            WHERE u.email = :email
            OR u.oldLoginId = :email
            AND o.otpid = (SELECT MAX(otpid) FROM Otps);
            """, nativeQuery = true)
    Optional<Otp> findByEmail(@Param("email") String email);
}
