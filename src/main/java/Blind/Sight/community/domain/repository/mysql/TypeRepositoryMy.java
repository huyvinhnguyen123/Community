package Blind.Sight.community.domain.repository.mysql;

import Blind.Sight.community.domain.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeRepositoryMy extends JpaRepository<Type, String> {
}
