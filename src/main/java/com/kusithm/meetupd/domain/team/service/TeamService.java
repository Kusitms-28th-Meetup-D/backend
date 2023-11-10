package com.kusithm.meetupd.domain.team.service;

import com.kusithm.meetupd.common.error.EnumNotFoundException;
import com.kusithm.meetupd.domain.team.dto.response.PageDto;
import com.kusithm.meetupd.domain.team.dto.response.RecruitingTeamResponseDto;
import com.kusithm.meetupd.domain.team.entity.Team;
import com.kusithm.meetupd.domain.team.entity.TeamProgressType;
import com.kusithm.meetupd.domain.team.entity.TeamUser;
import com.kusithm.meetupd.domain.team.mysql.TeamRepository;
import com.kusithm.meetupd.domain.user.entity.User;
import com.kusithm.meetupd.domain.user.mysql.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.kusithm.meetupd.common.error.ErrorCode.ENUM_NOT_FOUND;
import static com.kusithm.meetupd.domain.team.entity.TeamProgressType.RECRUITING;
import static com.kusithm.meetupd.domain.team.entity.TeamUserRoleType.TEAM_LEADER;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamService {

    private final TeamRepository teamRepository;

    //진행상황에 맞는 팀 찾기
    public List<Team> findTeamsCondition(PageDto dto, Integer teamProgress) {
        Pageable pageable = PageRequest.of(
                dto.getPage() - 1,
                dto.getSize(),
                Sort.by("createdDate").descending()
        );

        return teamRepository.findAllByProgress(teamProgress, pageable).getContent();
    }

    //모집중인 팀 찾기
    public List<RecruitingTeamResponseDto> findRecruitingTeams(List<Team> allRecruitingTeams) {
        List<RecruitingTeamResponseDto> dto = new ArrayList<>();

        for (Team team : allRecruitingTeams) {
            List<TeamUser> teamUsers = team.getTeamUsers();

            for (TeamUser teamuser : teamUsers) {
                if (teamuser.getRole().equals(TEAM_LEADER.getCode()))
                    dto.add(new RecruitingTeamResponseDto(team, teamuser.getUser()));
            }
        }

        return dto;
    }


}
