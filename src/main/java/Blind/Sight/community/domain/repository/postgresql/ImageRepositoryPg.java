package Blind.Sight.community.domain.repository.postgresql;

import Blind.Sight.community.domain.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepositoryPg extends JpaRepository<Image, String> {
    Optional<Image> findByImageName(String imageName);
}
