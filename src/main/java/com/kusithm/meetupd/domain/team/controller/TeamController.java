package com.kusithm.meetupd.domain.team.controller;

import com.kusithm.meetupd.common.dto.SuccessResponse;
import com.kusithm.meetupd.common.dto.code.SuccessCode;
import com.kusithm.meetupd.domain.team.dto.response.TeamDetailResponseDto;
import com.kusithm.meetupd.domain.team.dto.request.PageDto;
import com.kusithm.meetupd.domain.team.dto.response.RecruitingContestTeamResponseDto;
import com.kusithm.meetupd.domain.team.dto.response.TeamResponseDto;
import com.kusithm.meetupd.domain.team.entity.Team;
import com.kusithm.meetupd.domain.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

        Page<Team> teamsCondition = teamService.findTeamsCondition(pageDTO,RECRUITING.getNumber());
        TeamResponseDto  response = teamService.findRecruitingTeams(teamsCondition);

        return SuccessResponse.of(SuccessCode.OK, response);
    }

    //해당 공모전에서 모집중인 팀 리스트
    @GetMapping("/contest/{contestId}")
    public ResponseEntity<SuccessResponse<List<RecruitingContestTeamResponseDto>>> findContestRecruitingTeams(@PathVariable String contestId) {

        List<RecruitingContestTeamResponseDto> response = teamService.findContestRecruitingTeams(contestId, RECRUITING.getNumber());

        return SuccessResponse.of(SuccessCode.OK, response);
    }

    //팀 상세조회
    @GetMapping("/{teamId}")
    public ResponseEntity<SuccessResponse<TeamDetailResponseDto>> findTeamDetail(@PathVariable Long teamId) {

        teamService.findTeamDetail(teamId);

        return null;
    }

    //팀 오픈하기
//    @PostMapping("/open")
//    public ResponseEntity<SuccessResponse<>> openTeam(){
//
////        return SuccessResponse.of(SuccessCode.OK, response);
//        return null;
//    }
}
