package com.kusithm.meetupd.common.config;

import com.kusithm.meetupd.MeetupDApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@EnableFeignClients(basePackageClasses = MeetupDApplication.class)
@Configuration
public class FeignClientConfig {
}
