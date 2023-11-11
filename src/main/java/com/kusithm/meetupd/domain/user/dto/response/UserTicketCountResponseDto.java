package com.kusithm.meetupd.domain.user.dto.response;


import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserTicketCountResponseDto {

    private Integer ticketCount;

    public static UserTicketCountResponseDto of(Integer ticketCount) {
        return UserTicketCountResponseDto.builder()
                .ticketCount(ticketCount)
                .build();
    }
}
