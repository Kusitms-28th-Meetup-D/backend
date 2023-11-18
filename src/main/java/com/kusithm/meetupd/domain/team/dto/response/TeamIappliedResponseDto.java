package com.kusithm.meetupd.domain.team.dto.response;

import com.kusithm.meetupd.domain.contest.entity.Contest;
import com.kusithm.meetupd.domain.team.entity.Team;
import com.kusithm.meetupd.domain.team.entity.TeamUserRoleType;
import com.kusithm.meetupd.domain.user.entity.LocationType;
import com.kusithm.meetupd.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Builder
public class TeamIappliedResponseDto {

    private String contestId;
    private String contestTitle;
    private List<String> contestImage;

    public ResponseTeamMemeberDto leaderInfo;
    public Long teamId;
    public String status;
    public String leaderMessage;
    public int max;//모집할 인원수
    public int cur;//합류한 인원수
    public String location;//활동지역
    public String endDate;//활동 종료 예정일


    public static TeamIappliedResponseDto of(Contest contest, User leader, Team team, Integer status){

        DateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedEndDate = sdFormat.format(team.getReviewDate());

        return TeamIappliedResponseDto.builder()
                .contestId(contest.getId())
                .contestTitle(contest.getTitle())
                .contestImage(contest.getContestImages())
                .leaderInfo(new ResponseTeamMemeberDto(leader))
                .teamId(team.getId())
                .status(TeamUserRoleType.ofCode(status).getValue())
                .leaderMessage(team.getLeaderMessage())
                .max(team.getHeadCount())
                .cur(team.getTeamUsers().size())
                .location(LocationType.ofCode(team.getLocation().getLocationType()).getValue())
                .endDate(formattedEndDate)
                .build();
    }
}
