package com.kusithm.meetupd.domain.auth.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KakaoLoginResponseDto {

    private Long userId;

    private String redirectUrl;

    private String refreshToken;

    private String accessToken;

    public static KakaoLoginResponseDto of(Long userId, String redirectUrl, String refreshToken, String accessToken) {
        return KakaoLoginResponseDto.builder()
                .userId(userId)
                .redirectUrl(redirectUrl)
                .refreshToken(refreshToken)
                .accessToken(accessToken)
                .build();
    }
}
