package com.kusithm.meetupd.domain.team.scheduler;

import com.kusithm.meetupd.domain.contest.entity.Contest;
import com.kusithm.meetupd.domain.contest.service.ContestService;
import com.kusithm.meetupd.domain.team.entity.Team;
import com.kusithm.meetupd.domain.team.service.TeamService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@EnableAsync
@Component
public class TeamScheduler {

    private final TeamService teamService;
    private final ContestService contestService;

    // 팀 리뷰날짜 확인하여 지났으면 팀 활동이 종료되었으니 리뷰보내라고 메일전송
    @Scheduled(cron = "0 1 0 * * *")
    public void scheduleTaskUpdateTeamProgressEnd() throws MessagingException, UnsupportedEncodingException {
        List<Team> teams = teamService.updateTeamProgressEnd();
        log.info("updateTeamProgressEnd schedule tasks - {}", LocalDate.now());
        teams.forEach(it -> log.info("updated team id - {}", it.getId()));
    }

    // 공모전 모집 일자 마감됐으면 해당 공모전에 팀들 상태 활동중으로 변경하기 11/22 모집 완료시 바로 활동중으로 팀 상태 변경하기로함
//    @Scheduled(cron = "1 0 0 * * *")
//    public void scheduleTaskUpdateTeamProgressProceeding() {
//        List<Contest> todayRecruitEndContests = contestService.findRecruitEndContests();
//        todayRecruitEndContests.forEach(contest -> teamService.updateTeamProgressProceeding(contest.getId()));
//        log.info("updateTeamProgressProceeding schedule tasks - {}", LocalDate.now());
//    }
}
