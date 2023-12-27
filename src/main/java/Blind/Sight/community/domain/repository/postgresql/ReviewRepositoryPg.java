package Blind.Sight.community.domain.repository.postgresql;

import Blind.Sight.community.domain.entity.Review;
import Blind.Sight.community.domain.repository.postgresql.query.ReviewDataForSendMail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ReviewRepositoryPg extends JpaRepository<Review, String> {
    @Query(value = """
            SELECT productId FROM Products
            """, nativeQuery = true)
    Set<String> findAllProductIds();

    @Query(value = """
            SELECT productId, userId
            FROM Reviews
            WHERE productId = :productId
            """, nativeQuery = true)
    List<ReviewDataForSendMail> findReviewByProductId(String productId);

    Optional<Review> findReviewByUserId(String userId);

}
