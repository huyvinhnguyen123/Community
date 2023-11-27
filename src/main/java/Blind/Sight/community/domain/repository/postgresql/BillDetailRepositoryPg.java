package Blind.Sight.community.domain.repository.postgresql;

import Blind.Sight.community.domain.entity.many.BillDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillDetailRepositoryPg extends JpaRepository<BillDetail, Long> {
}
