package Blind.Sight.community.domain.entity;

import Blind.Sight.community.util.random.RandomId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "Currencies")
public class Currency {
    @Id
    @Column(name = "currencyId", updatable = false)
    private String currencyId;
    @Column(name = "currencyName", unique = true, nullable = false)
    private String currencyName;
    @Column(name = "currencyCode", unique = true, nullable = false)
    private String currencyCode;
    @Column(name = "symbol", nullable = false)
    private String symbol;
    @Column(name = "exchangeRate", nullable = false)
    private Double exchangeRate;
    @Column(name = "defaultCurrency", nullable = false)
    private Double defaultCurrency;
    @Column(name = "isDefaultCurrency", nullable = false)
    private Boolean isDefaultCurrency;

    @Column(name = "createAt")
    private LocalDate createAt;
    @Column(name = "updateAt")
    private LocalDate updateAt;

    public Currency() {
        this.currencyId = RandomId.generateCounterIncrement("Currency-");
        this.createAt = LocalDate.now();
        this.updateAt = LocalDate.now();
    }
}
