package org.aper.web.domain.review.controller;

import com.aperlibrary.user.entity.User;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.aper.web.domain.review.service.ReviewService;
import org.aper.web.domain.review.dto.ReviewRequestDto.CreateReviewRequestDto;
import org.aper.web.domain.review.dto.ReviewResponseDto.CreatedReviewDto;
import org.aper.web.domain.user.valid.UserValidationSequence;
import org.aper.web.global.docs.ReviewControllerDocs;
import org.aper.web.global.dto.ResponseDto;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/review")
@Validated(UserValidationSequence.class)
public class ReviewController implements ReviewControllerDocs {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping()
    public ResponseDto<CreatedReviewDto> createReview(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody @Valid CreateReviewRequestDto requestDto) {
        User reviewer = userDetails.user();
        CreatedReviewDto reviewData = reviewService.createReview(reviewer, requestDto);
        return ResponseDto.success("리뷰작성에 성공하였습니다.", reviewData);
    }
}
