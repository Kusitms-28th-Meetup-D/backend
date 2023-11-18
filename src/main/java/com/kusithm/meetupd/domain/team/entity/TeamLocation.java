package com.kusithm.meetupd.domain.team.entity;

import com.kusithm.meetupd.common.entity.BaseEntity;
import com.kusithm.meetupd.domain.user.entity.User;
import com.kusithm.meetupd.domain.user.entity.UserLocation;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity(name = "TEAM_LOCATION")
public class TeamLocation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Long id;

    @Column(name = "location", nullable = false)
    private Integer locationType;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public void changeTeam(Team team) {
        this.team = team;
        team.updateLocation(this);
    }

    public static TeamLocation createLocation(Integer locationType) {
        return TeamLocation.builder()
                .locationType(locationType)
                .build();
    }
}
