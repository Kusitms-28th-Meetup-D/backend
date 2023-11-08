package com.kusithm.meetupd.domain.user.dto;


import com.kusithm.meetupd.domain.user.entity.*;
import lombok.*;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserMypageResponseDto {

    private Long userId;

    private String profile_image;

    private String username;

    private List<String> task; //직무

    private String location; //활동 지역

    private List<String> major; //전공

    private String selfIntroduction; //한줄소개

    private List<String> internships; //이력 - 대외활동 및 인턴

    private List<String> awards; //이력 - 수상 경력

    private List<String> tools; //스킬 - 사용 가능 툴

    private List<String> certificates; //스킬 - 보유 자격증

    public UserMypageResponseDto (User user) {
        this.userId = user.getId();
        this.profile_image = user.getProfileImage();
        this.username = user.getUsername();
        this.task = user.getTasks().stream().map(Task::getTask).collect(Collectors.toList());
        this.location = LocationType.ofCode(user.getLocation().getLocationName()).getValue();
        this.major =user.getMajors().stream().map(Major::getMajor).collect(Collectors.toList());
        this.selfIntroduction = user.getSelfIntroduction();
        this.internships = user.getInternships().stream().map(Internship::getInternships).collect(Collectors.toList());
        this.awards =user.getAwards().stream().map(Award::getAwards).collect(Collectors.toList());
        this.tools =user.getTools().stream().map(Tool::getTool).collect(Collectors.toList());
        this.certificates =user.getCertificates().stream().map(Certificate::getCertificate).collect(Collectors.toList());
    }
}
