package com.kusithm.meetupd.domain.team.dto.response;

import com.kusithm.meetupd.domain.team.entity.Team;
import com.kusithm.meetupd.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;

@Builder
@Getter
public class RecruitingContestTeamResponseDto {

    private int max;//모집할 인원수
    private int cur;//합류한 인원수
    private String leaderMessage;
    private int leftMember;//남은 자리수 : 모집할 인원수 - 모집된 인원수
    private ResponseTeamMemeberDto leaderInfo;
    private List<ResponseTeamMemeberDto> teamMemeberInfos;

    public static RecruitingContestTeamResponseDto of(Team team, User leader, List<User> teamMemeberInfos) {
        int max = team.getHeadCount();
        int cur = team.getTeamUsers().size();
        return RecruitingContestTeamResponseDto.builder()
                .max(max)
                .cur(team.getTeamUsers().size())
                .leaderMessage(team.getLeaderMessage())
                .leftMember(max - cur)
                .leaderInfo(new ResponseTeamMemeberDto(leader))
                .teamMemeberInfos(teamMemeberInfos.stream().map(user -> new ResponseTeamMemeberDto(user)).collect(Collectors.toList()))
                .build();
    }

}
