package com.kusithm.meetupd.domain.review.mysql;

import com.kusithm.meetupd.domain.review.entity.UserReviewedTeam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserReviewedTeamRepository extends JpaRepository<UserReviewedTeam, Long> {

    Boolean existsByUserIdAndTeamId(Long userId, Long teamId);
}
