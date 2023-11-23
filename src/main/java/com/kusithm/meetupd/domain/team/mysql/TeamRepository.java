package com.kusithm.meetupd.domain.team.mysql;

import com.kusithm.meetupd.domain.team.entity.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
    List<Team> findAllByProgress(int teamProgress);
    Page<Team> findAllByProgress(int teamProgress, Pageable pageable);
    List<Team> findAllByContestIdAndProgress(String contestId, Integer teamProgress);
    Optional<List<Team>> findAllByIdAndProgress(Long userId, Integer progress);
    List<Team> findAllByReviewDateLessThanAndProgress(Date date, Integer Progress);
    List<Team> findAllByContestIdAndProgressLessThanEqual(String contestId, Integer teamProgress);

    @Query( "SELECT t " +
            "FROM Team t " +
            "WHERE t.progress = 1 " +
            "AND t.id IN (" +
            "SELECT tu.team.id " +
            "FROM TeamUser tu " +
            "WHERE tu.team = t " +
            "AND tu.role IN (2, 4) " +
            "GROUP BY tu.team.id " +
            "ORDER BY COUNT(tu.team.id) DESC" +
            ") " +
            "ORDER BY (" +
            "SELECT COUNT(tu2) " +
            "FROM TeamUser tu2 " +
            "WHERE tu2.team = t " +
            "AND tu2.role IN (2, 4)" +
            ") DESC, t.id DESC"

    )
    List<Team> findPopularTeams();
}
