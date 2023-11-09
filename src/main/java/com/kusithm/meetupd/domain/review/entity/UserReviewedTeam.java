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

    // TODO : User 확실하게 생성되면 User ID로 넣기
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;
    private Long userId;

    // TODO : Team 생성되면 Team ID로 넣기
//    @ManyToOne
//    @JoinColumn(name = "team_id")
//    private Team team;
    private Long teamId;
//
//    @CreatedDate
//    @Column(name = "reviewed_at", updatable = false)
//    @Convert(converter = LocalDateTimeConverter.class)
//    private LocalDateTime reviewedAt;

    public static UserReviewedTeam of(Long userId, Long teamId) {
        return UserReviewedTeam.builder()
                .userId(userId)
                .teamId(teamId)
                .build();
    }
}
