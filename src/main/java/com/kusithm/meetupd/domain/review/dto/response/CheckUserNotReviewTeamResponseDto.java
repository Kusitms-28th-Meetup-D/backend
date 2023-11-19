package com.kusithm.meetupd.domain.review.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CheckUserNotReviewTeamResponseDto {

    private Boolean userNotReview;

    public static CheckUserNotReviewTeamResponseDto of(Boolean userNotReview) {
        return CheckUserNotReviewTeamResponseDto.builder()
                .userNotReview(userNotReview)
                .build();
    }
}
