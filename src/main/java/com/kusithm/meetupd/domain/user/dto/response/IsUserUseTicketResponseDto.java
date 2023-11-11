package com.kusithm.meetupd.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class IsUserUseTicketResponseDto {

    private Boolean isUsed;

    public static IsUserUseTicketResponseDto of(Boolean isUsed) {
        return IsUserUseTicketResponseDto.builder()
                .isUsed(isUsed)
                .build();
    }
}

