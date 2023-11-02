package com.kusithm.meetupd.common.error;

public class EnumNotFoundException extends ApplicationException{

    public EnumNotFoundException(ErrorCode error) {
        super(error);
    }
}
