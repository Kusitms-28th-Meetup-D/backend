package com.kusithm.meetupd.domain.fileUpload.controller;

import com.kusithm.meetupd.domain.fileUpload.dto.response.FileUploadResponse;
import com.kusithm.meetupd.domain.fileUpload.service.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/s3")
public class S3FileUploadController {

    private final AwsS3Service awsS3Service;

    @PostMapping("/upload")
    public FileUploadResponse uploadFile(
            @RequestPart(value = "file") List<MultipartFile> images) {
        return awsS3Service.uploadFile(images);
    }

}