package Blind.Sight.community.domain.entity;

import Blind.Sight.community.util.random.RandomId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "Reviews")
public class Review {
    @Id
    @Column(name = "reviewId", updatable = false)
    private String reviewId;
    @Column(name = "userId", updatable = false)
    private String userId;
    @Column(name = "productId", updatable = false)
    private String productId;

    @Column(name = "reviewComment")
    private String reviewComment;

    @Column(name = "isLike", nullable = false)
    private boolean isLike;
    @Column(name = "isDislike", nullable = false)
    private boolean isDislike;

    @Column(name = "rateStar")
    private int rateStar;
    @Column(name = "rateStarReview")
    private String rateStarReview;

    @Column(name = "createAt")
    private LocalDate createdAt;
    @Column(name = "updateAt")
    private LocalDate updateAt;

    public Review() {
        this.reviewId = RandomId.generateCounterIncrement("Review-");
        this.isLike = false;
        this.isDislike = false;
        this.createdAt = LocalDate.now();
        this.updateAt = LocalDate.now();
    }

    public Review(String userId, String productId) {
        this.reviewId = RandomId.generateCounterIncrement("Review-");
        this.userId = userId;
        this.productId = productId;
        this.isLike = false;
        this.isDislike = false;
        this.createdAt = LocalDate.now();
        this.updateAt = LocalDate.now();
    }

    public Review(String userId, String productId, String reviewComment, boolean isLike, boolean isDislike, int rateStar, String rateStarReview) {
        this.reviewId = RandomId.generateCounterIncrement("Review-");
        this.userId = userId;
        this.productId = productId;
        this.reviewComment = reviewComment;
        this.isLike = isLike;
        this.isDislike = isDislike;
        this.rateStar = rateStar;
        this.rateStarReview = rateStarReview;
        this.createdAt = LocalDate.now();
        this.updateAt = LocalDate.now();
    }
}
