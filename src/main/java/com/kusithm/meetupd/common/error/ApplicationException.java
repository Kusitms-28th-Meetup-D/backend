package com.kusithm.meetupd.common.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApplicationException extends RuntimeException {
    private final HttpStatus status;

    public ApplicationException(ErrorCode error) {
        super(error.getMessage());
        this.status = error.getStatus();
    }
}
