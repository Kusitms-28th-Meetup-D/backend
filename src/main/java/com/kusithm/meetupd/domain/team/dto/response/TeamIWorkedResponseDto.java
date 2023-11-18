package com.kusithm.meetupd.domain.team.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

@Getter
@Builder
public class TeamIWorkedResponseDto {
    private String contestTitle;
    private String endDate;
    private int memberSize;
    private boolean isPossibleWriteReviews;
    private ResponseTeamMemeberDto leaderInfo;
    private List<ResponseTeamMemeberDto> teamMemeberInfos;

//    public static TeamIWorkedResponseDto of(String contestTitle){
//DateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
//    String formattedDate = sdFormat.format(team.getReviewDate());

//        return TeamIWorkedResponseDto.builder()
//                .contestTitle(contestTitle)
//                .endDate()
//                .memberSize()
//                .isPossibleWriteReviews()
//                .leaderInfo()
//                .teamMemeberInfos()
//                .build();
//    }
}
