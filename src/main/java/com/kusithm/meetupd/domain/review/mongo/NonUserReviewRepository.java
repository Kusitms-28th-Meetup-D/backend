package com.kusithm.meetupd.domain.review.mongo;

import com.kusithm.meetupd.domain.review.entity.NonUserReview;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NonUserReviewRepository extends MongoRepository<NonUserReview, String> {

    Boolean existsByUserId(Long userId);

}
