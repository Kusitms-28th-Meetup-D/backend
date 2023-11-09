package com.kusithm.meetupd.domain.user.entity;

import com.kusithm.meetupd.common.entity.BaseEntity;
import com.kusithm.meetupd.domain.team.entity.TeamUser;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity(name = "WANTEAM_USER")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "kakao_id", nullable = false)
    private Long kakaoId;

    @Column(name = "name", nullable = false)
    private String username;

    @Column(name = "age", nullable = false)
    private Integer age;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "profile_image", nullable = true)
    private String profileImage;

    @Column(name = "birth_day", nullable = false)
    private Date birth_day;

    @Column(name = "gender", nullable = false)
    private GENDER gender;

    @Column(name="self_introduction",nullable = false)
    private String selfIntroduction;

    @OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Major> majors = new ArrayList<>();

    @OneToOne(mappedBy = "user",cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private Location location;

    @OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Task> tasks = new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Internship> internships = new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Award> awards = new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Tool> tools = new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Certificate> certificates = new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<TeamUser> teamUsers = new ArrayList<>();



    public static User createUser(Long kakaoId, String username, Integer age, String email) {
        User user = User.builder()
                .kakaoId(kakaoId)
                .username(username)
                .age(age)
                .email(email)
                .build();
        return user;
    }
}
