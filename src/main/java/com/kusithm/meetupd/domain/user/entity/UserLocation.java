package com.kusithm.meetupd.domain.user.entity;

import com.kusithm.meetupd.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity(name = "USER_LOCATION")
public class UserLocation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Long id;

    @Column(name = "location", nullable = false)
    private Integer locationType;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public static UserLocation createLocation(Integer locationType) {
        return UserLocation.builder()
                .locationType(locationType)
                .build();
    }

    public void changeUser(User user) {
        this.user = user;
        user.updateLocation(this);
    }

    public void changeLocationType(Integer locationType) {
        this.locationType = locationType;
    }

}
