package Blind.Sight.community.domain.repository.postgresql;

import Blind.Sight.community.domain.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepositoryPg extends JpaRepository<Currency, Long> {
}
