package com.kusithm.meetupd.domain.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kusithm.meetupd.common.error.ConflictException;
import com.kusithm.meetupd.common.error.EntityNotFoundException;
import com.kusithm.meetupd.common.error.UnauthorizedException;
import com.kusithm.meetupd.common.jwt.TokenProvider;
import com.kusithm.meetupd.common.jwt.dto.Token;
import com.kusithm.meetupd.common.redis.entity.RefreshToken;
import com.kusithm.meetupd.common.redis.repository.RefreshTokenRepository;
import com.kusithm.meetupd.domain.auth.dto.request.KakaoLoginRequestDto;
import com.kusithm.meetupd.domain.auth.dto.request.KakaoRegisterRequestDto;
import com.kusithm.meetupd.domain.auth.dto.request.ReissueRequestDto;
import com.kusithm.meetupd.domain.auth.dto.response.KakaoLoginResponseDto;
import com.kusithm.meetupd.domain.auth.dto.response.KakaoRegisterResponseDto;
import com.kusithm.meetupd.domain.auth.dto.response.ReissueResponseDto;
import com.kusithm.meetupd.domain.auth.kakao.KakaoAccessToken;
import com.kusithm.meetupd.domain.auth.kakao.KakaoFeignClient;
import com.kusithm.meetupd.domain.auth.kakao.dto.KakaoGetIdResponseDto;
import com.kusithm.meetupd.domain.auth.kakao.dto.KakaoGetUserInfoResponseDto;
import com.kusithm.meetupd.domain.user.entity.User;
import com.kusithm.meetupd.domain.user.mysql.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.kusithm.meetupd.common.error.ErrorCode.*;
import static com.kusithm.meetupd.common.redis.entity.RefreshToken.createRefreshToken;
import static com.kusithm.meetupd.domain.auth.kakao.KakaoAccessToken.createKakaoAccessToken;
import static com.kusithm.meetupd.domain.user.entity.User.createUser;

@RequiredArgsConstructor
@Transactional
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final KakaoFeignClient kakaoFeignClient;

    public KakaoRegisterResponseDto register(KakaoRegisterRequestDto request) {

        KakaoAccessToken kakaoAccessToken = createKakaoAccessToken(request.getKakaoAccessToken());
        KakaoGetUserInfoResponseDto kakaoUserInfo = kakaoFeignClient.getKakaoUserEmailByAccessToken(kakaoAccessToken.getAccessTokenWithTokenType());
        // 이미 회원가입 된 유저인지 확인
        validateNotAlreadySignIn(kakaoUserInfo.getId());

        //아니라면 새로 유저 생성해줌
        User user = createUserByKakaoUserInfo(kakaoUserInfo, request.getUsername(), request.getAge());
        User savedUser = userRepository.save(user);

        return KakaoRegisterResponseDto.of(savedUser.getId(), savedUser.getUsername());
    }

    public KakaoLoginResponseDto kakaoLogin(KakaoLoginRequestDto request) {
        KakaoAccessToken kakaoAccessToken = createKakaoAccessToken(request.getKakaoAccessToken());
        KakaoGetIdResponseDto kakaoId = getKakaoIdByAccessToken(kakaoAccessToken.getAccessTokenWithTokenType());

        User findUser = getUserByKakaoId(kakaoId.getId());

        Token token = generateToken(findUser.getId());
        saveRefreshToken(findUser.getId(), token.getRefreshToken());

        return KakaoLoginResponseDto.of(findUser.getId(), request.getRedirectUrl(), token.getRefreshToken(), token.getAccessToken() );
    }

    public ReissueResponseDto reissue(ReissueRequestDto request) throws JsonProcessingException {
        Long userId = getUserIdFromAccessToken(request.getAccessToken());
        RefreshToken refreshToken = getRefreshTokenFromUserId(userId);
        validateRefreshTokenNotExpired(refreshToken);
        validateUserRefreshToken(refreshToken.getRefreshToken(), request.getRefreshToken());

        String accessToken = tokenProvider.createAccessToken(userId);

        if(validateRefreshTokenRemainDayUnderOneWeek(refreshToken.getRefreshToken())) {
            updateNewRefreshToken(refreshToken);
        }

        return ReissueResponseDto.of(accessToken, refreshToken.getRefreshToken());
    }


    private void validateNotAlreadySignIn(Long kakaoId) {
        if(userRepository.existsByKakaoId(kakaoId)){
            throw new ConflictException(DUPLICATE_KAKAO_ID);
        }
    }

    private User createUserByKakaoUserInfo(KakaoGetUserInfoResponseDto kakaoUserInfo, String username, Integer age) {
        return createUser(kakaoUserInfo.getId(), username, age, kakaoUserInfo.getKakao_account().getEmail());
    }

    private KakaoGetIdResponseDto getKakaoIdByAccessToken(String kakaoAccessToken) {
        return kakaoFeignClient.getKakaoIdByAccessToken(kakaoAccessToken);
    }

    private User getUserByKakaoId(Long kakaoId) {
        return userRepository.findByKakaoId(kakaoId).orElseThrow(
                () -> new EntityNotFoundException(NOT_SIGN_IN_KAKAO_ID));
    }

    private Token generateToken(Long userId) {
        return Token.of(tokenProvider.createAccessToken(userId), tokenProvider.createRefreshToken());
    }

    private void saveRefreshToken(Long userId, String newRefreshToken) {
        RefreshToken refreshToken = createRefreshToken(userId, newRefreshToken);
        refreshTokenRepository.save(refreshToken);
    }

    private Boolean validateRefreshTokenRemainDayUnderOneWeek(String refreshToken) {
        Long expireTime = tokenProvider.getExpDateFromToken(refreshToken);
        return (expireTime * 1000 - System.currentTimeMillis()) / (24 * 3600000) < 7;
    }

    private Long getUserIdFromAccessToken(String accessToken) throws JsonProcessingException {
        return Long.parseLong(tokenProvider.decodeJwtPayloadSubject(accessToken));
    }

    private RefreshToken getRefreshTokenFromUserId(Long userId) {
        return refreshTokenRepository.findById(userId)
                .orElseThrow(() -> new UnauthorizedException(EXPIRED_JWT_REFRESH_TOKEN));
    }


    private void validateRefreshTokenNotExpired(RefreshToken refreshToken) {
        tokenProvider.validateRefreshToken(refreshToken.getRefreshToken());
    }

    private void validateUserRefreshToken(String originalRefreshToken, String requestRefreshToken) {
        if(!originalRefreshToken.equals(requestRefreshToken)) {
            throw new UnauthorizedException(INVALID_JWT_REFRESH_TOKEN);
        }
    }

    private void updateNewRefreshToken(RefreshToken refreshToken) {
        String newRefreshToken1 = tokenProvider.createRefreshToken();
        refreshToken.update(newRefreshToken1);
    }
}
