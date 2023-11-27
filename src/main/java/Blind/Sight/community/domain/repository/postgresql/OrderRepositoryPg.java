package Blind.Sight.community.domain.repository.postgresql;

import Blind.Sight.community.domain.entity.Order;
import Blind.Sight.community.domain.repository.postgresql.query.ProductDataForPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepositoryPg extends JpaRepository<Order, String> {
    @Query(value = """
            SELECT * FROM orders o
            WHERE o.userId = ?1
            """,
            nativeQuery = true)
    List<Order> findByUserId(String userId);


    @Query(value = """
             SELECT p.productid, p.name, p.price, p.point
             FROM products p
             LEFT JOIN order_details od ON p.productid = od.productid
             LEFT JOIN orders o ON o.orderid = od.orderid
             WHERE o.orderid = ?1
            """,
            nativeQuery = true)
    List<ProductDataForPayment> findProductsByOrderId(String orderId);
}
