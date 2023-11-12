package com.kusithm.meetupd.domain.review.controller;

import com.kusithm.meetupd.common.dto.SuccessResponse;
import com.kusithm.meetupd.common.dto.code.SuccessCode;
import com.kusithm.meetupd.domain.review.dto.request.UploadReviewRequestDto;
import com.kusithm.meetupd.domain.review.dto.response.CheckUserReviewedByNonUserResponseDto;
import com.kusithm.meetupd.domain.review.dto.response.GetIsUserReviewTeamResponseDto;
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

    @GetMapping("/info/{userId}")
    public ResponseEntity<SuccessResponse<GetUserReviewResponseDto>> getUserReviewByUserId (@PathVariable Long userId) {
        GetUserReviewResponseDto response = reviewService.getUserReviewByUserId(userId);
        return SuccessResponse.of(response);
    }

    @PostMapping("")
    public ResponseEntity<SuccessResponse<UploadReviewResponseDto>> uploadRecommendation(@RequestBody UploadReviewRequestDto request) {
        UploadReviewResponseDto response = reviewService.uploadReviews(request.getSendUserId(), request);
        return SuccessResponse.of(SuccessCode.CREATED, response);
    }

    @GetMapping("/non-user/check/{userId}")
    public ResponseEntity<SuccessResponse<CheckUserReviewedByNonUserResponseDto>> checkUserReviewedByNonUser(@PathVariable Long userId) {
        CheckUserReviewedByNonUserResponseDto response = reviewService.checkUserReviewedByNonUser(userId);
        return SuccessResponse.of(SuccessCode.OK, response);
    }

    @GetMapping("/check-reviewed")
    public ResponseEntity<SuccessResponse<GetIsUserReviewTeamResponseDto>> isUserReviewThisTeam(@RequestParam Long userId, @RequestParam Long teamId) {
        GetIsUserReviewTeamResponseDto response = reviewService.isUserReviewThisTeam(userId, teamId);
        return SuccessResponse.of(SuccessCode.OK, response);
    }

}
