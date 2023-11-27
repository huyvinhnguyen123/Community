package Blind.Sight.community.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "Payments")
public class Payment {
    @Id
    @Column(name = "paymentId", updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;
    @Column(name = "paymentMethod", nullable = false)
    private String paymentMethod;
    @Column(name = "usingCard", nullable = false)
    private Boolean usingCard;
    @Column(name = "usingPoint", nullable = false)
    private Boolean usingPoint;
    @Column(name = "usingCash", nullable = false)
    private Boolean usingCash;
    @Column(name = "allowPayment", nullable = false)
    private Boolean allowPayment;

    @Column(name = "createAt")
    private LocalDate createAt;
    @Column(name = "updateAt")
    private LocalDate updateAt;

    @ManyToOne
    @JoinColumn(name = "orderId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    public Payment() {
        this.usingCard = false;
        this.usingPoint = false;
        this.usingCash = false;
        this.allowPayment = false;
        this.createAt = LocalDate.now();
        this.updateAt = LocalDate.now();
    }

    public Payment(String paymentMethod) {
        this.paymentMethod = paymentMethod;
        this.usingCard = false;
        this.usingPoint = false;
        this.usingCash = false;
        this.allowPayment = false;
        this.createAt = LocalDate.now();
        this.updateAt = LocalDate.now();
    }
}
