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

    private List<String> task = new ArrayList<>(); //직무

    private String location; //활동 지역

    private List<String> major= new ArrayList<>(); //전공

    private String selfIntroduction; //한줄소개

    private List<String> internships= new ArrayList<>(); //이력 - 대외활동 및 인턴

    private List<String> awards= new ArrayList<>(); //이력 - 수상 경력

    private List<String> tools= new ArrayList<>(); //스킬 - 사용 가능 툴

    private List<String> certificates= new ArrayList<>(); //스킬 - 보유 자격증


    public UserMypageResponseDto (User user,List<String> task,List<String> major,List<String> internships,List<String> awards
        ,List<String> tools, List<String> certificates) {
        this.userId = user.getId();
        this.profile_image = user.getProfileImage();
        this.username = user.getUsername();
        this.task = task;
        this.location = LocationType.ofCode(user.getLocation().getLocationName()).getValue();
        this.major =major;
        this.selfIntroduction = user.getSelfIntroduction();
        this.internships = internships;
        this.awards =awards;
        this.tools =tools;
        this.certificates =certificates;
    }
}
