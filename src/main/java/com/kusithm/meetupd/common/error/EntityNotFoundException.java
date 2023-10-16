package com.kusithm.meetupd.common.error;

public class EntityNotFoundException extends ApplicationException{

    public EntityNotFoundException(ErrorCode error) {
        super(error);
    }
}
