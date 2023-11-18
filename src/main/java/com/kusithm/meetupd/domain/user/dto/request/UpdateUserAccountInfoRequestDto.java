package com.kusithm.meetupd.domain.user.dto.request;

import lombok.Getter;

@Getter
public class UpdateUserAccountInfoRequestDto {

    private String username;

    private Integer location;

    private String major;

    private String task;

    private String selfIntroduce;
}
