package com.kusithm.meetupd.domain.user.entity;

import com.kusithm.meetupd.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity(name = "USER_INTERNSHIPS")
public class Internship extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "internships_id")
    private Long id;

    @Column(name = "internships_name", nullable = false)
    private String internships;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public static Internship createInternship(String internships) {
        return Internship.builder()
                .internships(internships)
                .build();
    }

    public void changeUser(User user) {
        this.user = user;
        user.addInternship(this);
    }
}
