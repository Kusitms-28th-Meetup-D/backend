package com.kusithm.meetupd.domain.contest.mongo;

import com.kusithm.meetupd.domain.contest.entity.Contest;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ContestRepository extends MongoRepository<Contest, String> {
}
