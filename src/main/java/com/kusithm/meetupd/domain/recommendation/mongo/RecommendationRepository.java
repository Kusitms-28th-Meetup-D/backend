package com.kusithm.meetupd.domain.recommendation.mongo;

import com.kusithm.meetupd.domain.recommendation.entity.Recommendation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RecommendationRepository extends MongoRepository<Recommendation, String> {

}
