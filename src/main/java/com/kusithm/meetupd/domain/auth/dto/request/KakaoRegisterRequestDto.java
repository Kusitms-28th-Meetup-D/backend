package com.kusithm.meetupd.domain.auth.dto.request;

import lombok.Getter;

@Getter
public class KakaoRegisterRequestDto {

    private String kakaoAccessToken;

    private String username;

    private Integer age;

}
