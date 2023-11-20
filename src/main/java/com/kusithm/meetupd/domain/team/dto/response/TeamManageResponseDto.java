package com.kusithm.meetupd.domain.team.dto.response;

import com.kusithm.meetupd.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class TeamManageResponseDto {
    private int teamMemberSize;//합류한 팀원 수
    private List<ResponseTeamMemeberDto> teamMemberInfos;
    private int teamApplySize;//지원자 수
    private List<ResponseTeamMemeberDto> applyMemberInfos;

    public static TeamManageResponseDto of(List<User> teamMemberInfos, List<User> applyMemberInfos) {
        return TeamManageResponseDto.builder()
                .teamMemberSize(teamMemberInfos.size())
                .teamMemberInfos(teamMemberInfos.stream().map(ResponseTeamMemeberDto::new).collect(Collectors.toList()))
                .teamApplySize(applyMemberInfos.size())
                .applyMemberInfos(applyMemberInfos.stream().map(ResponseTeamMemeberDto::new).collect(Collectors.toList()))
                .build();
    }
}
