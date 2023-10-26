package com.kusithm.meetupd.domain.auth.kakao;

import com.kusithm.meetupd.domain.auth.kakao.dto.KakaoGetIdResponseDto;
import com.kusithm.meetupd.domain.auth.kakao.dto.KakaoGetUserInfoResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "kakao-feign", url = "https://kapi.kakao.com")
public interface KakaoFeignClient {

    @GetMapping("/v1/user/access_token_info")
    public KakaoGetIdResponseDto getKakaoIdByAccessToken(@RequestHeader("Authorization") String accessToken);

    @GetMapping("/v2/user/me")
    public KakaoGetUserInfoResponseDto getKakaoUserEmailByAccessToken(@RequestHeader("Authorization") String accessToken);
}
