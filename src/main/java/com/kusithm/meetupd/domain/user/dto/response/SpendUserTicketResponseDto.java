package com.kusithm.meetupd.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SpendUserTicketResponseDto {

    private Integer remainTicket;

    public static SpendUserTicketResponseDto of(Integer remainTicket) {
        return SpendUserTicketResponseDto.builder()
                .remainTicket(remainTicket)
                .build();
    }
}
