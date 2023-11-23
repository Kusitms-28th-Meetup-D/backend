package com.kusithm.meetupd.domain.contest.dto.response;

import com.kusithm.meetupd.domain.contest.entity.Contest;
import com.kusithm.meetupd.domain.review.dto.response.GetUserReviewResponseDto;
import com.kusithm.meetupd.domain.review.entity.Review;
import com.kusithm.meetupd.domain.team.dto.response.RecruitingTeamResponseDto;
import com.kusithm.meetupd.domain.team.entity.Team;
import com.kusithm.meetupd.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.kusithm.meetupd.domain.review.dto.response.GetUserReviewResponseDto.CommentsResponseDto.createCommentsResponseDtos;
import static com.kusithm.meetupd.domain.review.dto.response.GetUserReviewResponseDto.TeamCultureResponseDto.createTeamCultureResponseDtos;
import static com.kusithm.meetupd.domain.review.dto.response.GetUserReviewResponseDto.WorkMethodResponseDto.createWorkMethodResponseDtos;
import static java.time.temporal.ChronoUnit.DAYS;

@Getter
@Builder
public class GetMainRecommendationResponseDto {

    private List<RecommendationContest> recommendationContests;

    private List<RecruitingTeamResponseDto> recommendationTeams;

    public static GetMainRecommendationResponseDto of(List<Contest> recommendContests, List<RecruitingTeamResponseDto> recommendationTeams, LocalDate nowDate) {
        return GetMainRecommendationResponseDto.builder()
                .recommendationContests(recommendContests.stream()
                        .map(it ->RecommendationContest.of(it, nowDate))
                        .collect(Collectors.toList())
                )
                .recommendationTeams(recommendationTeams).build();
    }

    @Getter
    @Builder
    public static class RecommendationContest {

        private String contestId;

        private String title;

        private String company;

        private List<String> images;

        private Long remainDay;

        private Integer teamNum;

        public static RecommendationContest of(Contest contest, LocalDate nowDate) {
            return RecommendationContest.builder()
                    .contestId(contest.getId())
                    .title(contest.getTitle())
                    .company(contest.getCompany())
                    .images(contest.getContestImages())
                    .remainDay(DAYS.between(nowDate, contest.getRecruitmentEndDate()))
                    .teamNum(contest.getTeamNum())
                    .build();
        }
    }



}
