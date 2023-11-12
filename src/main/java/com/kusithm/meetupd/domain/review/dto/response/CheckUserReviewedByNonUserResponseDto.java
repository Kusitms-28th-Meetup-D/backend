package com.kusithm.meetupd.domain.review.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CheckUserReviewedByNonUserResponseDto {

    private Boolean alreadyReviewed;

    public static CheckUserReviewedByNonUserResponseDto of(Boolean alreadyReviewed) {
        return CheckUserReviewedByNonUserResponseDto.builder()
                .alreadyReviewed(alreadyReviewed)
                .build();
    }
}
