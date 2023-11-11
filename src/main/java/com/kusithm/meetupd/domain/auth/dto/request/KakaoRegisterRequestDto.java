package com.kusithm.meetupd.domain.auth.dto.request;

import lombok.Getter;

@Getter
public class KakaoRegisterRequestDto {

    private String kakaoAccessToken;

    private String username;

    private Integer location;

    private String major;

    private String task;

    private String selfIntroduce;

}
