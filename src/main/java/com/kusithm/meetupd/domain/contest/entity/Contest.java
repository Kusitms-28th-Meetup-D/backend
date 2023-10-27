package com.kusithm.meetupd.domain.contest.entity;

import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

@Document(collation = "contest")
public class Contest {

    @Id
    private String id;

    @Field(name = "title")
    private String title;   // 공모전 제목

    @Field(name = "type")
    private Integer type;   // 공모전 분야

    @Field(name = "recruit_start")
    private Date recruitmentStartDate;  // 지원 시작 일자

    @Field(name = "recruit_end")
    private Date recruitmentEndDate;    // 지원 마감 일자

    @Field(name = "qualification")
    private String qualification;   // 지원 자격

    @Field(name = "full_schedule")
    private String fullSchedule;    // 전체 일정

    @Field(name = "price")
    private String price;   // 수상

    @Field(name = "apply_as")
    private String applyAs; // 지원 방법

    @Field(name = "description")
    private String desc;    // 상세 설명

    @Field(name = "contest_images")
    private List<ContestImage> contestImages;   // 공모전 사진
}
