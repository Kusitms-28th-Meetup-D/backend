package com.kusithm.meetupd.common.error;

public class DuplicateException extends ApplicationException{

    public DuplicateException(ErrorCode error) {
        super(error);
    }
}
