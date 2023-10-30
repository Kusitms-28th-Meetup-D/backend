package com.kusithm.meetupd.domain.grade.entity;

import com.kusithm.meetupd.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity(name = "GRADE")
public class Grade extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "grade_id")
    private Long id;

    @Column(name = "type", nullable = false)
    private TYPE userType;

    @Column(name = "buy_time", nullable = false)
    private Timestamp buyTime;

    @Column(name = "end_time", nullable = false)
    private Timestamp endTime;

}
