package com.kusithm.meetupd.domain.recommendation.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
public class RecommendationChoice {



    @Field(name = "keyword_type")
    private Integer keywordType;

    @Field(name = "count")
    @Builder.Default
    private Integer count = 0;

    public static List<RecommendationChoice> initRecommendationChoice() {
        List<Integer> keywordInitCodes = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

        /**
         * 초기 객관식 추천사 10개 세팅
         */
        List<RecommendationChoice> initRecommendationChoices = keywordInitCodes.stream()
                .map(it -> RecommendationChoice.builder().keywordType(it).build())
                .toList();

        return initRecommendationChoices;

    }

}
