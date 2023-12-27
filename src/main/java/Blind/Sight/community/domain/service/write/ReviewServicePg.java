package Blind.Sight.community.domain.service.write;

import Blind.Sight.community.domain.entity.Review;
import Blind.Sight.community.domain.entity.User;
import Blind.Sight.community.domain.repository.postgresql.ReviewRepositoryPg;
import Blind.Sight.community.dto.review.ReviewInput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewServicePg {
    private final ReviewRepositoryPg reviewRepositoryPg;

    private Review findReviewByUserId(String userId) {
        Review existReview = reviewRepositoryPg.findReviewByUserId(userId).orElseThrow(
                () -> new NullPointerException("Not found this review: " + userId)
        );

        log.info("Found review");
        return existReview;
    }

    public void createReview(Authentication authentication, ReviewInput reviewInput) {
        User userLogin = new User();
        if (authentication != null && authentication.isAuthenticated()) {
            userLogin = (User) authentication.getPrincipal();
        }

        Review review = new Review();
        review.setUserId(userLogin.getUserId());

        Set<String> productIds = reviewRepositoryPg.findAllProductIds();
        for(String productId: productIds) {
            if (productId.equals(reviewInput.getProductId())) {
                review.setProductId(productId);
            }
        }

        review.setReviewComment(reviewInput.getReviewComment());

        if(Boolean.TRUE.equals(reviewInput.isLike()) && Boolean.TRUE.equals(reviewInput.isDislike())) {
            throw new IllegalArgumentException("Invalid like & dislike, choose one of them");
        }

        if(Boolean.TRUE.equals(reviewInput.isLike())) {
            review.setLike(true);
            review.setDislike(false);
        }

        if(Boolean.TRUE.equals(reviewInput.isDislike())) {
            review.setDislike(true);
            review.setLike(false);
        }

        review.setRateStar(reviewInput.getRateStar());
        switch (review.getRateStar()) {
            case 1: review.setRateStarReview("Worst"); break;
            case 2: review.setRateStarReview("Bad"); break;
            case 3: review.setRateStarReview("Neutral"); break;
            case 4: review.setRateStarReview("Good"); break;
            case 5: review.setRateStarReview("Excellent"); break;
            default: throw new IllegalArgumentException("Invalid rate star: " + review.getRateStar());
        }

        reviewRepositoryPg.save(review);
        log.info("Create review success");
    }

    public void updateReview(Authentication authentication, ReviewInput reviewInput) {
        User userLogin = new User();
        if (authentication != null && authentication.isAuthenticated()) {
            userLogin = (User) authentication.getPrincipal();
        }

        Review existReview = findReviewByUserId(userLogin.getUserId());
        existReview.setUpdateAt(LocalDate.now());
        existReview.setReviewComment(reviewInput.getReviewComment());

        if(Boolean.TRUE.equals(reviewInput.isLike()) && Boolean.TRUE.equals(reviewInput.isDislike())) {
            throw new IllegalArgumentException("Invalid like & dislike, choose one of them");
        }

        if(Boolean.TRUE.equals(reviewInput.isLike())) {
            existReview.setLike(true);
            existReview.setDislike(false);
        }

        if(Boolean.TRUE.equals(reviewInput.isDislike())) {
            existReview.setDislike(true);
            existReview.setLike(false);
        }

        existReview.setRateStar(reviewInput.getRateStar());
        switch (existReview.getRateStar()) {
            case 1: existReview.setRateStarReview("Worst"); break;
            case 2: existReview.setRateStarReview("Bad"); break;
            case 3: existReview.setRateStarReview("Neutral"); break;
            case 4: existReview.setRateStarReview("Good"); break;
            case 5: existReview.setRateStarReview("Excellent"); break;
            default: throw new IllegalArgumentException("Invalid rate star: " + existReview.getRateStar());
        }

        reviewRepositoryPg.save(existReview);
        log.info("Update review success");
    }
}
