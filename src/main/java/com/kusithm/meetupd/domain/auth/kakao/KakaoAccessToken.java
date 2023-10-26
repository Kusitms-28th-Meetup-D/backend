package com.kusithm.meetupd.domain.auth.kakao;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class KakaoAccessToken {

    private String accessToken;

    private static final String TOKEN_TYPE = "Bearer ";

    public static KakaoAccessToken createKakaoAccessToken(String accessToken) {
        return new KakaoAccessToken(accessToken);
    }

    public String getAccessTokenWithTokenType() {
        return TOKEN_TYPE + accessToken;
    }
}
