package com.kusithm.meetupd.domain.team.dto.response;

import com.kusithm.meetupd.domain.contest.entity.Contest;
import com.kusithm.meetupd.domain.team.entity.Team;
import com.kusithm.meetupd.domain.user.entity.User;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecruitingTeamResponseDto {

    //공모전Id,공모전이름,팀장Id,팀장이름,팀장프로필사진,팀장의 한마디
    private String contestId;

    private String contesttitle; //공모전 이름

    private Long teamId;

    private Long teamLeaderId;

    private String teamLeaderName;

    private String teamLeaderImage;

    private String teamLeaderMessage;

    public RecruitingTeamResponseDto(Contest contest, Team team, User user) {

        this.contestId = contest.getId();

        this.contesttitle = contest.getTitle();

        this.teamId = team.getId();

        this.teamLeaderId = user.getId();

        this.teamLeaderName = user.getUsername();

        this.teamLeaderImage = user.getProfileImage();

        this.teamLeaderMessage = team.getLeaderMessage();

    }

}
