package Blind.Sight.community.domain.entity.many;

import Blind.Sight.community.domain.entity.Image;
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
@Table(name = "Product_Images")
public class ProductImage {
    @Id
    @Column(name = "productImageId", updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productImageId;

    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "imageId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Image image;
}
