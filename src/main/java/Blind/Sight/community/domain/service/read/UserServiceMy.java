package Blind.Sight.community.domain.service.read;

import Blind.Sight.community.domain.entity.Image;
import Blind.Sight.community.domain.entity.Type;
import Blind.Sight.community.domain.entity.User;
import Blind.Sight.community.domain.entity.many.UserImage;
import Blind.Sight.community.domain.repository.mysql.ImageRepositoryMy;
import Blind.Sight.community.domain.repository.mysql.TypeRepositoryMy;
import Blind.Sight.community.domain.repository.mysql.UserImageRepositoryMy;
import Blind.Sight.community.domain.repository.mysql.UserRepositoryMy;
import Blind.Sight.community.domain.repository.mysql.query.UserDataForSearching;
import Blind.Sight.community.domain.repository.postgresql.ImageRepositoryPg;
import Blind.Sight.community.domain.repository.postgresql.UserImageRepositoryPg;
import Blind.Sight.community.domain.repository.postgresql.UserRepositoryPg;
import Blind.Sight.community.domain.service.system.DataSynchronizationService;
import Blind.Sight.community.dto.user.UserData;
import Blind.Sight.community.dto.user.UserDataForUser;
import Blind.Sight.community.dto.user.UserSearchInput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceMy {
    private final UserRepositoryPg userRepositoryPg;
    private final ImageRepositoryPg imageRepositoryPg;
    private final UserImageRepositoryPg userImageRepositoryPg;
    private final UserRepositoryMy userRepositoryMy;
    private final ImageRepositoryMy imageRepositoryMy;
    private final UserImageRepositoryMy userImageRepositoryMy;
    private final TypeRepositoryMy typeRepositoryMy;
    private final DataSynchronizationService dataSynchronizationService;

    public void synchronizeAllData() {
        List<User> pgUsers = userRepositoryPg.findAll();
        List<User> myUsers = userRepositoryMy.findAll();
        List<Image> pgImages = imageRepositoryPg.findAll();
        List<Image> myImages = imageRepositoryMy.findAll();
        List<UserImage> pgUserImages = userImageRepositoryPg.findAll();
        List<UserImage> myUserImages = userImageRepositoryMy.findAll();
        List<Type> myTypes = typeRepositoryMy.findAll();

        // Data is Empty
        if(myUsers.isEmpty()) {
            dataSynchronizationService.synchronizeMySQLUserData();
        }

        if(myImages.isEmpty()) {
            dataSynchronizationService.synchronizeMySQLImageData();
        }

        if(myUserImages.isEmpty()) {
            dataSynchronizationService.synchronizeMySQLUserImageData();
        }

        if(myTypes.isEmpty()) {
            dataSynchronizationService.synchronizeMySQLTypeData();
        }

        // Data hasn't been synchronized
        if(!myUsers.equals(pgUsers)) {
            dataSynchronizationService.synchronizeMySQLUserDataUpdate();
        }

        if(!myImages.equals(pgImages)) {
            dataSynchronizationService.synchronizeMySQLImageDataUpdate();
        }

        if(!myUserImages.equals(pgUserImages)) {
            dataSynchronizationService.synchronizeMySQLUserImageDataUpdate();
        }
    }

    public Page<UserData> findAllUsers(Pageable pageable) {
        Page<User> users = userRepositoryMy.findAllUser(pageable);

        return users.map(user -> {
            UserData userData = new UserData();
            userData.setId(user.getUserId());
            userData.setBirthDate(user.getBirthDate());
            userData.setName(user.getUsername());
            userData.setEmail(user.getEmail());
            userData.setRole(user.getRole());

            log.info("Get user success");
            return userData;
        });
    }

    public Page<UserDataForUser> searchListOfUser(UserSearchInput userSearchInput, Pageable pageable) {
        Page<UserDataForSearching> users = userRepositoryMy.searchListOfUser(
                userSearchInput.getUsername(),userSearchInput.getEmail(),pageable);

        return users.map(user -> {
            log.info(user.getUserId(), user.getImageId());

            UserDataForUser userDataForUser = new UserDataForUser();
            userDataForUser.setId(user.getUserId());
            userDataForUser.setName(user.getUsername());
            userDataForUser.setBirthDate(user.getBirthDate());
            userDataForUser.setEmail(user.getEmail());
            userDataForUser.setImageId(user.getImageId());
            userDataForUser.setImageName(user.getImageName());
            userDataForUser.setImagePath(user.getImagePath());

            log.info("Get user success");
            return userDataForUser;
        });
    }
}
