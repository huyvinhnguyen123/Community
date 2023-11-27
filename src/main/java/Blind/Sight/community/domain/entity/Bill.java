package Blind.Sight.community.domain.entity;

import Blind.Sight.community.util.random.RandomId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "Bills")
public class Bill {
    @Id
    @Column(name = "billId", updatable = false)
    private String billId;
    @Column(name = "billName", nullable = false)
    private String billName;

    @Column(name = "totalPrice", nullable = false)
    private Double totalPrice;
    @Column(name = "totalMoney", nullable = false)
    private Double totalMoney;
    @Column(name = "leftMoney", nullable = false)
    private Double leftMoney;

    @Column(name = "totalProductPoint", nullable = false)
    private Double totalProductPoint;
    @Column(name = "totalUserPoint", nullable = false)
    private Double totalUserPoint;
    @Column(name = "leftPoint", nullable = false)
    private Double leftPoint;

    @Column(name = "datePayment", nullable = false)
    private LocalDate datePayment;

    @Column(name = "createAt")
    private LocalDate createAt;
    @Column(name = "updateAt")
    private LocalDate updateAt;

    @OneToOne
    @JoinColumn(name = "orderId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Order order;

    @OneToOne
    @JoinColumn(name = "currencyId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Currency currency;

    public Bill() {
        this.billId = RandomId.generateCounterIncrement("Bill-");
        this.billName = "Bill-" + LocalDate.now();
        this.datePayment = LocalDate.now();
        this.createAt = LocalDate.now();
        this.updateAt = LocalDate.now();
    }

    public Bill(Double totalPrice, Double totalMoney, Double leftMoney,
                Double totalProductPoint, Double totalUserPoint, Double leftPoint) {
        this.billId = RandomId.generateCounterIncrement("Bill-");
        this.billName = "Bill-" + LocalDate.now();
        this.totalPrice = totalPrice;
        this.totalMoney = totalMoney;
        this.leftMoney = leftMoney;
        this.totalProductPoint = totalProductPoint;
        this.totalUserPoint = totalUserPoint;
        this.leftPoint = leftPoint;
        this.datePayment = LocalDate.now();
        this.createAt = LocalDate.now();
        this.updateAt = LocalDate.now();
    }
}
