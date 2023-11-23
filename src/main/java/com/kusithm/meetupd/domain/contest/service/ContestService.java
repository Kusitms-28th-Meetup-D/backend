package com.kusithm.meetupd.domain.contest.service;


import com.kusithm.meetupd.common.error.EntityNotFoundException;
import com.kusithm.meetupd.common.error.ErrorCode;
import com.kusithm.meetupd.domain.contest.dto.response.FindContestsResponseDto;
import com.kusithm.meetupd.domain.contest.dto.response.GetContestDetailInfoResponseDto;
import com.kusithm.meetupd.domain.contest.dto.response.GetMainRecommendationResponseDto;
import com.kusithm.meetupd.domain.contest.entity.Contest;
import com.kusithm.meetupd.domain.contest.entity.ContestType;
import com.kusithm.meetupd.domain.contest.mongo.ContestRepository;
import com.kusithm.meetupd.domain.review.entity.Review;
import com.kusithm.meetupd.domain.review.mongo.ReviewRepository;
import com.kusithm.meetupd.domain.team.dto.response.RecruitingTeamResponseDto;
import com.kusithm.meetupd.domain.team.entity.Team;
import com.kusithm.meetupd.domain.team.entity.TeamUser;
import com.kusithm.meetupd.domain.team.mysql.TeamRepository;
import com.kusithm.meetupd.domain.team.mysql.TeamUserRepository;
import com.kusithm.meetupd.domain.user.entity.User;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.kusithm.meetupd.common.error.ErrorCode.USER_REVIEW_NOT_FOUND;
import static com.kusithm.meetupd.domain.contest.dto.response.FindContestsResponseDto.createListOf;
import static com.kusithm.meetupd.domain.team.entity.TeamProgressType.RECRUITMENT_COMPLETED;
import static com.kusithm.meetupd.domain.team.entity.TeamUserRoleType.TEAM_LEADER;

@RequiredArgsConstructor
@Service
public class ContestService {

    private final ContestRepository contestRepository;
    private final ReviewRepository reviewRepository;
    private final TeamRepository teamRepository;
    private final TeamUserRepository teamUserRepository;
    private final MongoConverter mongoConverter;
    private final MongoClient mongoClient;

    public List<FindContestsResponseDto> findContestsByCategory(Integer contestType) {
        // 카테고리 전체 조회일 때
        if(isFindAllContest(contestType)) {
            return findAllContests(LocalDate.now());
        }
        else {
            validContestType(contestType);
            return findContestsByType(LocalDate.now(), contestType);
        }
    }

    public List<FindContestsResponseDto> findContestsBySearchText(String searchText) {
        List<Contest> contests = getContestsBySearchText(searchText);
        return createListOf(contests, LocalDate.now());
    }

    public GetContestDetailInfoResponseDto getContestDetailById(String contestId) {
        Contest findContest = getContestById(contestId);
        List<Team> teams = findAllRecuritTeamByContestTitle(contestId);
        Double averageContestUserComments = 0.0;
        for (Team team : teams) {
            averageContestUserComments += getTeamAverageCommentsCount(team);
        }
        averageContestUserComments /= teams.size();
        return GetContestDetailInfoResponseDto.of(findContest, LocalDate.now(),  Math.floor(averageContestUserComments * 10) / 10);
    }

    public GetMainRecommendationResponseDto getMainRecommendContestsAndTeams() {
        List<Contest> recommendationContests = contestRepository.findRecommendationSixContests(LocalDate.now());
        List<Team> popularTeams = teamRepository.findPopularTeams();
        List<RecruitingTeamResponseDto> recruitingTeamResponseDtos = createRecruitingTeamResponseDtos(popularTeams);
        return GetMainRecommendationResponseDto.of(recommendationContests, recruitingTeamResponseDtos, LocalDate.now());
    }

    private List<Team> findAllRecuritTeamByContestTitle(String contestId) {
        return teamRepository.findAllByContestIdAndProgressLessThanEqual(contestId, RECRUITMENT_COMPLETED.getNumber());
    }

    private Double getTeamAverageCommentsCount(Team team) {
        Double averageTeamUserComments = 0.0;
        List<TeamUser> teamUsers = team.getTeamUsers();
        for (TeamUser teamUser : teamUsers) {
            averageTeamUserComments += getTeamUserCommentCount(teamUser);
        }
        return averageTeamUserComments / teamUsers.size();
    }

    private Integer getTeamUserCommentCount(TeamUser teamUser) {
        User user = teamUser.getUser();
        Review review = findReviewByUserId(user);
        return review.getReviewComments().size();
    }

    private Review findReviewByUserId(User user) {
        return reviewRepository.findByUserId(user.getId())
                .orElseThrow(()-> new EntityNotFoundException(USER_REVIEW_NOT_FOUND));
    }

    public List<Contest> findRecruitEndContests() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(1);
        return findTodayRecruitEndContestsByDate(startDate, endDate);
    }

    private List<Contest> findTodayRecruitEndContestsByDate(LocalDate startDate, LocalDate endDate) {
        return contestRepository.findAllEndContestsToday(startDate, endDate);
    }

    private List<Contest> getContestsBySearchText(String searchText) {
        MongoDatabase database = mongoClient.getDatabase("wanteam-db");
        MongoCollection<Document> collection = database.getCollection("contest");
        return searchContestByText(searchText, collection);
    }

    private List<Contest> searchContestByText(String searchText, MongoCollection<Document> collection) {
        AggregateIterable<Document> result = collection.aggregate(Arrays.asList(
                new Document("$search",
                        new Document("index", "wanteam-db-contest")
                                .append("text", new Document("query", searchText)
                                        .append("path", new Document("wildcard", "*"))))));

        List<Contest> contests = new ArrayList<>();
        result.forEach(doc -> contests.add(mongoConverter.read(Contest.class, doc)));
        return contests;
    }

    private List<RecruitingTeamResponseDto> createRecruitingTeamResponseDtos(List<Team> popularTeams) {
        List<RecruitingTeamResponseDto> recruitingTeamResponseDtos = new ArrayList<>();
        for (Team popularTeam : popularTeams) {
            Contest teamContest = getContestById(popularTeam.getContestId());
            for (TeamUser teamuser : popularTeam.getTeamUsers()) {
                if (teamuser.getRole().equals(TEAM_LEADER.getCode())) {
                    recruitingTeamResponseDtos.add(
                            new RecruitingTeamResponseDto(teamContest,
                                    popularTeam,
                                    teamuser.getUser())
                    );
                }
            }
        }
        if (recruitingTeamResponseDtos.size() > 4) {
            recruitingTeamResponseDtos = recruitingTeamResponseDtos.subList(0, 4);
        }
        return recruitingTeamResponseDtos;
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

    private Contest getContestById(String contestId) {
        return contestRepository.findContestById(new ObjectId(contestId))
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.CONTEST_NOT_FOUND));
    }

}
