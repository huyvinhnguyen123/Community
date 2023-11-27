package Blind.Sight.community.domain.repository.postgresql;

import Blind.Sight.community.domain.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointRepositoryPg extends JpaRepository<Point, String> {
}
