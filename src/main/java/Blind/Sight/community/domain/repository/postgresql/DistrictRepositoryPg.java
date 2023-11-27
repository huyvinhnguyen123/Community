package Blind.Sight.community.domain.repository.postgresql;

import Blind.Sight.community.domain.entity.address.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistrictRepositoryPg extends JpaRepository<District, Long> {
}
