package com.kusithm.meetupd.common.jwt.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Token {

    private String accessToken;

    private String refreshToken;

    public static Token of(String accessToken, String refreshToken) {
        return Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
