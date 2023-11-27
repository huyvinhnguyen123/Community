package Blind.Sight.community.domain.repository.postgresql;

import Blind.Sight.community.domain.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepositoryPg extends JpaRepository<Payment, Long> {
}
