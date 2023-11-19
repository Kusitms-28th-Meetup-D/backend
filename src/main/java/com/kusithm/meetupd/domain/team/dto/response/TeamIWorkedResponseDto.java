package com.kusithm.meetupd.domain.team.dto.response;

import com.kusithm.meetupd.domain.contest.entity.Contest;
import com.kusithm.meetupd.domain.team.entity.Team;
import com.kusithm.meetupd.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class TeamIWorkedResponseDto {
    private String contestId;
    private String contestTitle;
    private String endDate;
    private int memberSize;
    private boolean isPossibleWriteReviews;
    private ResponseTeamMemeberDto leaderInfo;
    private List<ResponseTeamMemeberDto> teamMemeberInfos;

    public static TeamIWorkedResponseDto of(Team team, Contest contest, User leader, List<User> teamMember, Boolean isUserUploadReview) {
        DateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdFormat.format(team.getReviewDate());

        return TeamIWorkedResponseDto.builder()
                .contestId(contest.getId())
                .contestTitle(contest.getTitle())
                .endDate(formattedDate)
                .memberSize(teamMember.size())
                .isPossibleWriteReviews(!isUserUploadReview)
                .leaderInfo(new ResponseTeamMemeberDto(leader))
                .teamMemeberInfos(teamMember.stream().map(user -> new ResponseTeamMemeberDto(user)).collect(Collectors.toList()))
                .build();
    }
}
