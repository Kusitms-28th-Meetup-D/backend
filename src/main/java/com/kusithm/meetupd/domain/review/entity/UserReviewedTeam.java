package com.kusithm.meetupd.domain.review.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Entity(name = "user_reviewed_team")
public class UserReviewedTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_reviewed_team_id")
    private Long id;

    private Long userId;

    private Long teamId;

    public static UserReviewedTeam of(Long userId, Long teamId) {
        return UserReviewedTeam.builder()
                .userId(userId)
                .teamId(teamId)
                .build();
    }
}
