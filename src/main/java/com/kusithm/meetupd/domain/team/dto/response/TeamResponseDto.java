package com.kusithm.meetupd.domain.team.dto.response;

import com.kusithm.meetupd.domain.team.entity.Team;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamResponseDto {

    private PageResponseDTO pageResponseDTO;

    private List<RecruitingTeamResponseDto> recruitingTeams;

    public static TeamResponseDto ofCode(Page<Team> allRecruitingTeams, List<RecruitingTeamResponseDto> dto){
        return TeamResponseDto.builder()
                .pageResponseDTO(new PageResponseDTO(allRecruitingTeams))
                .recruitingTeams(dto)
                .build();
    }
}
