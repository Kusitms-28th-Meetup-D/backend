package com.kusithm.meetupd.domain.auth.dto.request;

import lombok.Getter;

@Getter
public class KakaoLoginRequestDto {

    private String kakaoAccessToken;

    private String redirectUrl;
}
