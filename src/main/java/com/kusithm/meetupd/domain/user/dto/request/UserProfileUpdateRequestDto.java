package com.kusithm.meetupd.domain.user.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class UserProfileUpdateRequestDto {

    private List<String> internships;

    private List<String> awards;

    private List<String> tools;

    private List<String> certificates;

}
