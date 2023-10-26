package com.kusithm.meetupd.domain.user.mysql;


import com.kusithm.meetupd.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByKakaoId(Long kakaoId);

    Boolean existsByKakaoId(Long kakaoId);
}
