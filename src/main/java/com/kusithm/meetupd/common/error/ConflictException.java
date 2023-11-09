package com.kusithm.meetupd.common.error;

public class ConflictException extends ApplicationException{

    public ConflictException(ErrorCode error) {
        super(error);
    }
}
