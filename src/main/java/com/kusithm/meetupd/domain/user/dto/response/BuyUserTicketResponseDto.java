package com.kusithm.meetupd.domain.user.dto.response;


import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BuyUserTicketResponseDto {

    private Integer ticketCount;

    public static BuyUserTicketResponseDto of(Integer ticketCount) {
        return BuyUserTicketResponseDto.builder()
                .ticketCount(ticketCount)
                .build();
    }
}
