package com.kusithm.meetupd.domain.team.entity;


import com.fasterxml.jackson.databind.ser.Serializers;
import com.kusithm.meetupd.common.entity.BaseEntity;
import com.kusithm.meetupd.domain.user.entity.GENDER;
import com.kusithm.meetupd.domain.user.entity.Location;
import com.kusithm.meetupd.domain.user.entity.Tool;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class Team extends BaseEntity {

    @Id
    @Column(name = "team_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "head_count", nullable = false)
    private Integer headCount; //인원수

    @Column(name = "review_date", nullable = false)
    private Date reviewDate; //회고할 날짜

    @Column(name = "chat_link", nullable = false)
    private String chatLink; //오픈채팅방 링크

    @Column(name = "leader_message", nullable = false)
    private String leaderMessage; //팀장의 한마디

    @Column(name = "progress", nullable = false)
    private Integer progress; //진행상황

    @Column(name = "notice", nullable = false)
    private String notice; //모집공고

    @OneToOne(mappedBy = "team", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Location location;  // 지역

    @OneToMany(mappedBy = "team",cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<TeamUser> teamUsers = new ArrayList<>();

    @Column(name = "contest_id")
    private String contestId;

}
