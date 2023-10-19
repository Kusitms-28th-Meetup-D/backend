package com.kusithm.meetupd.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity(name = "WANTEAM_USER")
public class User {

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
