package Blind.Sight.community.domain.repository.postgresql;

import Blind.Sight.community.domain.entity.many.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductImageRepositoryPg extends JpaRepository<ProductImage, Long> {
}
