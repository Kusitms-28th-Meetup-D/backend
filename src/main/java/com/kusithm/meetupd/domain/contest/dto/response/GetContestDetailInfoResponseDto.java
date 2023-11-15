package com.kusithm.meetupd.domain.contest.dto.response;

import com.kusithm.meetupd.domain.contest.entity.Contest;
import com.kusithm.meetupd.domain.contest.entity.ContestType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

@Getter
@Builder
public class GetContestDetailInfoResponseDto {

    private String contestId;

    private String title;

    private List<String> images;

    private Long remainDay;

    private String description;

    private String recruitDate;

    private List<String> types;

    private String subject;

    private String qualification;

    private String fullSchedule;

    private String price;

    public static GetContestDetailInfoResponseDto of(Contest contest, LocalDate nowDate) {
        String recruitDate = "[접수기간]" + contest.getRecruitmentStartDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
                + "(" + contest.getRecruitmentStartDate().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN) + ") ~ "
                + contest.getRecruitmentEndDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
                + "(" + contest.getRecruitmentEndDate().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN) + ") <"
                + DAYS.between(contest.getRecruitmentStartDate(), contest.getRecruitmentEndDate()) + "일간>";
        return GetContestDetailInfoResponseDto.builder()
                .contestId(contest.getId())
                .title(contest.getTitle())
                .images(contest.getContestImages())
                .remainDay(DAYS.between(nowDate, contest.getRecruitmentEndDate()))
                .description(contest.getDesc())
                .recruitDate(recruitDate)
                .types(contest.getTypes().stream()
                        .map(type ->ContestType.ofCode(type).getValue())
                        .collect(Collectors.toList())
                )
                .subject(contest.getSubject())
                .qualification(contest.getQualification())
                .fullSchedule(contest.getFullSchedule())
                .price(contest.getPrice())
                .build();
    }
}
