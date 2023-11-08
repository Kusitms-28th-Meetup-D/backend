package com.kusithm.meetupd.domain.review.entity.inner;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Arrays;
import java.util.List;

@Builder
@Getter
public class ReviewTeamCulture {

    @Field(name = "culture_question_type")
    private Integer cultureQuestionType;

    @Field(name = "left_count")
    @Builder.Default
    private Integer leftCount = 0;

    @Field(name = "right_count")
    @Builder.Default
    private Integer rightCount = 0;

    public static List<ReviewTeamCulture> initRecommendationTeamCulture() {
        List<Integer> initTeamCulturesString = Arrays.asList(0, 1, 2);

        /**
         * 초기 팀문화 객관식 3개 세팅
         */
        List<ReviewTeamCulture> initReviewTeamCultures = initTeamCulturesString.stream()
                .map(it -> ReviewTeamCulture.builder().cultureQuestionType(it).build())
                .toList();
        return initReviewTeamCultures;
    }
}
