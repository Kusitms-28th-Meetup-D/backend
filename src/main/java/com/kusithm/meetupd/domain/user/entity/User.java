package com.kusithm.meetupd.domain.user.entity;

import com.kusithm.meetupd.common.entity.BaseEntity;
import com.kusithm.meetupd.domain.team.entity.TeamUser;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static com.kusithm.meetupd.domain.user.entity.Major.createMajor;
import static com.kusithm.meetupd.domain.user.entity.Task.createTask;
import static com.kusithm.meetupd.domain.user.entity.Ticket.*;

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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Major> majors = new ArrayList<>(); // 전공

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private UserLocation location;  // 지역

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Task> tasks = new ArrayList<>();   //희망 직무

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Internship> internships = new ArrayList<>();   // 인턴쉽

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Award> awards = new ArrayList<>(); // 수상내역

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Tool> tools = new ArrayList<>();   // 사용 툴

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Certificate> certificates = new ArrayList<>(); // 자격증

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Ticket ticket;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
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

        UserLocation createdlocation = UserLocation.createLocation(location);
        createdlocation.changeUser(user);
        Major createdMajor = createMajor(major);
        createdMajor.changeUser(user);
        Task createdTask = createTask(task);
        createdTask.changeUser(user);
        Ticket ticket = createInitTicket();
        ticket.changeUser(user);
        return user;
    }
    public void updateUserProfile(String username,
                                  Integer location,
                                  String major,
                                  String task,
                                  String selfIntroduction) {
        this.username = username;
        this.location.changeLocationType(location);
        this.majors.get(0).changeMajor(major);
        this.tasks.get(0).changeTask(task);
        this.selfIntroduction = selfIntroduction;
    }

    public void updateUserProfile(List<String> internships,
                                  List<String> awards,
                                  List<String> tools,
                                  List<String> certificates) {
        this.internships.clear();
        this.awards.clear();
        this.tools.clear();
        this.certificates.clear();

        List<Internship> createInternships = internships.stream()
                .map(Internship::createInternship).toList();
        createInternships.forEach(internship -> internship.changeUser(this));

        List<Award> createAwards = awards.stream()
                .map(Award::createAward).toList();
        createAwards.forEach(award -> award.changeUser(this));

        List<Tool> createTools = tools.stream()
                .map(Tool::createTool).toList();
        createTools.forEach(tool -> tool.changeUser(this));

        List<Certificate> createCertificates = certificates.stream()
                .map(Certificate::createCertificate).toList();
        createCertificates.forEach(certificate -> certificate.changeUser(this));
    }

    public void updateLocation(UserLocation location) {
        this.location = location;
    }

    public void updateTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public void addMajor(Major major) {
        this.majors.add(major);
    }

    public void addTask(Task task) {
        this.tasks.add(task);
    }

    public Integer getTicketCount() {
        return this.ticket.getCount();
    }

    ///

    public void addInternship(Internship internship) {
        this.internships.add(internship);
    }

    public void addAward(Award award) {
        this.awards.add(award);
    }

    public void addTool(Tool tool) {
        this.tools.add(tool);
    }

    public void addCertificate(Certificate certificate) {
        this.certificates.add(certificate);
    }
}
