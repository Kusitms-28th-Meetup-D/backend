package com.kusithm.meetupd.domain.review.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

import static com.kusithm.meetupd.domain.review.dto.request.UploadReviewRequestDto.*;

@Getter
public class NonUserReviewRequestDto {
    private Long userId;

    private List<SelectKeywordDto> selectedKeywords;

    private SelectTeamCultureDto selectedTeamCultures;

    private SelectWorkMethodDto selectedWorkMethods;

    private String recommendationComment;
}
