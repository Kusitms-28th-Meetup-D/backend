package com.kusithm.meetupd.domain.fileUpload.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

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
