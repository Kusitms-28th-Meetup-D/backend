package com.kusithm.meetupd.domain.team.controller;

import com.kusithm.meetupd.common.auth.UserId;
import com.kusithm.meetupd.common.dto.SuccessResponse;
import com.kusithm.meetupd.common.dto.code.SuccessCode;
import com.kusithm.meetupd.domain.review.dto.request.UpdateTeamProgressRecruitmentRequestDto;
import com.kusithm.meetupd.domain.team.dto.TeamIOpenedResponseDto;
import com.kusithm.meetupd.domain.team.dto.TestDto;
import com.kusithm.meetupd.domain.team.dto.request.*;
import com.kusithm.meetupd.domain.team.dto.response.*;
import com.kusithm.meetupd.domain.team.entity.Team;
import com.kusithm.meetupd.domain.team.service.TeamService;
import com.kusithm.meetupd.domain.user.dto.response.ReviewPageUserResponseDto;
import feign.Response;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static com.kusithm.meetupd.domain.team.entity.TeamProgressType.RECRUITING;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/teams")
@Slf4j
public class TeamController {

    private final TeamService teamService;

    // 공모전 리스트 상단의 모집중인 팀 리스트
    @GetMapping("/recruiting")
    public ResponseEntity<SuccessResponse<TeamResponseDto>> findAllRecruitingTeams(PageDto pageDTO) {

        Page<Team> teamsCondition = teamService.findTeamsCondition(pageDTO, RECRUITING.getNumber());
        TeamResponseDto response = teamService.findRecruitingTeams(teamsCondition);

        return SuccessResponse.of(SuccessCode.OK, response);
    }

    //해당 공모전에서 모집중인 팀 리스트
    @GetMapping("/contest/{contestId}")
    public ResponseEntity<SuccessResponse<List<RecruitingContestTeamResponseDto>>> findContestRecruitingTeams(@PathVariable String contestId) {

        List<RecruitingContestTeamResponseDto> response = teamService.findContestRecruitingTeams(contestId, RECRUITING.getNumber());

        return SuccessResponse.of(SuccessCode.OK, response);
    }

    //팀 상세조회
    @GetMapping("/detail/{teamId}")
    public ResponseEntity<SuccessResponse<TeamDetailResponseDto>> findTeamDetail(@UserId Long userId, @PathVariable Long teamId) {
        TeamDetailResponseDto response = teamService.findTeamDetail(userId, teamId);
        return SuccessResponse.of(SuccessCode.OK, response);
    }

    //팀 오픈하기
    @PostMapping("/open")
    public ResponseEntity<SuccessResponse> openTeam(@UserId Long userId, @RequestBody RequestCreateTeamDto teamDto) {
        teamService.openTeam(userId, teamDto);
        return SuccessResponse.of(SuccessCode.TEAM_CREATED);
    }
    @PostMapping("/apply")
    public ResponseEntity<SuccessResponse> applyTeam(@UserId Long userId, @RequestBody ApplyTeamRequestDto dto) {
        teamService.applyTeam(userId, dto.getTeamId());
        return SuccessResponse.of(SuccessCode.OK);
    }

    //팀원 상태 변경(지원자 -> 합격 / 반려)
    @PatchMapping("/change-role")
    public ResponseEntity<SuccessResponse> applyTeam(@UserId Long userId, @RequestBody RequestChangeRoleDto requestChangeRoleDto) throws MessagingException, UnsupportedEncodingException {
        teamService.changeRole(userId, requestChangeRoleDto);
        return SuccessResponse.of(SuccessCode.OK);
    }

    // 팀 모집 취소
    @DeleteMapping("/{teamId}")
    public ResponseEntity<SuccessResponse> deleteTeam(@UserId Long userId, @PathVariable Long teamId) {
        teamService.deleteTeam(userId, teamId);
        return SuccessResponse.of(SuccessCode.OK);
    }

    // 팀 모집 중 -> 모집 완료로 상태 변경
    @PatchMapping("/recruitment-complete")
    public ResponseEntity<SuccessResponse> updateTeamProgressRecruitment(@UserId Long userId, @RequestBody UpdateTeamProgressRecruitmentRequestDto request) {
        teamService.updateTeamProgressRecruitment(userId, request.getTeamId());
        return SuccessResponse.of(SuccessCode.OK);
    }

    // 팀 지원 취소하기
    @DeleteMapping("/cancel-apply/{teamId}")
    public ResponseEntity<SuccessResponse> cancelApplyTeam(@UserId Long userId, @PathVariable Long teamId) {
        teamService.cancelApplyTeam(userId, teamId);
        return SuccessResponse.of(SuccessCode.OK);
    }

    //내가 오픈한 팀
    @GetMapping("/opened-myself")
    public ResponseEntity<SuccessResponse<List<TeamIOpenedResponseDto>>> findTeamIOpen(@UserId Long userId) {
        List<TeamIOpenedResponseDto> response = teamService.findTeamIOpen(userId);
        return SuccessResponse.of(SuccessCode.OK, response);
    }

    //내가 지원한 팀
    @GetMapping("/applied-team")
    public ResponseEntity<SuccessResponse<List<TeamIappliedResponseDto>>> findTeamIApplied(@UserId Long userId) {
        List<TeamIappliedResponseDto> response = teamService.appliedTeam(userId);
        return SuccessResponse.of(SuccessCode.OK, response);
    }

    //활동중인 팀
    @GetMapping("/proceed-team")
    public ResponseEntity<SuccessResponse<List<TeamProceedResponseDto>>> findTeamIProceed(@UserId Long userId) {
        List<TeamProceedResponseDto> response = teamService.proceedTeam(userId);
        return SuccessResponse.of(SuccessCode.OK, response);
    }

    //활동했던 팀
    @GetMapping("/worked-team")
    public ResponseEntity<SuccessResponse<List<TeamIWorkedResponseDto>>> findTeamIWorked(@UserId Long userId) {
        List<TeamIWorkedResponseDto> response = teamService.workedTeam(userId);
        return SuccessResponse.of(SuccessCode.OK, response);
    }

    //추천사 페이지 유저파트
    @GetMapping("/review/{teamId}")
    public ResponseEntity<SuccessResponse<ReviewPageUserResponseDto>> getReviewPageUser(@UserId Long userId, @PathVariable Long teamId) {
        ReviewPageUserResponseDto response = teamService.getReviewPageUser(userId,teamId);
        return SuccessResponse.of(SuccessCode.OK, response);
    }

    //팀 관리하기
    @GetMapping("/manage/{teamId}")
    public ResponseEntity<SuccessResponse<TeamManageResponseDto>> manageTeam(@UserId Long userId, @PathVariable Long teamId) {
        TeamManageResponseDto response = teamService.manageTeam(userId,teamId);
        return SuccessResponse.of(SuccessCode.OK, response);
    }
}
