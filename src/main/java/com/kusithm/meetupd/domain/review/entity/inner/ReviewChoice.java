package com.kusithm.meetupd.domain.review.entity.inner;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Arrays;
import java.util.List;

@Builder
@Getter
public class ReviewChoice {



    @Field(name = "keyword_type")
    private Integer keywordType;

    @Field(name = "count")
    @Builder.Default
    private Integer count = 0;

    public static List<ReviewChoice> initRecommendationChoice() {
        List<Integer> keywordInitCodes = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

        /**
         * 초기 객관식 추천사 10개 세팅
         */
        List<ReviewChoice> initReviewChoices = keywordInitCodes.stream()
                .map(it -> ReviewChoice.builder().keywordType(it).build())
                .toList();

        return initReviewChoices;

    }

    public void increaseCount() {
        this.count += 1;
    }
}
