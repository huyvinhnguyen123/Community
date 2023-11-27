package Blind.Sight.community.domain.service.system;

import Blind.Sight.community.domain.entity.User;
import Blind.Sight.community.domain.entity.temporary.Otp;
import Blind.Sight.community.domain.repository.postgresql.OtpRepositoryPg;
import Blind.Sight.community.domain.repository.postgresql.UserRepositoryPg;
import Blind.Sight.community.util.random.RandomString;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OtpService {
    private final UserRepositoryPg userRepositoryPg;
    private final OtpRepositoryPg otpRepositoryPg;
    private static final int OTP_LENGTH = 6; // Adjust as needed

    private User findUserByEmail(String email) {
        User existUser = userRepositoryPg.findUserByEmail(email).orElseThrow(
                () -> {
                    log.error("Not found this user: {}", email);
                    return new NullPointerException("Not found this user: " + email);
                }
        );

        log.info("Found user");
        return existUser;
    }

    public Otp generateOtp(String email) {
        User existUser = findUserByEmail(email);

        Otp otp = new Otp();
        otp.setCode(RandomString.generateRandomString(OTP_LENGTH));
        otp.setUser(existUser);
        otp.setTimeSpecify(otp.getTimeDeleted().getMinute() - otp.getTimeCreated().getMinute());

        otpRepositoryPg.save(otp);
        log.info("Create otp success");

        return otp;
    }

    public boolean validateOtp(String email, String otp) {
        Optional<Otp> otpOptional = otpRepositoryPg.findByEmail(email);

        if (otpOptional.isPresent()) {
            Otp otpEntity = otpOptional.get();
            return otpEntity.getCode().equals(otp) && LocalTime.now().isBefore(otpEntity.getTimeDeleted());
        }

        return false;
    }
}
