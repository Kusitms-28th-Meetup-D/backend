package com.kusithm.meetupd.domain.auth.kakao.dto;

import lombok.Getter;

@Getter
public class KakaoGetUserInfoResponseDto {
    private Long id;

    private KakaoAcountDto kakao_account;

    @Getter
    public class KakaoAcountDto {
        private String email;
    }
}
