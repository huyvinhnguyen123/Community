package Blind.Sight.community.domain.repository.postgresql;

import Blind.Sight.community.domain.entity.address.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepositoryPg extends JpaRepository<City, Long> {
}
