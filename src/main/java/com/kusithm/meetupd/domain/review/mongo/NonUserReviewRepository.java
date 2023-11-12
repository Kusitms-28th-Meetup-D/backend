package com.kusithm.meetupd.domain.review.mongo;

import com.kusithm.meetupd.domain.review.entity.NonUserReview;
import com.kusithm.meetupd.domain.review.entity.WaitReview;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NonUserReviewRepository extends MongoRepository<NonUserReview, String> {

    Boolean existsByUserId(Long userId);

}
