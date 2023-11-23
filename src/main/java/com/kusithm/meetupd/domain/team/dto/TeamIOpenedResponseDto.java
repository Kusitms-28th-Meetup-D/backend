package com.kusithm.meetupd.domain.team.dto;

import com.kusithm.meetupd.domain.contest.entity.Contest;
import com.kusithm.meetupd.domain.team.dto.response.ResponseTeamMemeberDto;
import com.kusithm.meetupd.domain.team.entity.Team;
import com.kusithm.meetupd.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class TeamIOpenedResponseDto {
    private Long teamId;
    private int teamMemberSize;
    private int applyMemberSize;
    private List<ResponseTeamMemeberDto> teamMemberInfos;
    private List<ResponseTeamMemeberDto> applyMemberInfos;
    private String contestId;
    private String contestTitle;
    private List<String> contestImage;

    public static TeamIOpenedResponseDto of(Team team, List<User> teamMemberInfos, List<User> applyMemberInfos, Contest contest) {
        return TeamIOpenedResponseDto.builder()
                .teamId(team.getId())
                .teamMemberSize(teamMemberInfos.size())
                .applyMemberSize(applyMemberInfos.size())
                .teamMemberInfos(teamMemberInfos.stream().map(user -> new ResponseTeamMemeberDto(user)).collect(Collectors.toList()))
                .applyMemberInfos(applyMemberInfos.stream().map(user -> new ResponseTeamMemeberDto(user)).collect(Collectors.toList()))
                .contestId(contest.getId())
                .contestTitle(contest.getTitle())
                .contestImage(contest.getContestImages())
                .build();
    }
}
