package com.kusithm.meetupd.domain.contest.entity;

import jakarta.persistence.Id;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Document("contest")
public class Contest {

    @Id
    @Field(name="_id")
    private String id;

    @Field(name = "title")
    private String title;   // 공모전 제목

    @Field(name = "company")
    private String company;   // 공모전 주최사

    @Field(name = "types")
    private List<Integer> types = new ArrayList<>();   // 공모전 분야

    @Field(name = "recruit_start")
    private LocalDate recruitmentStartDate;  // 지원 시작 일자

    @Field(name = "recruit_end")
    private LocalDate recruitmentEndDate;    // 지원 마감 일자

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

    @Field(name = "team_num")
    private Integer teamNum;    // 공모전 내 팀 수

    @Field(name = "contest_images")
    private List<String> contestImages = new ArrayList<>();   // 공모전 사진

    @Field(name = "subject")
    private String subject;
}
