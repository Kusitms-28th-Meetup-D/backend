package com.kusithm.meetupd.domain.review.aws.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SendEmailByAWSFeignRequestDto {

    private String sendEmail;

    private String teamName;

    public static SendEmailByAWSFeignRequestDto of(String sendEmail, String teamName) {
        return SendEmailByAWSFeignRequestDto.builder()
                .sendEmail(sendEmail)
                .teamName(teamName)
                .build();
    }
}
