package com.kusithm.meetupd.domain.team.service;

import com.kusithm.meetupd.domain.team.dto.request.PageDto;
import com.kusithm.meetupd.domain.team.dto.response.PageResponseDTO;
import com.kusithm.meetupd.domain.team.dto.response.RecruitingTeamResponseDto;
import com.kusithm.meetupd.domain.team.dto.response.TeamResponseDto;
import com.kusithm.meetupd.domain.team.entity.Team;
import com.kusithm.meetupd.domain.team.entity.TeamUser;
import com.kusithm.meetupd.domain.team.mysql.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.kusithm.meetupd.domain.team.entity.TeamUserRoleType.TEAM_LEADER;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamService {

    private final TeamRepository teamRepository;

    //진행상황에 맞는 팀 찾기
    public Page<Team> findTeamsCondition(PageDto dto, Integer teamProgress) {
        Pageable pageable = PageRequest.of(
                dto.getPage() - 1,
                dto.getSize(),
                Sort.by("createdDate").descending()
        );

        return teamRepository.findAllByProgress(teamProgress, pageable);
    }

    //모집중인 팀 찾기
    public TeamResponseDto findRecruitingTeams(Page<Team> allRecruitingTeams) {
        List<RecruitingTeamResponseDto> dto = new ArrayList<>();

        for (Team team : allRecruitingTeams.getContent()) {
            List<TeamUser> teamUsers = team.getTeamUsers();

            for (TeamUser teamuser : teamUsers) {
                if (teamuser.getRole().equals(TEAM_LEADER.getCode()))
                    dto.add(new RecruitingTeamResponseDto(team, teamuser.getUser()));
            }
        }

        return TeamResponseDto.ofCode(allRecruitingTeams,dto);
    }


}
