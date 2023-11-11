package com.kusithm.meetupd.domain.user.entity;

import com.kusithm.meetupd.common.entity.BaseEntity;
import com.kusithm.meetupd.domain.team.entity.TeamUser;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static com.kusithm.meetupd.domain.user.entity.Location.craeteLocation;
import static com.kusithm.meetupd.domain.user.entity.Major.createMajor;
import static com.kusithm.meetupd.domain.user.entity.Task.createTask;

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

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "profile_image", nullable = false)
    private String profileImage;

    @Column(name="self_introduction",nullable = false)
    private String selfIntroduction;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Major> majors = new ArrayList<>(); // 전공

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Location location;  // 지역

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Task> tasks = new ArrayList<>();   //희망 직무

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Internship> internships = new ArrayList<>();   // 인턴쉽

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Award> awards = new ArrayList<>(); // 수상내역

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Tool> tools = new ArrayList<>();   // 사용 툴

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Certificate> certificates = new ArrayList<>(); // 자격증

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<TeamUser> teamUsers = new ArrayList<>();


    public static User createRegisterUser(String username,
                                  Integer location,
                                  String major,
                                  String task,
                                  String selfIntroduction,
                                  Long kakaoId,
                                  String email,
                                  String profileImage
                                  ) {
        User user = User.builder()
                .username(username)
                .selfIntroduction(selfIntroduction)
                .kakaoId(kakaoId)
                .email(email)
                .profileImage(profileImage)
                .build();

        Location createdlocation = craeteLocation(location);
        createdlocation.changeUser(user);

        Major createdMajor = createMajor(major);
        createdMajor.changeUser(user);

        Task createdTask = createTask(task);
        createdTask.updateUser(user);

        return user;
    }

    public void updateLocation(Location location) {
        this.location = location;
    }

    public void addMajor(Major major) {
        this.majors.add(major);
    }

    public void addTask(Task task) {
        this.tasks.add(task);
    }
}
