package Blind.Sight.community.domain.entity.many;

import Blind.Sight.community.domain.entity.Order;
import Blind.Sight.community.domain.entity.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Order_Details")
public class OrderDetail {
    @Id
    @Column(name = "orderDetailId", updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderDetailId;
    @Column(nullable = false)
    private Integer quantity;
    @Column(nullable = false)
    private Double price;
    @Column(name = "totalPrice", nullable = false)
    private Double totalPrice;

    @ManyToOne
    @JoinColumn(name = "orderId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;
}
