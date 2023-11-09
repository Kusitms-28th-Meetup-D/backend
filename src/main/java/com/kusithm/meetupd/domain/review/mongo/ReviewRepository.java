package com.kusithm.meetupd.domain.review.mongo;

import com.kusithm.meetupd.domain.review.entity.Review;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ReviewRepository extends MongoRepository<Review, String> {

    Optional<Review> findByUserId(Long userId);
}
