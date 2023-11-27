package Blind.Sight.community.domain.repository.mysql;

import Blind.Sight.community.domain.entity.many.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserImageRepositoryMy extends JpaRepository<UserImage, Long> {
}
