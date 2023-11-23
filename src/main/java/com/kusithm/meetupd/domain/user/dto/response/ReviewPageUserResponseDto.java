package com.kusithm.meetupd.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ReviewPageUserResponseDto {

    private Long teamId;
    private List<UserReviewResponseDto> userReviewResponseDtoList;

    public static ReviewPageUserResponseDto of(Long teamId, List<UserReviewResponseDto> userReviewInfo) {
        return ReviewPageUserResponseDto.builder()
                .teamId(teamId)
                .userReviewResponseDtoList(userReviewInfo)
                .build();
    }

}
