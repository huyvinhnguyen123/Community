package Blind.Sight.community.domain.repository.postgresql;

import Blind.Sight.community.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepositoryPg extends JpaRepository<Category, String> {
}
