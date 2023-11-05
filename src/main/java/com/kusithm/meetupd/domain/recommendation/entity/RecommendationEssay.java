package com.kusithm.meetupd.domain.recommendation.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Field;

@Builder
@Getter
public class RecommendationEssay {

    @Field(name = "team_id")
    private Long teamId;

    @Field(name = "team_name")
    private String teamName;

    @Field(name = "essay")
    private String essay;

    public static RecommendationEssay createRecommendationEssay(Long teamId, String teamName, String essay) {
        return RecommendationEssay.builder()
                .teamId(teamId)
                .teamName(teamName)
                .essay(essay)
                .build();
    }
}
