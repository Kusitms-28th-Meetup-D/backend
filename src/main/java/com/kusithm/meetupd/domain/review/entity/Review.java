package com.kusithm.meetupd.domain.review.entity;


import com.kusithm.meetupd.domain.review.entity.inner.ReviewChoice;
import com.kusithm.meetupd.domain.review.entity.inner.ReviewComment;
import com.kusithm.meetupd.domain.review.entity.inner.ReviewTeamCulture;
import com.kusithm.meetupd.domain.review.entity.inner.ReviewWorkMethod;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@Document("review")
public class Review {

    @Id
    @Field(name="_id")
    private String id;


    @Field(name = "user_id")
    private Long userId;   // 유저 아이디

    @Field(name = "multiple_chocies")
    @Builder.Default
    private List<ReviewChoice> multipleChoices = ReviewChoice.initRecommendationChoice(); // 객관식 평가

    @Field(name = "comments")
    @Builder.Default
    private List<ReviewComment> reviewComments = new ArrayList<>();   // 한줄평

    @Field(name = "team_cultures")
    @Builder.Default
    private List<ReviewTeamCulture> teamCultures = ReviewTeamCulture.initRecommendationTeamCulture();   // 팀문화 객관식

    @Field(name = "work_methods")
    @Builder.Default
    private List<ReviewWorkMethod> workMethods = ReviewWorkMethod.initRecommendationWorkMethod();   // 작업 방식 객관식

    public static Review creatEmptyReview(Long userId) {
        return Review.builder()
                .userId(userId)
                .build();
    }

    public void incKeyWordCount(Integer keywordPosition) {
        multipleChoices.get(keywordPosition).increaseCount();
    }
}
