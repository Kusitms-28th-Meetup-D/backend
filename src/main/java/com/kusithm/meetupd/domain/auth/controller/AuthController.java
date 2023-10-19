package com.kusithm.meetupd.domain.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kusithm.meetupd.common.dto.SuccessResponse;
import com.kusithm.meetupd.common.dto.code.SuccessCode;
import com.kusithm.meetupd.domain.auth.dto.request.KakaoLoginRequestDto;
import com.kusithm.meetupd.domain.auth.dto.request.KakaoRegisterRequestDto;
import com.kusithm.meetupd.domain.auth.dto.request.ReissueRequestDto;
import com.kusithm.meetupd.domain.auth.dto.response.KakaoLoginResponseDto;
import com.kusithm.meetupd.domain.auth.dto.response.KakaoRegisterResponseDto;
import com.kusithm.meetupd.domain.auth.dto.response.ReissueResponseDto;
import com.kusithm.meetupd.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<SuccessResponse<KakaoRegisterResponseDto>> register(@RequestBody KakaoRegisterRequestDto request) {
        KakaoRegisterResponseDto response = authService.register(request);
        return SuccessResponse.of(SuccessCode.USER_CREATED, response);
    }
    @PostMapping("/login")
    public ResponseEntity<SuccessResponse<KakaoLoginResponseDto>> kakaoLogin(@RequestBody KakaoLoginRequestDto request) {
        KakaoLoginResponseDto response = authService.kakaoLogin(request);
        return SuccessResponse.of(SuccessCode.OK, response);
    }

    @PostMapping("/reissue")
    public ResponseEntity<SuccessResponse<ReissueResponseDto>> reissue(@RequestBody ReissueRequestDto request) throws JsonProcessingException {
        ReissueResponseDto response = authService.reissue(request);
        return SuccessResponse.of(SuccessCode.OK, response);
    }

}
