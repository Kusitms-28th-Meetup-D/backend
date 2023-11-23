package com.kusithm.meetupd.domain.user.dto.response;

import com.kusithm.meetupd.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserReviewResponseDto {

    private Long teamMemberId; //추천사를 남길 팀원의 id
    private String teamMemberName; //추천사를 남길 팀원의 이름
    private String teamMemberImage; //추천사를 남길 팀원의 프로필 이미지

    public static UserReviewResponseDto of(User user) {
        return UserReviewResponseDto.builder()
                .teamMemberId(user.getId())
                .teamMemberName(user.getUsername())
                .teamMemberImage(user.getProfileImage())
                .build();
    }
}
