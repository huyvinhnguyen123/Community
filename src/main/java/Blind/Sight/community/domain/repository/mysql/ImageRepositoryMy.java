package Blind.Sight.community.domain.repository.mysql;

import Blind.Sight.community.domain.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepositoryMy extends JpaRepository<Image, String> {
}
