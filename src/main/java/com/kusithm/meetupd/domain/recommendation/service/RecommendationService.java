package com.kusithm.meetupd.domain.recommendation.service;

import com.kusithm.meetupd.domain.recommendation.entity.Recommendation;
import com.kusithm.meetupd.domain.recommendation.mongo.RecommendationRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;
    private final MongoTemplate mongoTemplate;

}
