package com.kusithm.meetupd.domain.team.dto.response;

import com.kusithm.meetupd.domain.team.entity.Team;
import com.kusithm.meetupd.domain.user.entity.LocationType;
import com.kusithm.meetupd.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class TeamDetailResponseDto {

    public ResponseTeamMemeberDto leaderInfo;
    private String leaderMessage;
    public int max;//모집할 인원수
    public int cur;//합류한 인원수
    public String location;//활동지역
    public String endDate;//활동 종료 예정일
    public String notice;//모집공고
    public int leftMember;//남은 자리수 : 모집할 인원수 - 모집된 인원수
    public List<ResponseTeamMemeberDto> teamMemeberInfos;
    public int status; //나의 상태

    public static TeamDetailResponseDto of(Team team, User leader, List<User> teamMemeberInfos,int status) {
        int max = team.getHeadCount();
        int cur = team.getTeamUsers().size();
        return TeamDetailResponseDto.builder()
                .leaderInfo(new ResponseTeamMemeberDto(leader))
                .leaderMessage(team.getLeaderMessage())
                .max(max)
                .cur(cur)
                .location(LocationType.ofCode(team.getLocation().getLocationType()).getValue())
                .endDate(String.valueOf(team.getReviewDate()))
                .notice(team.getNotice())
                .leftMember(max-cur)
                .teamMemeberInfos(teamMemeberInfos.stream().map(ResponseTeamMemeberDto::new).collect(Collectors.toList()))
                .status(status)
                .build();
    }
}
