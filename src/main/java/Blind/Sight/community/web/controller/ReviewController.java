package Blind.Sight.community.web.controller;

import Blind.Sight.community.domain.service.write.ReviewServicePg;
import Blind.Sight.community.dto.review.ReviewInput;
import Blind.Sight.community.web.response.common.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/v1/reviews")
public class ReviewController {
    private final ReviewServicePg reviewServicePg;

    @PostMapping("/create")
    public ResponseEntity<ResponseDto<Object>> createReviewComment(Authentication authentication, @RequestBody ReviewInput reviewInput) {
        log.info("Request creating review...");
        reviewServicePg.createReview(authentication, reviewInput);
        return ResponseEntity.ok(ResponseDto.build().withMessage("OK"));
    }
}
