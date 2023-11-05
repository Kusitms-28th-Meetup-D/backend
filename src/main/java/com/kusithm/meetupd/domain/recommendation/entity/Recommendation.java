package com.kusithm.meetupd.domain.recommendation.entity;


import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@Document("recommendation")
public class Recommendation {

    @Id
    @Field(name="_id")
    private String id;


    @Field(name = "user_id")
    private Long userId;   // 유저 아이디

    @Field(name = "multiple_chocies")
    @Builder.Default
    private List<RecommendationChoice> multipleChoices = RecommendationChoice.initRecommendationChoice(); // 객관식 평가

    @Field(name = "essays")
    @Builder.Default
    private List<RecommendationEssay> essays = new ArrayList<>();   // 한줄평

    @Field(name = "team_cultures")
    @Builder.Default
    private List<RecommendationTeamCulture> teamCultures = RecommendationTeamCulture.initRecommendationTeamCulture();   // 팀문화 객관식

    @Field(name = "work_methods")
    @Builder.Default
    private List<RecommendationWorkMethod> workMethods = RecommendationWorkMethod.initRecommendationWorkMethod();   // 작업 방식 객관식

    public static Recommendation creatEmptyReview(Long userId) {
        return Recommendation.builder()
                .userId(userId)
                .build();
    }
}
