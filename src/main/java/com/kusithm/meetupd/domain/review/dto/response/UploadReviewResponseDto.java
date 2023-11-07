package com.kusithm.meetupd.domain.review.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UploadReviewResponseDto {

    private String uploadResultMessage;

    public static UploadReviewResponseDto of(String uploadResultMessage) {
        return UploadReviewResponseDto.builder()
                .uploadResultMessage(uploadResultMessage)
                .build();
    }
}
