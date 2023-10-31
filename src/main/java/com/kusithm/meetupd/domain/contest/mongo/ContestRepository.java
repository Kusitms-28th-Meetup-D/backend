package com.kusithm.meetupd.domain.contest.mongo;

import com.kusithm.meetupd.domain.contest.entity.Contest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ContestRepository extends MongoRepository<Contest, String> {

    @Query(value = "{recruit_end : {$gte : ?0}}", sort = "{ recruit_end : 1}")
    List<Contest> findAllContestsByDate(LocalDate date);

    @Query(value = "{recruit_end : {$gte : ?0}, types :  {$eq : ?1}}", sort = "{ recruit_end : 1}")
    List<Contest> findContestsByDateAndType(LocalDate date, Integer num);


}
