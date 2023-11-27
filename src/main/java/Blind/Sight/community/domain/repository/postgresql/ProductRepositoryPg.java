package Blind.Sight.community.domain.repository.postgresql;

import Blind.Sight.community.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepositoryPg extends JpaRepository<Product, String> {
}
