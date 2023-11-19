package com.kusithm.meetupd.domain.team.scheduler;

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

    // 팀 리뷰날짜 확인하여 지났으면 팀 활동이 종료되었으니 리뷰보내라고 메일전송
    @Scheduled(cron = "10 0 12 * * *")
    public void scheduleTaskTeamUpdateTeamProgressEnd() throws MessagingException, UnsupportedEncodingException {
        List<Team> teams = teamService.updateTeamProgressEnd();
        log.info("updateTeamProgressEnd schedule tasks - {}", LocalDate.now());
        teams.forEach(it -> log.info("updated team id - {}", it.getId()));
    }
}
