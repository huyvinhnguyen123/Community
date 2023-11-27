package Blind.Sight.community.domain.service.system;

import Blind.Sight.community.domain.entity.Image;
import Blind.Sight.community.domain.entity.Type;
import Blind.Sight.community.domain.entity.User;
import Blind.Sight.community.domain.entity.many.UserImage;
import Blind.Sight.community.domain.repository.mysql.ImageRepositoryMy;
import Blind.Sight.community.domain.repository.mysql.TypeRepositoryMy;
import Blind.Sight.community.domain.repository.mysql.UserImageRepositoryMy;
import Blind.Sight.community.domain.repository.mysql.UserRepositoryMy;
import Blind.Sight.community.domain.repository.postgresql.ImageRepositoryPg;
import Blind.Sight.community.domain.repository.postgresql.TypeRepositoryPg;
import Blind.Sight.community.domain.repository.postgresql.UserImageRepositoryPg;
import Blind.Sight.community.domain.repository.postgresql.UserRepositoryPg;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DataSynchronizationService {
    private final UserRepositoryPg userRepositoryPg;
    private final UserRepositoryMy userRepositoryMy;
    private final ImageRepositoryPg imageRepositoryPg;
    private final ImageRepositoryMy imageRepositoryMy;
    private final UserImageRepositoryPg userImageRepositoryPg;
    private final UserImageRepositoryMy userImageRepositoryMy;
    private final TypeRepositoryPg typeRepositoryPg;
    private final TypeRepositoryMy typeRepositoryMy;
//====================================================================
    // User
    @Async
    public void synchronizeMySQLUserData() {
        List<User> users = userRepositoryPg.findAll();
        for(User user: users) {
            userRepositoryMy.save(user);
        }

        log.info("User's data has been transferred!");
    }

    @Async
    public void synchronizeMySQLUserDataUpdate() {
        List<User> pgUsers = userRepositoryPg.findAll();
        List<User> myUsers = userRepositoryMy.findAll();

        // Create a map for efficient comparison based on user IDs.
        Map<String, User> pgUserMap = pgUsers.stream().collect(Collectors.toMap(User::getUserId, id -> id));

        for (User myUser : myUsers) {
            String userId = myUser.getUserId();
            User pgUser = pgUserMap.get(userId);

            if (pgUser == null) {
                // User exists in MySQL but not in Postgres; delete it.
                userRepositoryMy.delete(myUser);
            } else {
                // User exists in both MySQL and Postgres; update it.
                myUser.setUserName(pgUser.getUsername()); // Update fields as needed.
                myUser.setEmail(pgUser.getEmail()); // Update fields as needed.
                userRepositoryMy.save(myUser);
            }
        }

        // Insert new records from Postgres.
        for (User pgUser : pgUsers) {
            String userId = pgUser.getUserId();
            if (userRepositoryMy.findById(userId).isEmpty()) {
                // User exists in Postgres but not in MySQL; insert it.
                userRepositoryMy.save(pgUser);
            }
        }

        log.info("User's data has been synchronized!");
    }
//====================================================================
    // Image
    @Async
    public void synchronizeMySQLImageData() {
        List<Image> images = imageRepositoryPg.findAll();
        for(Image image: images) {
            imageRepositoryMy.save(image);
        }

        log.info("Image's data has been transferred!");
    }

    @Async
    public void synchronizeMySQLImageDataUpdate() {
        List<Image> pgImages = imageRepositoryPg.findAll();
        List<Image> myImages = imageRepositoryMy.findAll();

        Map<String, Image> pgImageMap = pgImages.stream().collect(Collectors.toMap(Image::getImageId, id -> id));

        for (Image myImage: myImages) {
            String imageId = myImage.getImageId();
            Image pgImage = pgImageMap.get(imageId);

            if (pgImage == null) {
                imageRepositoryMy.delete(myImage);
            } else {
                myImage.setImageName(pgImage.getImageName());
                myImage.setImagePath(pgImage.getImagePath());
                myImage.setImagePathId(pgImage.getImagePathId());
                myImage.setTimeUpload(pgImage.getTimeUpload());
                imageRepositoryMy.save(myImage);
            }

        }

        for (Image pgImage : pgImages) {
            String imageId = pgImage.getImageId();
            if (imageRepositoryMy.findById(imageId).isEmpty()) {
                imageRepositoryMy.save(pgImage);
            }
        }

        log.info("Image's data has been synchronized!");
    }
//====================================================================
    // UserImage
    @Async
    public void synchronizeMySQLUserImageData() {
        List<UserImage> userImages = userImageRepositoryPg.findAll();
        for(UserImage userImage: userImages) {
            userImageRepositoryMy.save(userImage);
        }

        log.info("UserImage's data has been transferred!");
    }

    @Async
    public void synchronizeMySQLUserImageDataUpdate() {
        List<UserImage> pgUserImages = userImageRepositoryPg.findAll();
        List<UserImage> myUserImages = userImageRepositoryMy.findAll();

        Map<Long, UserImage> pgUserImageMap = pgUserImages.stream().collect(Collectors.toMap(UserImage::getUserImageId, id -> id));

        for (UserImage myUserImage: myUserImages) {
            Long userImageId = myUserImage.getUserImageId();
            UserImage pgUserImage = pgUserImageMap.get(userImageId);

            if (pgUserImage == null) {
                userImageRepositoryMy.delete(myUserImage);
            } else {
                myUserImage.setUser(pgUserImage.getUser());
                myUserImage.setImage(pgUserImage.getImage());
                userImageRepositoryMy.save(myUserImage);
            }

        }

        for (UserImage pgUserImage : pgUserImages) {
            Long userImageId = pgUserImage.getUserImageId();
            if (userImageRepositoryMy.findById(userImageId).isEmpty()) {
                userImageRepositoryMy.save(pgUserImage);
            }
        }

        log.info("UserImage's data has been synchronized!");
    }
//====================================================================
    // Type
    @Async
    public void synchronizeMySQLTypeData() {
        List<Type> types = typeRepositoryPg.findAll();
        for(Type type: types) {
            typeRepositoryMy.save(type);
        }

        log.info("Type's data has been transferred!");
    }
}
