package com.kusithm.meetupd.domain.fileUpload.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.kusithm.meetupd.common.error.ErrorCode;
import com.kusithm.meetupd.common.error.FileUploadFailedException;
import com.kusithm.meetupd.domain.fileUpload.dto.response.FileUploadResponse;
import com.kusithm.meetupd.domain.fileUpload.entity.Image;
import com.kusithm.meetupd.domain.fileUpload.mysql.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.kusithm.meetupd.common.error.ErrorCode.FILE_UPLOAD_FAILED;

@Slf4j
@RequiredArgsConstructor
@Service
public class AwsS3Service {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;
    private final ImageRepository imageRepository;

    public FileUploadResponse uploadFile(List<MultipartFile> images) {

        List<String> fileUrls = new ArrayList<>();
        for (MultipartFile image : images) {
            String fileName = image.getOriginalFilename();

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(image.getContentType());

            try (InputStream inputStream = image.getInputStream()) {
                amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));

                String imagePath = amazonS3Client.getUrl(bucketName, fileName).toString(); // 접근가능한 URL 가져오기
                fileUrls.add(imagePath);
                imageRepository.save(new Image(imagePath));

            } catch (IOException e) {
                throw new FileUploadFailedException(FILE_UPLOAD_FAILED);
            }
        }

        return new FileUploadResponse(fileUrls);
    }
}