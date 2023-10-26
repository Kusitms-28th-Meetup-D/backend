package com.kusithm.meetupd.common.error;

public class FileUploadFailedException extends ApplicationException {

    public FileUploadFailedException(ErrorCode error) {
        super(error);
    }
}
