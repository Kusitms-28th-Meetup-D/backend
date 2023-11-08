package com.kusithm.meetupd.domain.review.entity;


import com.kusithm.meetupd.domain.review.dto.request.UploadReviewRequestDto;
import com.kusithm.meetupd.domain.review.dto.request.UploadReviewRequestDto.UploadReviewDto;
import com.kusithm.meetupd.domain.review.entity.inner.SelectKeyword;
import com.kusithm.meetupd.domain.review.entity.inner.SelectTeamCulture;
import com.kusithm.meetupd.domain.review.entity.inner.SelectWorkMethod;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@Document("not_upload_review")
public class WaitReview {

    @Id
    @Field(name = "_id", targetType = FieldType.OBJECT_ID)
    private String id;

    @Field(name = "user_id")
    private Long userId;    // 추천사 받은 유저 ID

    @Field(name = "team_id")
    private Long teamId;    // 추천사 써진 팀 Id

    @Field(name = "selected_keywords")
    private List<SelectKeyword> selectedKeywords;   // 객관식 추천사 List

    @Field(name = "selected_team_cultures")
    private SelectTeamCulture selectedTeamCultures; // 팀문화 성향 List

    @Field(name = "selected_work_methods")
    private SelectWorkMethod selectedWorkMethods;   // 작업 방식 성향 List

    @Field(name = "recommendation_comment")
    private String recommendationComment;

    public static WaitReview of(UploadReviewDto uploadReviewDto) {
        WaitReview waitReview = WaitReview.builder()
                .userId(uploadReviewDto.getUserId())
                .teamId(uploadReviewDto.getTeamId())
                .selectedKeywords(
                        uploadReviewDto.getSelectedKeywords().stream()
                                .map(SelectKeyword::of)
                                .collect(Collectors.toList())
                )
                .selectedTeamCultures(SelectTeamCulture.of(uploadReviewDto.getSelectedTeamCultures()))
                .selectedWorkMethods(SelectWorkMethod.of(uploadReviewDto.getSelectedWorkMethods()))
                .build();
        if(uploadReviewDto.getRecommendationComment() != null) {
            waitReview.addComment(uploadReviewDto.getRecommendationComment());
        }
        return waitReview;
    }

    private void addComment(String recommendationComment) {
        this.recommendationComment = recommendationComment;
    }
}
