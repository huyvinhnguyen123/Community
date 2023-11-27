package Blind.Sight.community.domain.repository.postgresql;

import Blind.Sight.community.domain.entity.many.CartDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartDetailRepositoryPg extends JpaRepository<CartDetail, Long> {
    @Query(value = """
            SELECT * FROM cart_details cd
            WHERE cd.cartid = ?1
            """,
            nativeQuery = true)
    List<CartDetail> findByCartId(String cartId);
}
