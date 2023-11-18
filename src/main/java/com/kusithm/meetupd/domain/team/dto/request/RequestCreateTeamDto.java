package com.kusithm.meetupd.domain.team.dto.request;

import com.kusithm.meetupd.domain.team.entity.Team;
import com.kusithm.meetupd.domain.team.entity.TeamLocation;
import lombok.Getter;

import java.util.Date;

import static com.kusithm.meetupd.domain.team.entity.TeamProgressType.RECRUITING;

@Getter
public class RequestCreateTeamDto {

    public String contestId;

    public int max;

    public Integer location;

    public Date endDate;

    public String leaderMessage;

    public String notice;

    public String chatLink;

    public Team toEntity() {
        return Team.builder()
                .headCount(max)
                .location(TeamLocation.createLocation(location))
                .reviewDate(endDate)
                .chatLink(chatLink)
                .leaderMessage(leaderMessage)
                .progress(RECRUITING.getNumber())
                .contestId(contestId)
                .notice(notice)
                .build();
    }
}
