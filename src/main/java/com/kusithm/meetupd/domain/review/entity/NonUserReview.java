package com.kusithm.meetupd.domain.review.entity;


import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Getter
@Builder
@Document(collection = "non_user_recommend")
public class NonUserReview {

    @Id
    @Field(name = "_id")
    private String id;

    @Field(name = "user_id")
    private Long userId;

    @CreatedDate
    @Field(name = "create_at")
    private LocalDateTime createAt;

    public static NonUserReview createNonUserReview(Long userId) {
        return NonUserReview.builder()
                .userId(userId)
                .build();
    }
}
