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
@Table(name = "Carts")
public class Cart {
    @Id
    @Column(name = "cartId", updatable = false)
    private String cartId;
    @Column(name = "token")
    private String token;
    @Column(name = "totalPrice", nullable = false)
    private Double totalPrice;
    @Column(name = "totalPoint", nullable = false)
    private Double totalPoint;
    @Column(name = "versionNo", nullable = false)
    private Double versionNo;

    @Column(name = "createAt")
    private LocalDate createAt;
    @Column(name = "updateAt")
    private LocalDate updateAt;

    @OneToOne
    @JoinColumn(name = "userId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    public Cart() {
        this.cartId = RandomId.generateCounterIncrement("Cart-");
        this.createAt = LocalDate.now();
        this.updateAt = LocalDate.now();
    }

    public Cart(String token) {
        this.cartId = RandomId.generateCounterIncrement("Cart-");
        this.token = token;
        this.createAt = LocalDate.now();
        this.updateAt = LocalDate.now();
    }

    public Cart(Double totalPrice, Double totalPoint, Double versionNo) {
        this.cartId = RandomId.generateCounterIncrement("Cart-");
        this.totalPrice = totalPrice;
        this.totalPoint = totalPoint;
        this.versionNo = versionNo;
        this.createAt = LocalDate.now();
        this.updateAt = LocalDate.now();
    }
}
