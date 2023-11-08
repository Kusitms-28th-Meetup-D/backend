package com.kusithm.meetupd.domain.review.entity.inner;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Builder
@Getter
public class ReviewComment {

    @Field(name = "team_id")
    private Long teamId;

    @Field(name = "contest_name")
    private String contestName;

    @Field(name = "comment")
    private String comment;

    @Field(name = "create_at")
    @Builder.Default
    private LocalDateTime createAt = LocalDateTime.now();

    public static ReviewComment createRecommendationComment(Long teamId, String contestName, String comment) {
        return ReviewComment.builder()
                .teamId(teamId)
                .contestName(contestName)
                .comment(comment)
                .build();
    }
}
