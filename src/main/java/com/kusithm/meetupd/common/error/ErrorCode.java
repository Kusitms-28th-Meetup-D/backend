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


    /**
     *  404 Not Found
     */
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "요청하신 엔티티를 찾을 수 없습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "요청하신 유저를 찾을 수 없습니다."),
    NOT_SIGN_IN_KAKAO_ID(HttpStatus.NOT_FOUND, "회원가입되지 않은 KAKAO 계정입니다. 회원가입을 진행해 주세요"),



    /**
     * 409 Conflict
     */
    DUPLICATE_SAMPLE_TEXT(HttpStatus.CONFLICT, "이미 존재하는 TEXT입니다."),
    DUPLICATE_USER_NAME(HttpStatus.CONFLICT, "이미 존재하는 유저 이름 입니다."),
    DUPLICATE_KAKAO_ID(HttpStatus.CONFLICT, "이미 회원가입 된 카카오 계정 입니다."),

    /**
     *  500 INTERNAL SERVER ERROR
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다."),
    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S3 이미지 업로드에 실패");

    private final HttpStatus status;
    private final String message;
}
