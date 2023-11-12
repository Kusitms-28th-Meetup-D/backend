package com.kusithm.meetupd.domain.auth.dto.response;

import com.kusithm.meetupd.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KakaoLoginResponseDto {

    private Long userId;

    private String redirectUrl;

    private String refreshToken;

    private String accessToken;

    private String profileImage;

    private String name;

    public static KakaoLoginResponseDto of(User user, String redirectUrl, String refreshToken, String accessToken) {
        return KakaoLoginResponseDto.builder()
                .userId(user.getId())
                .redirectUrl(redirectUrl)
                .refreshToken(refreshToken)
                .accessToken(accessToken)
                .profileImage(user.getProfileImage())
                .name(user.getUsername())
                .build();
    }
}
