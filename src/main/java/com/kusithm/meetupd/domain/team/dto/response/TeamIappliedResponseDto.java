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
import java.util.Date;
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


    public static TeamIappliedResponseDto of(Team team, Contest contest, User leader, Integer status) {


        String userRole = TeamUserRoleType.ofCode(status).getValue();
        return TeamIappliedResponseDto.builder()
                .contestId(contest.getId())
                .contestTitle(contest.getTitle())
                .contestImage(contest.getContestImages())
                .leaderInfo(new ResponseTeamMemeberDto(leader))
                .teamId(team.getId())
                .status(manufactureStatus(status))
                .leaderMessage(team.getLeaderMessage())
                .max(team.getHeadCount())
                .cur(team.getTeamUsers().size())
                .location(LocationType.ofCode(team.getLocation().getLocationType()).getValue())
                .endDate(formatDate(team.getReviewDate()))
                .build();

    }

    public static String formatDate(Date date){
        DateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
        return sdFormat.format(date);
    }

    public static String manufactureStatus(Integer status) {
        return switch (status) {
            case 2 -> "합류";
            case 3 -> "반려";
            case 4 -> "검토 중";
            default -> null;
        };
    }
}
