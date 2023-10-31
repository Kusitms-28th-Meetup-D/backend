package com.kusithm.meetupd.domain.contest.dto.response;


import com.kusithm.meetupd.domain.contest.entity.Contest;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

@Builder
@Getter
public class FindContestsResponseDto {

    private String contestId;

    private String title;

    private String company;

    private List<Integer> types;

    private List<String> images;

    private Long remainDay;

    private Integer teamNum;

    public static FindContestsResponseDto of(Contest contest, LocalDate nowDate) {

        return FindContestsResponseDto.builder()
                .contestId(contest.getId())
                .title(contest.getTitle())
                .company(contest.getCompany())
                .types(contest.getTypes())
                .images(contest.getContestImages())
                .remainDay(DAYS.between(nowDate, contest.getRecruitmentEndDate()))
                .teamNum(contest.getTeamNum())
                .build();
    }
}

