package Blind.Sight.community.dto.review;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewInput {
    private String productId;
    private String reviewComment;
    private boolean isLike;
    private boolean isDislike;
    private Integer rateStar;
}
