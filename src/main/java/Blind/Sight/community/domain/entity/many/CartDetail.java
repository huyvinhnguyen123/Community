package Blind.Sight.community.domain.entity.many;

import Blind.Sight.community.domain.entity.Cart;
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
@Table(name = "Cart_Details")
public class CartDetail {
    @Id
    @Column(name = "cartDetailId", updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartDetailId;
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
    @Column(name = "price", nullable = false)
    private Double price;
    @Column(name = "point", nullable = false)
    private Double point;
    @Column(name = "totalPrice", nullable = false)
    private Double totalPrice;
    @Column(name = "totalPoint", nullable = false)
    private Double totalPoint;
    @Column(name = "statusCode")
    private Integer statusCode;

    @ManyToOne
    @JoinColumn(name = "cartId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;
}
