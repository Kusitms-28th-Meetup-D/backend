package com.kusithm.meetupd.domain.review.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class UploadReviewRequestDto {

    private List<UploadReviewDto> uploadReviews;

    @Getter
    public static class UploadReviewDto {

        private Long userId;

        private Long teamId;

        private List<SelectKeywordDto> selectedKeywords;

        private SelectTeamCultureDto selectedTeamCultures;

        private SelectWorkMethodDto selectedWorkMethods;

        private String recommendationComment;
    }

    @Getter
    public static class SelectKeywordDto {

        private Integer selectKeyword;
    }
    @Getter
    public static class SelectTeamCultureDto {

        private Integer feedbackStyle;

        private Integer teamStyle;

        private Integer personalityStyle;
    }
    @Getter
    public static class SelectWorkMethodDto {

        private Integer workStyle;

        private Integer resultProcess;

        private Integer workLifeBalance;
    }
}
