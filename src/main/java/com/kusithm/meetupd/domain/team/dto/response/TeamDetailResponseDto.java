package com.kusithm.meetupd.domain.team.dto.response;

import com.kusithm.meetupd.domain.team.entity.Team;
import com.kusithm.meetupd.domain.user.entity.LocationType;
import com.kusithm.meetupd.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class TeamDetailResponseDto {

    private ResponseTeamMemeberDto leaderInfo;
    private String leaderMessage;
    private int max;//모집할 인원수
    private int cur;//합류한 인원수
    private String location;//활동지역
    private String endDate;//활동 종료 예정일
    private String notice;//모집공고
    private int leftMember;//남은 자리수 : 모집할 인원수 - 모집된 인원수
    private List<ResponseTeamMemeberDto> teamMemeberInfos;
    private int status; //나의 상태

    public static TeamDetailResponseDto of(Team team, User leader, List<User> teamMemeberInfos, int status) {

        DateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdFormat.format(team.getReviewDate());

        int max = team.getHeadCount();
        int cur = teamMemeberInfos.size();
        return TeamDetailResponseDto.builder()
                .leaderInfo(new ResponseTeamMemeberDto(leader))
                .leaderMessage(team.getLeaderMessage())
                .max(max)
                .cur(cur+1) //팀장 포함
                .location(LocationType.ofCode(team.getLocation().getLocationType()).getValue())
                .endDate(formattedDate)
                .notice(team.getNotice())
                .leftMember(max - cur -1)
                .teamMemeberInfos(teamMemeberInfos.stream().map(ResponseTeamMemeberDto::new).collect(Collectors.toList()))
                .status(status)
                .build();
    }
}
