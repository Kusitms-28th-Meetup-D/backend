package com.kusithm.meetupd.domain.review.mongo;

import com.kusithm.meetupd.domain.review.entity.WaitReview;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface WaitReviewRepository extends MongoRepository<WaitReview, String> {

    List<WaitReview> findAllByUserIdAndTeamId(Long userId, Long teamId);

    Boolean existsByUserId(Long userId);
}
