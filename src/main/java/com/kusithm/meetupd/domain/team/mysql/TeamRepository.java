package com.kusithm.meetupd.domain.team.mysql;

import com.kusithm.meetupd.domain.team.entity.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
    List<Team> findAllByProgress(int teamProgress);
    Page<Team> findAllByProgress(int teamProgress, Pageable pageable);
    List<Team> findAllByContestIdAndProgress(String contestId, Integer teamProgress);
    Optional<List<Team>> findAllByIdAndProgress(Long userId, Integer progress);
    List<Team> findAllByReviewDateLessThanAndProgress(Date date, Integer Progress);
}
