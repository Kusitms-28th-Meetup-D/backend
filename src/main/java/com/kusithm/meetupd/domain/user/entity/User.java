package com.kusithm.meetupd.domain.user.entity;

import com.kusithm.meetupd.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

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
