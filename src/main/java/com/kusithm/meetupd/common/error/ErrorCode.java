package com.kusithm.meetupd.common.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    /**
     *  400 Bad Request
     */
    ALREADY_USER_USE_TICKET(HttpStatus.BAD_REQUEST, "이미 해당 유저에게 티켓을 사용했습니다."),

    /**
     *  401 Unauthorized
     */
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "접근할 수 있는 권한이 없습니다. access token을 확인하세요."),
    INVALID_JWT_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED,  "유효하지 않은 ACCESS TOKEN 입니다"),
    EXPIRED_JWT_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "ACCESS TOKEN이 만료되었습니다. 재발급 받아주세요."),
    INVALID_JWT_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED,  "유효하지 않은 REFRESH TOKEN 입니다."),
    EXPIRED_JWT_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "REFRESH TOKEN이 만료되었습니다. 다시 로그인해주세요"),

    /**
     *  403 Forbidden
     */
    WRONG_USER_PASSWORD(HttpStatus.FORBIDDEN, "입력하신 비밀번호가 올바르지 않습니다."),
    USER_NOT_HAVE_ENOUGH_TICKET(HttpStatus.FORBIDDEN, "보유한 티켓의 수가 부족합니다. 티켓을 충전해주세요."),


    /**
     *  404 Not Found
     */
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "요청하신 엔티티를 찾을 수 없습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "요청하신 유저를 찾을 수 없습니다."),
    NOT_SIGN_IN_KAKAO_ID(HttpStatus.NOT_FOUND, "회원가입되지 않은 KAKAO 계정입니다. 회원가입을 진행해 주세요"),

    ENUM_NOT_FOUND(HttpStatus.NOT_FOUND, "요청하신 ENUM TYPE을 찾을 수 없습니다."),

    CONTEST_NOT_FOUND(HttpStatus.NOT_FOUND, "요청하신 공모전을 찾을 수 없습니다."),
    USER_RECOMMENDATION_NOT_FOUND(HttpStatus.NOT_FOUND, "요청하신 유저의 추천사를 찾을 수 없습니다."),



    /**
     * 409 Conflict
     */
    DUPLICATE_SAMPLE_TEXT(HttpStatus.CONFLICT, "이미 존재하는 TEXT입니다."),
    DUPLICATE_USER_NAME(HttpStatus.CONFLICT, "이미 존재하는 유저 이름 입니다."),
    DUPLICATE_KAKAO_ID(HttpStatus.CONFLICT, "이미 회원가입 된 카카오 계정 입니다."),
    DUPLICATE_USER_REVIEW_TEAM(HttpStatus.CONFLICT, "이미 회원님은 해당 팀에 리뷰를 남겼습니다."),

    /**
     *  500 INTERNAL SERVER ERROR
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다."),
    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S3 이미지 업로드에 실패");

    private final HttpStatus status;
    private final String message;
}
