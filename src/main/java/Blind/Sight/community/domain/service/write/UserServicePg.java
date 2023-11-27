package Blind.Sight.community.domain.service.write;

import Blind.Sight.community.config.security.JwtUtil;
import Blind.Sight.community.config.security.PasswordEncrypt;
import Blind.Sight.community.domain.entity.Image;
import Blind.Sight.community.domain.entity.User;
import Blind.Sight.community.domain.entity.many.UserImage;
import Blind.Sight.community.domain.entity.temporary.Otp;
import Blind.Sight.community.domain.repository.postgresql.UserRepositoryPg;
import Blind.Sight.community.domain.service.custom.EmailDetailsService;
import Blind.Sight.community.domain.service.many.UserImageService;
import Blind.Sight.community.domain.service.system.OtpService;
import Blind.Sight.community.dto.email.EmailDetails;
import Blind.Sight.community.dto.user.LoginInput;
import Blind.Sight.community.dto.user.ResetPasswordInput;
import Blind.Sight.community.dto.user.UserDataInput;
import Blind.Sight.community.util.common.DeleteFlag;
import Blind.Sight.community.util.common.LockFlag;
import Blind.Sight.community.util.common.RoleData;
import Blind.Sight.community.util.format.CustomDateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServicePg {
    private final UserRepositoryPg userRepositoryPg;
    private final ImageServicePg imageServicePg;
    private final UserImageService userImageService;
    private final EmailDetailsService emailDetailsService;
    private final OtpService otpService;
    private static final Integer EXPIRATION_DURATION = 60;
    private static final String NOT_AVAILABLE = "Not available";
    @Value("${drive.folder.users}")
    private String userPath;

    private void sendMail(String email, String otp, Integer timeSpecify) {
        EmailDetails emailDetails= new EmailDetails();
        emailDetails.setRecipient(email);
        emailDetails.setSubject("Reset Password");
        emailDetailsService.sendSingleMailOTPWithTemplate(emailDetails, otp, timeSpecify);
    }

    public User findUserById(String userId) {
        User existUser = userRepositoryPg.findById(userId).orElseThrow(
                () -> {
                    log.error("Not found this user: {}", userId);
                    return new NullPointerException("Not found this user: " + userId);
                }
        );

        log.info("Found user");
        return existUser;
    }

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

    public void createUser(UserDataInput userDataInput) {
        User user = new User();
        user.setUserName(userDataInput.getName());
        user.setBirthDate(CustomDateTimeFormatter.dateOfBirthFormatter(userDataInput.getBirthDate()));
        user.setEmail(userDataInput.getEmail());
        user.setPassword(PasswordEncrypt.bcryptPassword(userDataInput.getPassword()));
        user.setRole(RoleData.USER.getRole());
        user.setLockFlag(LockFlag.NON_LOCK.getCode());
        user.setDeleteFlag(DeleteFlag.NON_DELETE.getCode());
        userRepositoryPg.save(user);
        log.info("Save new user success");

        log.info("Register new user success");
    }

    public void updateUserAndImage(Authentication authentication, String userId, UserDataInput userDataInput) throws GeneralSecurityException, IOException {
        User userLogin = new User();
        if (authentication != null && authentication.isAuthenticated()) {
            userLogin = (User) authentication.getPrincipal();
        }

        User existUser = findUserById(userId);

        // Must check user login and user update is current user(user login)
        if(userLogin.getUserId().equals(existUser.getUserId())) {
            existUser.setUserName(userDataInput.getName());
            existUser.setBirthDate(CustomDateTimeFormatter.dateOfBirthFormatter(userDataInput.getBirthDate()));
            userRepositoryPg.save(existUser);

            if(userDataInput.getFileId() != null && !userDataInput.getFileId().isEmpty()) {
                for(UserImage userImage: userImageService.findAll()) {
                    if(userImage.getUser().getUserId().equals(existUser.getUserId())) {
                        if(userImage.getImage().getImageId().equals(userDataInput.getFileId())) {
                            imageServicePg.updateReplaceImage(userDataInput.getFileId(), userDataInput.getFile(), userPath);
                        } else {
                            log.error("Not found this file id: " + userDataInput.getFileId());
                        }
                    }
                }
            } else {
                Image image = imageServicePg.createImage(userDataInput.getFile(), userPath, "user");
                userImageService.createUserImage(existUser, image);
            }
            log.info("Update user success");
        }
    }

    public void updateUserAndDeleteImage(Authentication authentication, String userId, String fileId) throws GeneralSecurityException, IOException {
        User userLogin = new User();
        if (authentication != null && authentication.isAuthenticated()) {
            userLogin = (User) authentication.getPrincipal();
        }

        User existUser = findUserById(userId);

        log.info(userLogin.getUserId());
        log.info(existUser.getUserId());

        // Must check user login and user update is current user(user login)
        if(userLogin.getUserId().equals(existUser.getUserId())) {
            for (UserImage userImage : userImageService.findAll()) {
                if (userImage.getUser().getUserId().equals(existUser.getUserId()) && userImage.getImage().getImageId().equals(fileId)) {
                    imageServicePg.deleteImage(userImage.getImage().getImageId());
                }
            }
        }
    }

    public void sendMail(String email) {
        User existUser = findUserByEmail(email);
        Otp otp = otpService.generateOtp(existUser.getEmail());
        String otpCode = otp.getCode();
        Integer timeSpecify = otp.getTimeSpecify();
        sendMail(existUser.getEmail(), otpCode, timeSpecify);
    }

    public void resetPassword(ResetPasswordInput resetPasswordInput) {
        User existUser = findUserByEmail(resetPasswordInput.getEmail());
        if(Boolean.TRUE.equals(otpService.validateOtp(existUser.getEmail(), resetPasswordInput.getOtpCode()))) {
            if(resetPasswordInput.getNewPassword().equals(resetPasswordInput.getRetypePassword())) {
                existUser.setPassword(PasswordEncrypt.bcryptPassword(resetPasswordInput.getNewPassword()));
                userRepositoryPg.save(existUser);

                log.info("Update new password success");
            } else {
                log.error("The password & retype password is not the same");
            }
        } else {
            log.error("The code has been expired");
        }
    }

    public void lockUser(Authentication authentication, String userId, String imageId) throws GeneralSecurityException, IOException {
        User userLogin = new User();
        if (authentication != null && authentication.isAuthenticated()) {
            userLogin = (User) authentication.getPrincipal();
        }

        User existUser = findUserById(userId);

        // Must check user login and user update is current user(user login)
        if(userLogin.equals(existUser)) {
            existUser.setOldLoginId(existUser.getEmail());
            existUser.setEmail(NOT_AVAILABLE);
            existUser.setPassword(NOT_AVAILABLE);
            existUser.setLockFlag(LockFlag.LOCKED.getCode());
            userRepositoryPg.save(existUser);

            for (UserImage userImage : userImageService.findAll()) {
                if (userImage.getUser().getUserId().equals(existUser.getUserId()) && userImage.getImage().getImageId().equals(imageId)) {
                    imageServicePg.deleteImage(imageId);
                }
            }
        }
    }

    public String login(LoginInput loginInput, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                loginInput.getEmail(),
                loginInput.getPassword()
        );

        Authentication login = authenticationManager.authenticate(authentication);

        // Check if the user is deleted or locked
        User user = (User) login.getPrincipal();
        String token = jwtUtil.createToken(user);

        log.info("Create token success: {}", token);
        return token;
    }
}
