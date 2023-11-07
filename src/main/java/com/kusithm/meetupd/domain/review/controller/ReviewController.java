package com.kusithm.meetupd.domain.review.controller;

import com.kusithm.meetupd.common.auth.UserId;
import com.kusithm.meetupd.common.dto.SuccessResponse;
import com.kusithm.meetupd.common.dto.code.SuccessCode;
import com.kusithm.meetupd.domain.review.dto.request.UploadReviewRequestDto;
import com.kusithm.meetupd.domain.review.dto.response.GetUserReviewResponseDto;
import com.kusithm.meetupd.domain.review.dto.response.UploadReviewResponseDto;
import com.kusithm.meetupd.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/{userId}")
    public ResponseEntity<SuccessResponse<GetUserReviewResponseDto>> getUserReviewByUserId (@PathVariable Long userId) {
        GetUserReviewResponseDto response = reviewService.getUserReviewByUserId(userId);
        return SuccessResponse.of(response);
    }

    @PostMapping("")
    public ResponseEntity<SuccessResponse<UploadReviewResponseDto>> uploadRecommendation(@RequestBody UploadReviewRequestDto request) {
        UploadReviewResponseDto response = reviewService.uploadReviews(request.getSendUserId(), request);
        return SuccessResponse.of(SuccessCode.CREATED, response);
    }

}
