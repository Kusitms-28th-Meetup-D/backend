package com.kusithm.meetupd.domain.team.controller;

import com.kusithm.meetupd.common.dto.SuccessResponse;
import com.kusithm.meetupd.common.dto.code.SuccessCode;
import com.kusithm.meetupd.domain.contest.dto.response.FindContestsResponseDto;
import com.kusithm.meetupd.domain.team.dto.response.PageDto;
import com.kusithm.meetupd.domain.team.dto.response.RecruitingContestTeamResponseDto;
import com.kusithm.meetupd.domain.team.dto.response.RecruitingTeamResponseDto;
import com.kusithm.meetupd.domain.team.entity.Team;
import com.kusithm.meetupd.domain.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
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
    public ResponseEntity<SuccessResponse<List<RecruitingTeamResponseDto>>> findAllRecruitingTeams(PageDto pageDTO) {

        List<Team> teamsCondition = teamService.findTeamsCondition(pageDTO,RECRUITING.getNumber());
        List<RecruitingTeamResponseDto> response = teamService.findRecruitingTeams(teamsCondition);

        return SuccessResponse.of(SuccessCode.OK, response);
    }

    //해당 공모전에서 모집중인 팀 리스트
    @GetMapping("/{contestId}")
    public ResponseEntity<SuccessResponse<List<RecruitingContestTeamResponseDto>>> findRecruitingTeams() {

        return null;
    }
}
