package Blind.Sight.community.domain.repository.postgresql;

import Blind.Sight.community.domain.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepositoryPg extends JpaRepository<Bill, String> {
}
