package com.kusithm.meetupd.domain.team.service;

import com.kusithm.meetupd.common.error.ConflictException;
import com.kusithm.meetupd.common.error.EntityNotFoundException;
import com.kusithm.meetupd.domain.contest.entity.Contest;
import com.kusithm.meetupd.domain.contest.mongo.ContestRepository;
import com.kusithm.meetupd.domain.team.dto.TeamIOpenedResponseDto;
import com.kusithm.meetupd.domain.team.dto.request.PageDto;
import com.kusithm.meetupd.domain.team.dto.request.RequestChangeRoleDto;
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

import static com.kusithm.meetupd.common.error.ErrorCode.*;
import static com.kusithm.meetupd.domain.team.entity.TeamProgressType.RECRUITING;
import static com.kusithm.meetupd.domain.team.entity.TeamUserRoleType.*;
import static com.kusithm.meetupd.common.error.ErrorCode.*;
import static com.kusithm.meetupd.domain.team.entity.TeamUserRoleType.TEAM_LEADER;
import static com.kusithm.meetupd.domain.team.entity.TeamUserRoleType.TEAM_MEMBER;
import static com.kusithm.meetupd.domain.team.entity.TeamUserRoleType.*;
import static org.apache.logging.log4j.ThreadContext.isEmpty;

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
            User leader = teams.getTeamUsers().stream().filter(v -> v.getRole().equals(TEAM_LEADER.getCode())).map(TeamUser::getUser).findFirst().get();

            List<User> member = teams.getTeamUsers().stream().filter(v -> v.getRole().equals(TEAM_MEMBER.getCode())).map(TeamUser::getUser).collect(Collectors.toList());

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
        return contestRepository.findContestById(new ObjectId(contestId))
                .orElseThrow(() -> new EntityNotFoundException(CONTEST_NOT_FOUND));
    }

    //팀 상세조회
    public TeamDetailResponseDto findTeamDetail(Long userId, Long teamId) {
        Team team = findTeamById(teamId);
        User teamLeader = findTeamLeader(teamId);
        List<User> teamMember = findTeamMember(teamId);
        int status = decideStatus(userId, teamLeader.getId(), teamId);
        return TeamDetailResponseDto.of(team, teamLeader, teamMember, status);
    }

    private int decideStatus(Long userId, Long leaderId, Long teamId) {
        if (validateIsUserLeader(userId, leaderId)) {
            return 1;   //내가 오픈한 팀인 경우
        }
        Optional<TeamUser> teamUser = findTeamUserByUserIdAndTeamId(userId, teamId);
        if (validateUserInTeam(teamUser)) {
            return 2;   //내가 지원한 팀이 아닌경우
        }
        Integer userRole = getUserTeamRole(teamUser);
        if (userRole.equals(TEAM_MEMBER.getCode())) {
            return 3;   //승인
        }
        if (userRole.equals(FAILED.getCode())) {
            return 4;   //반려
        }
        if (userRole.equals(VOLUNTEER.getCode())) {
            return 5;   //승인,반려 둘 다 x
        }
        return 6;
    }

    private Boolean validateIsUserLeader(Long userId, Long leaderId) {
        return userId.equals(leaderId);
    }

    private Boolean validateUserInTeam(Optional<TeamUser> teamUser) {
        return teamUser.isEmpty();
    }

    private Integer getUserTeamRole(Optional<TeamUser> teamUser) {
        return teamUser.get().getRole();
    }

    private List<User> findTeamMember(Long teamId) {
        return findTeamUserByRoleAndTeamId(TEAM_MEMBER.getCode(), teamId).stream().map(TeamUser::getUser).collect(Collectors.toList());
    }

    private User findTeamLeader(Long teamId) {
        return findTeamUserByRoleAndTeamId(TEAM_LEADER.getCode(), teamId).stream().map(TeamUser::getUser).findFirst().orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));
    }

    public List<Team> findTeamByContentIdAndProgress(String contestId, Integer teamProgress) {
        return teamRepository.findAllByContestIdAndProgress(contestId, teamProgress);
    }

    private List<TeamUser> findTeamUserByRoleAndTeamId(Integer role, Long teamId) {
        return teamUserRepository.findAllByRoleAndTeamId(role, teamId);
    }

    public void openTeam(Long userId, RequestCreateTeamDto teamDto) {
        verifyCanOpenTeam(TEAM_LEADER.getCode(), userId);
        User user = findUserById(userId);
        Team team = saveTeam(teamDto);
        team.getLocation().changeUser(user);
        team.getLocation().changeTeam(team);
        saveTeamUser(TEAM_LEADER.getCode(), user, team);
    }

    private void verifyCanOpenTeam(Integer role, Long userId) {
        if (teamUserRepository.existsByRoleAndUserId(role, userId))
            throw new ConflictException(ALREADY_USER_OPEN_TEAM);
    }

    public void applyTeam(Long userId, Long teamId) {
        verifyAlreadyApplyThisTeam(userId, teamId);
        User user = findUserById(userId);
        Team team = findTeamById(teamId);
        saveTeamUser(VOLUNTEER.getCode(), user, team);
    }

    public void changeRole(Long userId, RequestChangeRoleDto requestChangeRoleDto) {
        Long teamLeaderId = findTeamLeader(requestChangeRoleDto.getTeamId()).getId();
        Long memberId = requestChangeRoleDto.getMemberId();
        if (userId.equals(teamLeaderId)) { //팀장이라면 권한 수정
            TeamUser teamUser = findTeamUserByUserIdAndTeamId(memberId, requestChangeRoleDto.getTeamId()).orElseThrow(() -> new EntityNotFoundException(TEAM_USER_NOT_FOUND));
            teamUser.setRole(requestChangeRoleDto.getRole());
        } else throw new ConflictException(USER_NOT_HAVE_AUTHORITY);
    }

    private Team findTeamById(Long teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException(TEAM_NOT_FOUND));
    }

    private TeamUser findTeamUserById(Long userId) {
        return teamUserRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(TEAM_USER_NOT_FOUND));
    }

    private Optional<TeamUser> findTeamUserByUserIdAndTeamId(Long userId, Long teamId) {
        return teamUserRepository.findByUserIdAndTeamId(userId, teamId);
    }


    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));
    }

    public Team saveTeam(RequestCreateTeamDto teamDto) {
        return teamRepository.save(teamDto.toEntity());
    }

    private TeamUser saveTeamUser(Integer role, User user, Team team) {
        return teamUserRepository.save(TeamUser.toEntity(role, team, user));
    }

    private void verifyAlreadyApplyThisTeam(Long userId, Long teamId) {
        if (teamUserRepository.existsByUserIdAndTeamId(userId, teamId))
            throw new ConflictException(ALREADY_USER_APPLY_TEAM);
    }

    public List<TeamIOpenedResponseDto> findTeamIOpen(Long userId) {
        List<TeamIOpenedResponseDto> dtos = new ArrayList<>();
        List<TeamUser> teamUsersIOpened = findTeamUserByUserIdAndRole(userId, TEAM_LEADER.getCode()); //내가 오픈한 팀을 찾고
        for (TeamUser teamUser : teamUsersIOpened) {
            Team team = teamUser.getTeam();
            if(compareTeamProgress(team,RECRUITING.getNumber())){
                List<TeamUser> teamMember = findTeamUserByTeamIdAndRole(team.getId(), TEAM_MEMBER.getCode());
                List<TeamUser> applyMember = findTeamUserByTeamIdAndRole(team.getId(), VOLUNTEER.getCode());
                Contest contest = findContest(team.getContestId());
                dtos.add(TeamIOpenedResponseDto.of(team.getId(), findUserThroughTeamUser(teamMember), findUserThroughTeamUser(applyMember), contest));
            }
        }
        return dtos;
    }

    private List<TeamUser> findTeamUserByTeamIdAndRole(Long teamId,Integer role) {
        return teamUserRepository.findAllByTeamIdAndRole(teamId,role);
    }

    private boolean compareTeamProgress(Team team,Integer teamProgress) {
        return team.getProgress().equals(teamProgress);
    }

    private List<Team> findTeamByProgress(Integer progress) {
        return teamRepository.findAllByProgress(progress);
    }

    public List<User> findUserThroughTeamUser(List<TeamUser> teamUsers) {
        return teamUsers.stream().map(v -> v.getUser()).collect(Collectors.toList());
    }

    private List<TeamUser> findTeamUserByUserIdAndRole(Long userId, Integer role) {
        return teamUserRepository.findAllByUserIdAndRole(userId, role).orElseThrow(() -> new EntityNotFoundException(TEAM_NOT_FOUND));
    }
}
