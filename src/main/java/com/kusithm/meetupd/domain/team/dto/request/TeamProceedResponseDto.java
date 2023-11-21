package com.kusithm.meetupd.domain.team.dto.request;


import com.kusithm.meetupd.domain.contest.entity.Contest;
import com.kusithm.meetupd.domain.team.dto.response.ResponseTeamMemeberDto;
import com.kusithm.meetupd.domain.team.dto.response.TeamIappliedResponseDto;
import com.kusithm.meetupd.domain.team.entity.Team;
import com.kusithm.meetupd.domain.user.entity.LocationType;
import com.kusithm.meetupd.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class TeamProceedResponseDto {

    public String contestId;
    public String contestTitle;
    public List<String> contestImage;
    public ResponseTeamMemeberDto leaderInfo;
    private String leaderMessage;
    public int memberSize;//팀원
    public String location;//활동지역
    public String endDate;//활동 종료 예정일
    public String notice;//모집공고
    public List<ResponseTeamMemeberDto> teamMemberInfos;
    public String chatLink;

    public static TeamProceedResponseDto of(Team team, Contest contest, User leaderInfo, List<User> teamMemberInfos) {

        return TeamProceedResponseDto.builder()
                .contestId(contest.getId())
                .contestTitle(contest.getTitle())
                .contestImage(contest.getContestImages())
                .leaderInfo(new ResponseTeamMemeberDto(leaderInfo))
                .leaderMessage(team.getLeaderMessage())
                .memberSize(teamMemberInfos.size())
                .location(LocationType.ofCode(team.getLocation().getLocationType()).getValue())
                .endDate(TeamIappliedResponseDto.formatDate(team.getReviewDate()))
                .notice(team.getNotice())
                .teamMemberInfos(teamMemberInfos.stream().map(ResponseTeamMemeberDto::new).collect(Collectors.toList()))
                .chatLink(team.getChatLink())
                .build();
    }

}
