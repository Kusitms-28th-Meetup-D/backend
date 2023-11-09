package com.kusithm.meetupd.domain.review.dto.response;

import com.kusithm.meetupd.domain.review.entity.Review;
import com.kusithm.meetupd.domain.review.entity.inner.ReviewChoice;
import com.kusithm.meetupd.domain.review.entity.inner.ReviewComment;
import com.kusithm.meetupd.domain.review.entity.inner.ReviewTeamCulture;
import com.kusithm.meetupd.domain.review.entity.inner.ReviewWorkMethod;
import com.kusithm.meetupd.domain.review.util.ListComparatorKeywordCount;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.kusithm.meetupd.domain.review.dto.response.GetUserReviewResponseDto.CommentsResponseDto.createCommentsResponseDtos;
import static com.kusithm.meetupd.domain.review.dto.response.GetUserReviewResponseDto.TeamCultureResponseDto.createTeamCultureResponseDtos;
import static com.kusithm.meetupd.domain.review.dto.response.GetUserReviewResponseDto.WorkMethodResponseDto.createWorkMethodResponseDtos;

@Getter
@Builder
public class GetUserReviewResponseDto {

    private Long userId;

    private List<KeywordResponseDto> keywords;

    private List<TeamCultureResponseDto> teamCultures;

    private List<WorkMethodResponseDto> workMethods;

    private List<CommentsResponseDto> comments;

    public static GetUserReviewResponseDto of(Review review) {
        return GetUserReviewResponseDto.builder()
                .userId(review.getUserId())
                .keywords(KeywordResponseDto.createKeywordResponseDtos(review.getMultipleChoices()))
                .teamCultures(createTeamCultureResponseDtos(review.getTeamCultures()))
                .workMethods(createWorkMethodResponseDtos(review.getWorkMethods()))
                .comments(createCommentsResponseDtos(review.getReviewComments()))
                .build();

    }

    @Getter
    @Builder
    public static class KeywordResponseDto {

        private Integer keywordType;

        private Integer count;

        public static KeywordResponseDto of(ReviewChoice reviewChoice) {
            return KeywordResponseDto.builder()
                    .keywordType(reviewChoice.getKeywordType())
                    .count(reviewChoice.getCount())
                    .build();
        }

        public static List<KeywordResponseDto> createKeywordResponseDtos(List<ReviewChoice> reviewChoices) {
            reviewChoices.sort(new ListComparatorKeywordCount());

            List<KeywordResponseDto> keywordResponseDtos = new ArrayList<>();
            /**
             * 상위 count 5개 뽑는데 0개는 무시하고 뽑기
             */
            for(int i =0; i < 5; i++) {
                if(reviewChoices.get(i).getCount() == 0) break;
                keywordResponseDtos.add(KeywordResponseDto.of(reviewChoices.get(i)));
            }

            return keywordResponseDtos;
        }
    }

    @Getter
    @Builder
    public static class TeamCultureResponseDto {

        private Integer teamCultureType;

        private Integer percent;

        public static TeamCultureResponseDto of(ReviewTeamCulture teamCulture) {
            return TeamCultureResponseDto.builder()
                    .teamCultureType(teamCulture.getCultureQuestionType())
                    .percent(getPercent(teamCulture))
                    .build();
        }

        public static List<TeamCultureResponseDto> createTeamCultureResponseDtos(List<ReviewTeamCulture> teamCultures) {
            return teamCultures.stream()
                    .map(TeamCultureResponseDto::of)
                    .collect(Collectors.toList());
        }

        private static Integer getPercent(ReviewTeamCulture teamCulture){
            return (int) (teamCulture.getRightCount()/(double)(teamCulture.getLeftCount() + teamCulture.getRightCount()) * 100);
        }
    }

    @Getter
    @Builder
    public static class WorkMethodResponseDto {

        private Integer workMethodType;

        private Integer percent;

        public static WorkMethodResponseDto of(ReviewWorkMethod workMethod) {
            return WorkMethodResponseDto.builder()
                    .workMethodType(workMethod.getWorkQuestionType())
                    .percent(getPercent(workMethod))
                    .build();
        }

        public static List<WorkMethodResponseDto> createWorkMethodResponseDtos (List<ReviewWorkMethod> workMethods) {
            return workMethods.stream()
                    .map(WorkMethodResponseDto::of)
                    .collect(Collectors.toList());
        }
        private static Integer getPercent(ReviewWorkMethod workMethod){
            return (int) (workMethod.getRightCount()/(double)(workMethod.getLeftCount() + workMethod.getRightCount()) * 100);
        }
    }

    @Getter
    @Builder
    public static class CommentsResponseDto {

        private String comments;

        private String contestName;


        public static CommentsResponseDto of(ReviewComment comment) {
            return CommentsResponseDto.builder()
                    .comments(comment.getComment())
                    .contestName(comment.getContestName())
                    .build();
        }

        public static List<CommentsResponseDto> createCommentsResponseDtos(List<ReviewComment> comments) {
            return comments.stream()
                    .map(CommentsResponseDto::of)
                    .collect(Collectors.toList());
        }
    }

}
