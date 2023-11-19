package com.kusithm.meetupd.domain.team.dto.request;

import lombok.Getter;

@Getter
public class RequestChangeRoleDto {
    private Long teamId;
    private Long memberId;
    private Integer role;
}
