package com.kusithm.meetupd.domain.contest.service;


import com.kusithm.meetupd.domain.contest.dto.response.FindContestsResponseDto;
import com.kusithm.meetupd.domain.contest.entity.Contest;
import com.kusithm.meetupd.domain.contest.entity.ContestType;
import com.kusithm.meetupd.domain.contest.mongo.ContestRepository;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.kusithm.meetupd.domain.contest.dto.response.FindContestsResponseDto.createListOf;

@RequiredArgsConstructor
@Service
public class ContestService {

    private final ContestRepository contestRepository;
    private final MongoConverter mongoConverter;
    private final MongoClient mongoClient;

    public List<FindContestsResponseDto> findContestsByCategory(LocalDate nowDate, Integer contestType) {
        // 카테고리 전체 조회일 때
        if(isFindAllContest(contestType)) {
            return findAllContests(nowDate);
        }
        else {
            validContestType(contestType);
            return findContestsByType(nowDate, contestType);
        }
    }

    public List<FindContestsResponseDto> findContestsBySearchText(String searchText) {

        List<Contest> contests = getContestsBySearchTextInMongoDB(searchText);

        return createListOf(contests, LocalDate.now());
    }

    private List<Contest> getContestsBySearchTextInMongoDB(String searchText) {
        MongoDatabase database = mongoClient.getDatabase("wanteam-db");
        MongoCollection<Document> collection = database.getCollection("contest");
        AggregateIterable<Document> result = collection.aggregate(Arrays.asList(
                new Document("$search",
                        new Document("index", "wanteam-db-contest")
                        .append("text", new Document("query", searchText)
                                .append("path", new Document("wildcard", "*"))))));

        List<Contest> contests = new ArrayList<>();
        result.forEach(doc -> contests.add(mongoConverter.read(Contest.class, doc)));
        return contests;
    }

    private List<FindContestsResponseDto> findAllContests(LocalDate nowDate) {
        List<Contest> findContests = findContestsByDate(nowDate);
        return createListOf(findContests, nowDate);
    }

    private List<FindContestsResponseDto> findContestsByType(LocalDate nowDate, Integer contestType) {
        List<Contest> findContests = findTypeContestsByDate(nowDate, contestType);

        return createListOf(findContests, nowDate);
    }

    private List<Contest> findContestsByDate(LocalDate nowDate) {
        return contestRepository.findAllContestsByDate(nowDate);

    }


    private List<Contest> findTypeContestsByDate(LocalDate nowDate, Integer contentType) {
        return contestRepository.findContestsByDateAndType(nowDate, contentType);

    }

    private Boolean isFindAllContest(Integer contestType){
        return contestType.equals(0);
    }

    private void validContestType(Integer contestType) {
        ContestType.ofCode(contestType);
    }
}
