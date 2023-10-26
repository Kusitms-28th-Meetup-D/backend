package com.kusithm.meetupd.domain.auth.dto.request;

import lombok.Getter;

@Getter
public class ReissueRequestDto {

    private String accessToken;

    private String refreshToken;
}
