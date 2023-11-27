package Blind.Sight.community.domain.repository.postgresql;

import Blind.Sight.community.domain.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepositoryPg extends JpaRepository<Cart, String> {
    @Query(value = """
            SELECT * FROM Carts c
            WHERE c.userid = ?1
            """,
            nativeQuery = true)
    Optional<Cart> findByUserId(String userId);
    Optional<Cart> findByToken(String token);
}
