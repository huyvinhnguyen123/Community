package Blind.Sight.community.domain.repository.postgresql;

import Blind.Sight.community.domain.entity.many.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryRepositoryPg extends JpaRepository<ProductCategory, Long> {
}
