package com.kusithm.meetupd.domain.team.dto;

import com.kusithm.meetupd.domain.contest.entity.Contest;
import com.kusithm.meetupd.domain.team.dto.response.ResponseTeamMemeberDto;
import com.kusithm.meetupd.domain.team.dto.response.TeamDetailResponseDto;
import com.kusithm.meetupd.domain.team.entity.Team;
import com.kusithm.meetupd.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class TeamIOpenedResponseDto {
    Long teamId;
    int teamMemberSize;
    int applyMemberSize;
    List<ResponseTeamMemeberDto> teamMemberInfos;
    List<ResponseTeamMemeberDto> applyMemberInfos;
    String contestTitle;
    List<String> contestImage;

    public static TeamIOpenedResponseDto of(Long teamId, List<User> teamMemberInfos, List<User> applyMemberInfos, Contest contest) {
        return TeamIOpenedResponseDto.builder()
                .teamId(teamId)
                .teamMemberSize(teamMemberInfos.size())
                .applyMemberSize(applyMemberInfos.size())
                .teamMemberInfos(teamMemberInfos.stream().map(user -> new ResponseTeamMemeberDto(user)).collect(Collectors.toList()))
                .applyMemberInfos(applyMemberInfos.stream().map(user -> new ResponseTeamMemeberDto(user)).collect(Collectors.toList()))
                .contestTitle(contest.getTitle())
                .contestImage(contest.getContestImages())
                .build();
    }
}
