package com.kusithm.meetupd.domain.contest.dto.response;


import com.kusithm.meetupd.domain.contest.entity.Contest;
import com.kusithm.meetupd.domain.contest.entity.ContestType;
import jdk.jfr.ContentType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

@Builder
@Getter
public class FindContestsResponseDto {

    private String contestId;

    private String title;

    private String company;

//    private List<Integer> types;

    private List<String> types;

    private List<String> images;

    private Long remainDay;

    private Integer teamNum;

    public static FindContestsResponseDto of(Contest contest, LocalDate nowDate) {

        return FindContestsResponseDto.builder()
                .contestId(contest.getId())
                .title(contest.getTitle())
                .company(contest.getCompany())
                .types(contest.getTypes().stream()
                        .map(data ->ContestType.ofCode(data).getValue())
                        .collect(Collectors.toList()))
                .images(contest.getContestImages())
                .remainDay(DAYS.between(nowDate, contest.getRecruitmentEndDate()))
                .teamNum(contest.getTeamNum())
                .build();
    }
}

