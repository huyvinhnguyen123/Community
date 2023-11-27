package Blind.Sight.community.domain.entity.many;

import Blind.Sight.community.domain.entity.Bill;
import Blind.Sight.community.domain.entity.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Bill_Details")
public class BillDetail {
    @Id
    @Column(name = "billDetailId", updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long billDetailId;
    @Column(name = "datePaymentProduct", nullable = false)
    private LocalDate datePaymentProduct;

    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "billId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Bill bill;
}
