package com.kusithm.meetupd.domain.auth.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KakaoRegisterResponseDto {

    private Long userId;

    private String username;


    public static KakaoRegisterResponseDto of(Long userId, String username) {
        return KakaoRegisterResponseDto.builder()
                .userId(userId)
                .username(username)
                .build();
    }
}
