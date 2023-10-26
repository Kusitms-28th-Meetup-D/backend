package com.kusithm.meetupd.common.error;

public class UnauthorizedException extends ApplicationException{
    public UnauthorizedException(ErrorCode error) {
        super(error);
    }
}
