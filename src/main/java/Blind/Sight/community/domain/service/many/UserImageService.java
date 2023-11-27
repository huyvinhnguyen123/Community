package Blind.Sight.community.domain.service.many;

import Blind.Sight.community.domain.entity.Image;
import Blind.Sight.community.domain.entity.User;
import Blind.Sight.community.domain.entity.many.UserImage;
import Blind.Sight.community.domain.repository.postgresql.UserImageRepositoryPg;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserImageService {
    private final UserImageRepositoryPg userImageRepositoryPg;

    public Iterable<UserImage> findAll() {
        Iterable<UserImage> userImages = userImageRepositoryPg.findAll();
        log.info("Find all user image success");
        return userImages;
    }

    public UserImage findUserImageById(Long userImageId) {
        UserImage existUserImage = userImageRepositoryPg.findById(userImageId).orElseThrow(
                () -> new NullPointerException("Not found this user image: " + userImageId)
        );

        log.info("Found user image");
        return existUserImage;
    }

    public void createUserImage(User user, Image image) {
        UserImage userImage = new UserImage();
        userImage.setUser(user);
        userImage.setImage(image);
        userImageRepositoryPg.save(userImage);

        log.info("Create user's image success");
    }

    public void updateUserImage(Long userImageId, User user, Image image) {
        UserImage existUserImage = findUserImageById(userImageId);
        existUserImage.setUser(user);
        existUserImage.setImage(image);
        userImageRepositoryPg.save(existUserImage);

        log.info("Update user's image success");
    }

    public void deleteUserImage(Long userImageId) {
        UserImage existUserImage = findUserImageById(userImageId);
        userImageRepositoryPg.delete(existUserImage);

        log.info("Delete user's image success");
    }
}
