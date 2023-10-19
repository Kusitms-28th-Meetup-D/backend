package com.kusithm.meetupd.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserCheckResponseDto {

    private Long userId;

    private String username;

    public static UserCheckResponseDto of(Long userId, String username) {
        return UserCheckResponseDto.builder()
                .userId(userId)
                .username(username)
                .build();
    }
}
