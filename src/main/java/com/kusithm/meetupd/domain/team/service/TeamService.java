package com.kusithm.meetupd.domain.team.service;

import com.kusithm.meetupd.common.error.ConflictException;
import com.kusithm.meetupd.common.error.EntityNotFoundException;
import com.kusithm.meetupd.domain.contest.entity.Contest;
import com.kusithm.meetupd.domain.contest.mongo.ContestRepository;
import com.kusithm.meetupd.domain.team.dto.request.PageDto;
import com.kusithm.meetupd.domain.team.dto.request.RequestCreateTeamDto;
import com.kusithm.meetupd.domain.team.dto.response.*;
import com.kusithm.meetupd.domain.team.entity.Team;
import com.kusithm.meetupd.domain.team.entity.TeamUser;
import com.kusithm.meetupd.domain.team.mysql.TeamRepository;
import com.kusithm.meetupd.domain.team.mysql.TeamUserRepository;
import com.kusithm.meetupd.domain.user.entity.User;
import com.kusithm.meetupd.domain.user.mysql.UserRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.kusithm.meetupd.common.error.ErrorCode.ALREADY_USER_OPEN_TEAM;
import static com.kusithm.meetupd.common.error.ErrorCode.USER_NOT_FOUND;
import static com.kusithm.meetupd.domain.team.entity.TeamUserRoleType.TEAM_LEADER;
import static com.kusithm.meetupd.domain.team.entity.TeamUserRoleType.TEAM_MEMBER;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamUserRepository teamUserRepository;
    private final UserRepository userRepository;
    private final ContestRepository contestRepository;

    //진행상황에 맞는 팀 찾기
    public Page<Team> findTeamsCondition(PageDto dto, Integer teamProgress) {
        Pageable pageable = PageRequest.of(
                dto.getPage() - 1,
                dto.getSize(),
                Sort.by("createdDate").descending()
        );

        return teamRepository.findAllByProgress(teamProgress, pageable);
    }

    //해당 공모전에서 모집중인 팀 리스트
    public List<RecruitingContestTeamResponseDto> findContestRecruitingTeams(String contestId, Integer teamProgress) {

        List<RecruitingContestTeamResponseDto> dto = new ArrayList<>();

        List<Team> teamByContentIdAndProgress = findTeamByContentIdAndProgress(contestId, teamProgress);

        for (Team teams : teamByContentIdAndProgress) {
            User leader = teams.getTeamUsers().stream().filter(v -> v.getRole().equals(TEAM_LEADER.getCode())).map(teamUser -> teamUser.getUser()).findFirst().get();

            List<User> member = teams.getTeamUsers().stream().filter(v -> v.getRole().equals(TEAM_MEMBER.getCode())).map(teamUser -> teamUser.getUser()).collect(Collectors.toList());

            dto.add(RecruitingContestTeamResponseDto.of(teams, leader, member));
        }
        return dto;
    }


    //모집중인 팀 찾기
    public TeamResponseDto findRecruitingTeams(Page<Team> allRecruitingTeams) {
        List<RecruitingTeamResponseDto> dto = new ArrayList<>();
        for (Team team : allRecruitingTeams.getContent()) {
            List<TeamUser> teamUsers = team.getTeamUsers();
            Contest contest = findContest(team.getContestId());

            for (TeamUser teamuser : teamUsers) {
                if (teamuser.getRole().equals(TEAM_LEADER.getCode())) {
                    dto.add(new RecruitingTeamResponseDto(contest, team, teamuser.getUser()));
                }
            }
        }
        return TeamResponseDto.ofCode(allRecruitingTeams, dto);
    }

    private Contest findContest(String contestId) {
        return contestRepository.findContestById(new ObjectId(contestId));
    }

    public List<Team> findTeamByContentIdAndProgress(String contestId, Integer teamProgress) {
        return teamRepository.findAllByContestIdAndProgress(contestId, teamProgress);
    }


    //팀 상세조회 - 기획팀 중단 요청
    public void findTeamDetail(long teamId) {
        Optional<Team> team = teamRepository.findById(teamId);

    }
    public void openTeam(Long userId, RequestCreateTeamDto teamDto) {
        verifyCanOpenTeam(TEAM_LEADER.getCode(),userId);
        User user = findUserById(userId);
        Team team = saveTeam(teamDto);
        team.getLocation().changeUser(user);
        team.getLocation().changeTeam(team);
        saveTeamUser(TEAM_LEADER.getCode(),user,team);
    }

    private void verifyCanOpenTeam(Integer role, Long userId) {
        if(teamUserRepository.existsByRoleAndUserId(role,userId))
            throw new ConflictException(ALREADY_USER_OPEN_TEAM);
    }

    public User findUserById(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));
    }

    public Team saveTeam(RequestCreateTeamDto teamDto) {
        return teamRepository.save(teamDto.toEntity());
    }
    private TeamUser saveTeamUser(Integer role,User user, Team team) {
        return teamUserRepository.save(TeamUser.toEntity(role,team,user));
    }

}
