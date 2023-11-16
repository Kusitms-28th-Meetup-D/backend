package com.kusithm.meetupd.domain.team.dto.request;

import lombok.Getter;

@Getter
public class RequestChangeRoleDto {
    public Long teamId;
    public Long memberId;
    public Integer role;
}
