package com.kusithm.meetupd.domain.team.dto.response;


import com.kusithm.meetupd.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseTeamMemeberDto {
    private Long teamMemberId;
    private String teamMemberName;
    private String teamMemberImage;
    private List<String> teamMemberTask;
    private List<String> teamMemberMajor;

    public ResponseTeamMemeberDto(User user) {
        this.teamMemberId = user.getId();
        this.teamMemberName = user.getUsername();
        this.teamMemberImage = user.getProfileImage();
        this.teamMemberTask = user.getTasks().stream().map(v -> v.getTask()).collect(Collectors.toList());
        this.teamMemberMajor = user.getMajors().stream().map(v -> v.getMajor()).collect(Collectors.toList());
    }
}
