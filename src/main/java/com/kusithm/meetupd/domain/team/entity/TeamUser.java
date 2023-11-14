package com.kusithm.meetupd.domain.team.entity;

import com.kusithm.meetupd.common.entity.BaseEntity;
import com.kusithm.meetupd.domain.user.entity.Location;
import com.kusithm.meetupd.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.kusithm.meetupd.domain.team.entity.TeamProgressType.RECRUITING;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamUser extends BaseEntity {

    @Id
    @Column(name = "team_user_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer teamUserId;

    @Column(name = "role",nullable = false)
    private Integer role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public static TeamUser toEntity(Integer role, Team team, User user) {
        return TeamUser.builder()
                .role(role)
                .team(team)
                .user(user)
                .build();
    }
}

