package com.kusithm.meetupd.common.error.dto;

import com.kusithm.meetupd.common.error.ApplicationException;
import com.kusithm.meetupd.common.error.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class ErrorResponse {
    private final int status;
    private final String message;

    public static ErrorResponse create() {
        return new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR.getStatus().value(), ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
    }

    public static ErrorResponse of(ApplicationException exception) {
        return ErrorResponse.builder()
                .status(exception.getStatus().value())
                .message(exception.getMessage())
                .build();
    }
}
