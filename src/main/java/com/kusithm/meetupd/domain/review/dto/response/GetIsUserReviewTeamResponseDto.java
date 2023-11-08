package com.kusithm.meetupd.domain.review.dto.response;


import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GetIsUserReviewTeamResponseDto {
    private Boolean isReviewedBefore;

    public static GetIsUserReviewTeamResponseDto of(Boolean isReviewedBefore) {
        return GetIsUserReviewTeamResponseDto.builder()
                .isReviewedBefore(isReviewedBefore)
                .build();
    }
}
