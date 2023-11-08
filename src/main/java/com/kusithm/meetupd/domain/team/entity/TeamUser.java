package com.kusithm.meetupd.domain.team.entity;

import com.kusithm.meetupd.common.entity.BaseEntity;
import jakarta.persistence.*;

@Entity
public class TeamUser extends BaseEntity {

    @Id
    @Column(name = "team_user_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer teamUserId;

    @Column(name = "role",nullable = false)
    private Integer role;

}
