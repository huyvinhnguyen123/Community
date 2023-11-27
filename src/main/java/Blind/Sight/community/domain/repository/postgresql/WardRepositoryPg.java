package Blind.Sight.community.domain.repository.postgresql;

import Blind.Sight.community.domain.entity.address.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WardRepositoryPg extends JpaRepository<Ward, Long> {
}
