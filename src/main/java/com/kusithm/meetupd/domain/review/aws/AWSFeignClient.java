package com.kusithm.meetupd.domain.review.aws;

import com.kusithm.meetupd.domain.review.aws.dto.request.SendEmailByAWSFeignRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "aws-feign", url = "https://dop0og42sl.execute-api.us-east-2.amazonaws.com")
public interface AWSFeignClient {

    @PostMapping("/prod/send-email")
    public void sendReviewEmail(@RequestBody SendEmailByAWSFeignRequestDto request);

}
