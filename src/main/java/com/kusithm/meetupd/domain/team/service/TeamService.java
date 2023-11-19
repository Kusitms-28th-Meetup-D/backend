package com.kusithm.meetupd.domain.team.service;

import com.kusithm.meetupd.common.error.ConflictException;
import com.kusithm.meetupd.common.error.EntityNotFoundException;
import com.kusithm.meetupd.common.error.ForbiddenException;
import com.kusithm.meetupd.domain.contest.entity.Contest;
import com.kusithm.meetupd.domain.contest.mongo.ContestRepository;
import com.kusithm.meetupd.domain.email.service.EmailService;
import com.kusithm.meetupd.domain.review.mongo.WaitReviewRepository;
import com.kusithm.meetupd.domain.review.mysql.UserReviewedTeamRepository;
import com.kusithm.meetupd.domain.team.dto.TeamIOpenedResponseDto;
import com.kusithm.meetupd.domain.team.dto.request.PageDto;
import com.kusithm.meetupd.domain.team.dto.request.RequestChangeRoleDto;
import com.kusithm.meetupd.domain.team.dto.request.RequestCreateTeamDto;
import com.kusithm.meetupd.domain.team.dto.request.TeamProceedResponseDto;
import com.kusithm.meetupd.domain.team.dto.response.*;
import com.kusithm.meetupd.domain.team.entity.Team;
import com.kusithm.meetupd.domain.team.entity.TeamUser;
import com.kusithm.meetupd.domain.team.mysql.TeamRepository;
import com.kusithm.meetupd.domain.team.mysql.TeamUserRepository;
import com.kusithm.meetupd.domain.user.entity.User;
import com.kusithm.meetupd.domain.user.mysql.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;

import static com.kusithm.meetupd.common.error.ErrorCode.*;
import static com.kusithm.meetupd.domain.team.entity.TeamProgressType.*;
import static com.kusithm.meetupd.domain.team.entity.TeamProgressType.PROCEEDING;
import static com.kusithm.meetupd.domain.team.entity.TeamProgressType.RECRUITING;
import static com.kusithm.meetupd.domain.team.entity.TeamUserRoleType.*;
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
    private final MongoTemplate mongoTemplate;
    private final EmailService emailService;
    private final UserReviewedTeamRepository userReviewedTeamRepository;

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
        return TeamDetailResponseDto.of(team, findTeamLeader(teamId), teamMember, status);
    }

    public void updateTeamProgressProceeding(String contestId) {
        List<Team> teams = findTeamByContentIdAndProgress(contestId, RECRUITMENT_COMPLETED.getNumber());
        teams.forEach(team -> team.updateProgress(PROCEEDING));
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
        return findTeamUserByRoleAndTeamId(TEAM_LEADER.getCode(), teamId).stream().map(TeamUser::getUser).findFirst()
                .orElseThrow(() -> new ForbiddenException(USER_NOT_TEAMLEADER));
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
        team.getLocation().changeTeam(team);
        Query query = createFindContestByIdQuery(team.getContestId());
        Update update = new Update();
        increaseContestRecruitTeamNumberInUpdate(update);
        saveTeamUser(TEAM_LEADER.getCode(), user, team);
        mongoTemplate.updateMulti(query, update, Contest.class);
    }

    private void verifyCanOpenTeam(Integer role, Long userId) {
        if (teamUserRepository.existsByRoleAndUserId(role, userId))
            throw new ConflictException(ALREADY_USER_OPEN_TEAM);
    }

    public void applyTeam(Long userId, Long teamId) {
        verifyAlreadyApplyThisTeam(userId, teamId);
        saveTeamUser(VOLUNTEER.getCode(), findUserById(userId), findTeamById(teamId));
    }

    public void changeRole(Long userId, RequestChangeRoleDto requestChangeRoleDto) throws MessagingException, UnsupportedEncodingException {
        Long teamLeaderId = findTeamLeader(requestChangeRoleDto.getTeamId()).getId();
        Long memberId = requestChangeRoleDto.getMemberId();
        if (userId.equals(teamLeaderId)) { //팀장이라면 권한 수정
            TeamUser teamUser = findTeamUserByUserIdAndTeamId(memberId, requestChangeRoleDto.getTeamId()).orElseThrow(() -> new EntityNotFoundException(TEAM_USER_NOT_FOUND));
            validateTeamProgressRecruiting(teamUser.getTeam());
            validateUserRoleIsVolunteer(teamUser);
            teamUser.setRole(requestChangeRoleDto.getRole());
        } else throw new ConflictException(USER_NOT_HAVE_AUTHORITY);
        if (requestChangeRoleDto.getRole().equals(TEAM_MEMBER.getCode())) {
            Team team = findTeamById(requestChangeRoleDto.getTeamId());
            User user = findUserById(memberId);
            Contest contest = findContest(team.getContestId());
            emailService.sendJoinTeamEmail(user.getEmail(), contest.getTitle(), team.getChatLink());
            List<TeamUser> teamMembers = findTeamUserByTeamIdAndRole(requestChangeRoleDto.getTeamId(), TEAM_MEMBER.getCode());
            if (teamMembers.size() == team.getHeadCount()) {
                team.updateProgress(RECRUITMENT_COMPLETED);
                contestTeamCountDecrease(team);
            }
        }
    }


    public void deleteTeam(Long userId, Long teamId) {
        TeamUser teamUser = findTeamUserByUserIdAndTeamId(userId, teamId)
                .orElseThrow(() -> new EntityNotFoundException(TEAM_USER_NOT_FOUND));
        if (!teamUser.equals(TEAM_LEADER.getCode())) {
            throw new ForbiddenException(USER_NOT_TEAMLEADER);
        }
        Team team = findTeamById(teamId);
        teamRepository.delete(team);
    }

    public void updateTeamProgressRecruitment(Long userId, Long teamId) {
        TeamUser teamUser = findTeamUserByUserIdAndTeamId(userId, teamId)
                .orElseThrow(() -> new EntityNotFoundException(TEAM_USER_NOT_FOUND));
        if (!teamUser.equals(TEAM_LEADER.getCode())) {
            throw new ForbiddenException(USER_NOT_TEAMLEADER);
        }
        Team team = findTeamById(teamId);
        validateTeamProgressRecruiting(team);
        team.updateProgress(RECRUITMENT_COMPLETED);
        contestTeamCountDecrease(team);
    }

    public void cancelApplyTeam(Long userId, Long teamId) {
        TeamUser teamUser = findTeamUserByUserIdAndTeamId(userId, teamId)
                .orElseThrow(() -> new EntityNotFoundException(TEAM_USER_NOT_FOUND));
        if (!teamUser.equals(VOLUNTEER.getCode())) {
            throw new ForbiddenException(USER_NOT_APPLY_STATUS);
        }
        teamUserRepository.delete(teamUser);
    }

    public List<TeamIOpenedResponseDto> findTeamIOpen(Long userId) {
        List<TeamIOpenedResponseDto> dtos = new ArrayList<>();
        Integer role = TEAM_LEADER.getCode();
        List<TeamUser> teamUsersIOpened = findTeamUserByUserIdAndRole(userId, role);//내가 오픈한 팀을 찾고
        for (TeamUser teamUser : teamUsersIOpened) {
            Team team = findTeamUserByTeam(teamUser);
            Long teamId = team.getId();
            if (compareTeamProgress(team, RECRUITING.getNumber())) {
                List<TeamUser> teamMember = findTeamUserByTeamIdAndRole(teamId, TEAM_MEMBER.getCode());
                List<TeamUser> applyMember = findTeamUserByTeamIdAndRole(teamId, VOLUNTEER.getCode());
                dtos.add(TeamIOpenedResponseDto.of(team, findUserThroughTeamUser(teamMember), findUserThroughTeamUser(applyMember), findContest(team.getContestId())));
            }
        }
        return dtos;
    }

    public List<TeamIappliedResponseDto> appliedTeam(Long userId) {
        List<TeamIappliedResponseDto> dtos = new ArrayList<>();
        List<TeamUser> appliedReamUsers = findTeamUserIApplied(userId, TEAM_MEMBER.getCode());
        for (TeamUser teamUser : appliedReamUsers) {
            Team team = findTeamUserByTeam(teamUser);
            if (team.getProgress().equals(RECRUITING.getNumber())) {
                dtos.add(TeamIappliedResponseDto.of(team, findContest(team.getContestId()), findTeamLeader(team.getId()), teamUser.getRole()));
            }
        }
        return dtos;
    }

    public List<TeamProceedResponseDto> proceedTeam(Long userId) {
        List<TeamProceedResponseDto> dtos = new ArrayList<>();
        List<TeamUser> teamUserProceed = findTeamMemberAndTeamUser(userId);
        for (TeamUser teamUser : teamUserProceed) {
            Team team = findTeamUserByTeam(teamUser);
            if (team.getProgress().equals(PROCEEDING.getNumber())) {
                Long teamId = team.getId();
                dtos.add(TeamProceedResponseDto.of(team, findContest(team.getContestId()), findTeamLeader(teamId), findTeamMember(teamId)));
            }
        }
        return dtos;
    }

    public List<TeamIWorkedResponseDto> workedTeam(Long userId) {
        List<TeamIWorkedResponseDto> dtos = new ArrayList<>();
        List<TeamUser> teamMemberAndTeamUser = findTeamMemberAndTeamUser(userId);
        for (TeamUser teamUser : teamMemberAndTeamUser) {
            Team team = findTeamUserByTeam(teamUser);
            if (team.getProgress().equals(PROGRESS_ENDED.getNumber())) {
                Long teamId = team.getId();
                dtos.add(TeamIWorkedResponseDto.of(team, findContest(team.getContestId()), findTeamLeader(teamId), findTeamMember(teamId), checkUserReviewThisTeam(userId, teamId)));
            }
        }
        return dtos;
    }

    private List<TeamUser> findTeamMemberAndTeamUser(Long userId) {
        return findTeamUserLessThan(userId, TEAM_MEMBER.getCode());
    }

    private Team findTeamUserByTeam(TeamUser teamUser) {
        return teamUser.getTeam();
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

    public List<Team> updateTeamProgressEnd() throws MessagingException, UnsupportedEncodingException {
        List<Team> teams = getReviewDateAfterTeam(createTodayDateTimeEnd());
        for (Team team : teams) {
            team.updateProgress(PROGRESS_ENDED);
            sendTeamEndReview(team);
        }
        return teams;
    }

    private List<TeamUser> sendTeamEndReview(Team team) throws MessagingException, UnsupportedEncodingException {
        List<TeamUser> teamUsers = findTeamUserLeaderOrMember(team);
        for (TeamUser teamUser : teamUsers) {
            sendTeamReviewDateAfterEmail(teamUser);
        }
        return teamUsers;
    }

    private void sendTeamReviewDateAfterEmail(TeamUser teamUser) throws MessagingException, UnsupportedEncodingException {
        User user = teamUser.getUser();
        Team team = findTeamUserByTeam(teamUser);
        Contest contest = findContest(team.getContestId());
        emailService.sendEndTeamEmail(user.getEmail(), contest.getTitle());
    }

    private List<TeamUser> findTeamUserLeaderOrMember(Team team) {
        return teamUserRepository.findAllByTeamIdAndRoleLessThanEqual(team.getId(), TEAM_MEMBER.getCode());
    }

    private List<Team> getReviewDateAfterTeam(Date date) {
        System.out.println(date);
        return teamRepository.findAllByReviewDateLessThanAndProgress(date, PROCEEDING.getNumber());
    }

    private List<TeamUser> findTeamUserIApplied(Long userId, Integer role) {
        return teamUserRepository.findAllByUserIdAndRoleGreaterThanEqual(userId, role); //>=
    }

    private List<TeamUser> findTeamUserLessThan(Long userId, Integer role) {
        return teamUserRepository.findAllByUserIdAndRoleLessThanEqual(userId, role); //<=
    }

    private List<TeamUser> findTeamUserByTeamIdAndRole(Long teamId, Integer role) {
        return teamUserRepository.findAllByTeamIdAndRole(teamId, role);
    }

    private boolean compareTeamProgress(Team team, Integer teamProgress) {
        return team.getProgress().equals(teamProgress);
    }

    private List<Team> findTeamByProgress(Integer progress) {
        return teamRepository.findAllByProgress(progress);
    }

    public List<User> findUserThroughTeamUser(List<TeamUser> teamUsers) {
        return teamUsers.stream().map(v -> v.getUser()).collect(Collectors.toList());
    }

    private List<TeamUser> findTeamUserByUserIdAndRole(Long userId, Integer role) {
        return teamUserRepository.findAllByUserIdAndRole(userId, role);
    }


    private Query createFindContestByIdQuery(String contestId) {
        return new Query(Criteria.where("_id").is(new ObjectId(contestId)));
    }

    private void contestTeamCountDecrease(Team team) {
        Query query = createFindContestByIdQuery(team.getContestId());
        Update update = new Update();
        decreaseContestRecruitTeamNumberInUpdate(update);
        mongoTemplate.updateMulti(query, update, Contest.class);
    }

    private void increaseContestRecruitTeamNumberInUpdate(Update update) {
        update.inc("team_num", 1);
    }

    private void decreaseContestRecruitTeamNumberInUpdate(Update update) {
        update.inc("team_num", -1);
    }

    private Date createTodayDateTimeEnd() {
        Calendar cal = Calendar.getInstance();
        Date startDate = cal.getTime();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    private void validateUserRoleIsVolunteer(TeamUser teamUser) {
        if (!teamUser.getRole().equals(VOLUNTEER.getCode())) {
            throw new ForbiddenException(USER_ROLE_NOT_CHANGE);
        }
    }

    private void validateTeamProgressRecruiting(Team team) {
        if (!team.getProgress().equals(RECRUITING.getNumber())) {
            throw new ForbiddenException(TEAM_PROGRESS_NOT_RECRUITING);
        }
    }

    private Boolean checkUserReviewThisTeam(Long userId, Long teamId) {
        return userReviewedTeamRepository.existsByUserIdAndTeamId(userId, teamId);
    }
}
