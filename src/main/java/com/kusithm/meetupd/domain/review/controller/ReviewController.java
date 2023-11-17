package com.kusithm.meetupd.domain.review.controller;

import com.kusithm.meetupd.common.auth.UserId;
import com.kusithm.meetupd.common.dto.SuccessResponse;
import com.kusithm.meetupd.common.dto.code.SuccessCode;
import com.kusithm.meetupd.domain.review.dto.request.NonUserReviewRequestDto;
import com.kusithm.meetupd.domain.review.dto.request.UploadReviewRequestDto;
import com.kusithm.meetupd.domain.review.dto.response.CheckUserReviewedByNonUserResponseDto;
import com.kusithm.meetupd.domain.review.dto.response.GetIsUserReviewTeamResponseDto;
import com.kusithm.meetupd.domain.review.dto.response.GetUserReviewResponseDto;
import com.kusithm.meetupd.domain.review.dto.response.UploadReviewResponseDto;
import com.kusithm.meetupd.domain.review.service.ReviewService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    // 유저 리뷰 정보 확인
    @GetMapping("/info/{userId}")
    public ResponseEntity<SuccessResponse<GetUserReviewResponseDto>> getUserReviewByUserId (@PathVariable Long userId) {
        GetUserReviewResponseDto response = reviewService.getUserReviewByUserId(userId);
        return SuccessResponse.of(response);
    }

    // 회원 리뷰 작성 API
    @PostMapping("")
    public ResponseEntity<SuccessResponse<UploadReviewResponseDto>> uploadRecommendation(@UserId Long userId, @RequestBody UploadReviewRequestDto request) throws MessagingException, UnsupportedEncodingException {
        UploadReviewResponseDto response = reviewService.uploadReviews(userId, request);
        return SuccessResponse.of(SuccessCode.CREATED, response);
    }

    // 유저가 이 팀에 리뷰를 작성했는지 체크하는 API
    @GetMapping("/check-reviewed/{teamId}")
    public ResponseEntity<SuccessResponse<GetIsUserReviewTeamResponseDto>> isUserReviewThisTeam(@UserId Long userId, @PathVariable Long teamId) {
        GetIsUserReviewTeamResponseDto response = reviewService.isUserReviewThisTeam(userId, teamId);
        return SuccessResponse.of(SuccessCode.OK, response);
    }

    // 비 회원이 리뷰 작성
    @PostMapping("/non-user")
    public ResponseEntity<SuccessResponse<UploadReviewResponseDto>> checkUserReviewedByNonUser(@RequestBody NonUserReviewRequestDto request) throws MessagingException, UnsupportedEncodingException {
        UploadReviewResponseDto response = reviewService.uploadNonUserReview(request);
        return SuccessResponse.of(SuccessCode.CREATED, response);
    }

    // 비 회원이 리뷰를 작성한 유저인지 체크하는 API
    @GetMapping("/non-user/check/{userId}")
    public ResponseEntity<SuccessResponse<CheckUserReviewedByNonUserResponseDto>> checkUserReviewedByNonUser(@PathVariable Long userId) {
        CheckUserReviewedByNonUserResponseDto response = reviewService.checkUserReviewedByNonUser(userId);
        return SuccessResponse.of(SuccessCode.OK, response);
    }
}
