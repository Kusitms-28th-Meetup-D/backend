package com.kusithm.meetupd.domain.auth.kakao.dto;

import lombok.Getter;

@Getter
public class KakaoGetUserInfoResponseDto {
    private Long id;

    private KakaoAcountDto kakao_account;

    @Getter
    public static class KakaoAcountDto {
        private String email;

        private KakaoProfileDto profile;

    }

    @Getter
    public static class KakaoProfileDto {

        private String profile_image_url;

    }
}
