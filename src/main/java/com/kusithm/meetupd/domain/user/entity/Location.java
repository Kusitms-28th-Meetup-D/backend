package com.kusithm.meetupd.domain.user.entity;

import com.kusithm.meetupd.common.entity.BaseEntity;
import com.kusithm.meetupd.domain.team.entity.Team;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity(name = "USER_LOCATION")
public class Location extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Long id;

    @Column(name = "location", nullable = false)
    private Integer locationType;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public static Location craeteLocation(Integer locationType) {
        return Location.builder()
                .locationType(locationType)
                .build();
    }

    public static Location craeteLocation(Integer locationType,User user) {
        return Location.builder()
                .locationType(locationType)
                .user(user)
                .build();
    }

    public void changeUser(User user) {
        this.user = user;
        user.updateLocation(this);
    }

    public void changeTeam(Team team) {
        this.team = team;
        team.updateLocation(this);
    }
}
