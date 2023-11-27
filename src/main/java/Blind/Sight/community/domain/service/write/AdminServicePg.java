package Blind.Sight.community.domain.service.write;

import Blind.Sight.community.config.security.PasswordEncrypt;
import Blind.Sight.community.domain.entity.Image;
import Blind.Sight.community.domain.entity.User;
import Blind.Sight.community.domain.entity.many.UserImage;
import Blind.Sight.community.domain.repository.postgresql.UserImageRepositoryPg;
import Blind.Sight.community.domain.repository.postgresql.UserRepositoryPg;
import Blind.Sight.community.domain.service.many.UserImageService;
import Blind.Sight.community.dto.user.UserDataInput;
import Blind.Sight.community.util.common.BanLevel;
import Blind.Sight.community.util.common.DeleteFlag;
import Blind.Sight.community.util.common.LockFlag;
import Blind.Sight.community.util.common.RoleData;
import Blind.Sight.community.util.format.CustomDateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminServicePg {
    private final UserRepositoryPg userRepositoryPg;
    private final UserServicePg userServicePg;
    private final ImageServicePg imageServicePg;
    private final UserImageService userImageService;
    private static final String NOT_AVAILABLE = "Not available";
    @Value("${drive.folder.users}")
    private String userPath;


    public void createSystem(UserDataInput userDataInput) {
        User user = new User();
        user.setUserName(userDataInput.getName());
        user.setBirthDate(CustomDateTimeFormatter.dateOfBirthFormatter(userDataInput.getBirthDate()));
        user.setEmail(userDataInput.getEmail());
        user.setPassword(PasswordEncrypt.bcryptPassword(userDataInput.getPassword()));
        user.setRole(RoleData.SYSTEM.getRole());
        user.setLockFlag(LockFlag.NON_LOCK.getCode());
        user.setDeleteFlag(DeleteFlag.NON_DELETE.getCode());
        userRepositoryPg.save(user);
        log.info("Save new admin success");

        log.info("Register new admin success");
    }

    public void createAdmin(UserDataInput userDataInput) {
        User user = new User();
        user.setUserName(userDataInput.getName());
        user.setBirthDate(CustomDateTimeFormatter.dateOfBirthFormatter(userDataInput.getBirthDate()));
        user.setEmail(userDataInput.getEmail());
        user.setPassword(PasswordEncrypt.bcryptPassword(userDataInput.getPassword()));
        user.setRole(RoleData.ADMIN.getRole());
        user.setLockFlag(LockFlag.NON_LOCK.getCode());
        user.setDeleteFlag(DeleteFlag.NON_DELETE.getCode());
        userRepositoryPg.save(user);
        log.info("Save new admin success");

        log.info("Register new admin success");
    }

    public void updateUser(String userId, UserDataInput userDataInput) throws GeneralSecurityException, IOException {
        User existUser = userServicePg.findUserById(userId);
        existUser.setUserName(userDataInput.getName());
        existUser.setEmail(userDataInput.getEmail());
        existUser.setBirthDate(CustomDateTimeFormatter.dateOfBirthFormatter(userDataInput.getBirthDate()));
        userRepositoryPg.save(existUser);

        if(userDataInput.getFileId() != null && !userDataInput.getFileId().isEmpty()) {
            for(UserImage userImage: userImageService.findAll()) {
                if(userImage.getImage().getImageId().equals(userDataInput.getFileId())) {
                    imageServicePg.updateReplaceImage(userDataInput.getFileId(), userDataInput.getFile(), userPath);
                } else {
                    log.error("Not found this file id: " + userDataInput.getFileId());
                }
            }
        } else {
            Image image = imageServicePg.createImage(userDataInput.getFile(), userPath, "user");
            userImageService.createUserImage(existUser, image);
        }
    }

    public void deleteUserImage(String userId, String fileId) throws GeneralSecurityException, IOException {
        User existUser = userServicePg.findUserById(userId);
        Image existImage = imageServicePg.findImageById(fileId);

        for(UserImage userImage: userImageService.findAll()) {
            if(userImage.getUser().equals(existUser) && userImage.getImage().equals(existImage)) {
                imageServicePg.deleteImage(fileId);
                userImageService.deleteUserImage(userImage.getUserImageId());
            }
        }

        log.info("Delete user's image success");
    }

    public void banUser(String userId) {
        User existUser = userServicePg.findUserById(userId);
        existUser.setOldLoginId(existUser.getEmail());
        existUser.setEmail(NOT_AVAILABLE);
        existUser.setDeleteFlag(BanLevel.TROUBLE.getCode());
        existUser.setLockFlag(LockFlag.LOCKED.getCode());
        userRepositoryPg.save(existUser);
        log.info("Update user success");

        log.info("User has been banned");
    }

    public void deleteUserForever(String userId) {
        User existUser = userServicePg.findUserById(userId);
        userRepositoryPg.delete(existUser);

        log.info("Delete user success");
    }

}
