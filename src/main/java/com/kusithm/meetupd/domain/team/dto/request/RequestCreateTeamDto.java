package com.kusithm.meetupd.domain.team.dto.request;

import com.kusithm.meetupd.domain.team.entity.Team;
import com.kusithm.meetupd.domain.team.entity.TeamProgressType;
import com.kusithm.meetupd.domain.team.entity.TeamUser;
import com.kusithm.meetupd.domain.user.entity.Location;
import com.kusithm.meetupd.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

import static com.kusithm.meetupd.domain.team.entity.TeamProgressType.RECRUITING;

@Getter
public class RequestCreateTeamDto {

    public Long userId;

    public String contestId;

    public int headCount;

    public Integer location;

    public Date endDate;

    public String leaderMessage;

    public String notice;

    public String chatLink;

    public Team toEntity() {
        return Team.builder()
                .headCount(headCount)
                .location(Location.craeteLocation(location))
                .reviewDate(endDate)
                .chatLink(chatLink)
                .leaderMessage(leaderMessage)
                .progress(RECRUITING.getNumber())
                .contestId(contestId)
                .notice(notice)
                .build();
    }
}
