package com.kusithm.meetupd.domain.recommendation.entity.inner;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Arrays;
import java.util.List;

@Builder
@Getter
public class RecommendationWorkMethod {

    @Field(name = "work_question_type")
    private Integer workQuestionType;

    @Field(name = "left_count")
    @Builder.Default
    private Integer leftCount = 0;

    @Field(name = "right_count")
    @Builder.Default
    private Integer rightCount = 0;


    public static List<RecommendationWorkMethod> initRecommendationWorkMethod() {
        List<Integer> initWorkMethodType = Arrays.asList(0, 1, 2);

        /**
         * 초기 작업방식 객관식 3개 세팅
         */
        List<RecommendationWorkMethod> initRecommendationWorkMethods = initWorkMethodType.stream()
                .map(it -> RecommendationWorkMethod.builder().workQuestionType(it).build())
                .toList();
        return initRecommendationWorkMethods;
    }
}
