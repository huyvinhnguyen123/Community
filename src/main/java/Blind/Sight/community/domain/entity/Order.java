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
@Table(name = "Orders")
public class Order {
    @Id
    @Column(name = "orderId", updatable = false)
    private String orderId;
    @Column(name = "displayId", nullable = false)
    private Integer displayId;
    @Column(name = "totalPrice", nullable = false)
    private Double totalPrice;
    @Column(name = "totalPoint", nullable = false)
    private Double totalPoint;
    @Column(name = "statusCode")
    private Integer statusCode;
    @Column(name = "orderDate", nullable = false)
    private LocalDate orderDate;
    @Column(name = "isPayment", nullable = false)
    private Boolean isPayment;

    @Column(name = "createAt")
    private LocalDate createAt;
    @Column(name = "updateAt")
    private LocalDate updateAt;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    public Order() {
        this.orderId = RandomId.generateCounterIncrement("Order-");
        this.isPayment = false;
        this.orderDate = LocalDate.now();
        this.createAt = LocalDate.now();
        this.updateAt = LocalDate.now();
    }

    public Order(Integer displayId, Double totalPrice, Double totalPoint, Integer statusCode) {
        this.orderId = RandomId.generateCounterIncrement("Order-");
        this.isPayment = false;
        this.displayId = displayId;
        this.totalPrice = totalPrice;
        this.totalPoint = totalPoint;
        this.statusCode = statusCode;
        this.orderDate = LocalDate.now();
        this.createAt = LocalDate.now();
        this.updateAt = LocalDate.now();
    }
}
