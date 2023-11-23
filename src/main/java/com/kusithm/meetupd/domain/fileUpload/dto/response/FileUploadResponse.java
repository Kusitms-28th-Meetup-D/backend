package com.kusithm.meetupd.domain.fileUpload.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class FileUploadResponse {
    List<String> fileUrls = new ArrayList<>();

    public FileUploadResponse (List<String> fileUrls){
        this.fileUrls=fileUrls;
    }

}
