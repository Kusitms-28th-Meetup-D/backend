package com.kusithm.meetupd.domain.team.mysql;

import com.kusithm.meetupd.domain.team.entity.TeamUser;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface TeamUserRepository extends JpaRepository<TeamUser,Long> {
    boolean existsByUserId(Long userId);
    boolean existsByRoleAndUserId(Integer role, Long userId);
    boolean existsByUserIdAndTeamId(Long userId, Long teamId);
    List<TeamUser> findAllByRoleAndTeamId(Integer code, Long teamId);
    Optional<TeamUser> findByUserIdAndTeamId(Long userId, Long teamId);
    List<TeamUser> findAllByTeamIdAndRole(Long teamId, Integer role);

    List<TeamUser> findAllByUserIdAndRole(Long userId, Integer role);

    List<TeamUser> findAllByUserIdAndRoleGreaterThanEqual(Long userId, Integer role);

    List<TeamUser> findAllByUserIdAndRoleLessThanEqual(Long userId, Integer role);

    List<TeamUser> findAllByTeamIdAndRoleLessThanEqual(Long teamId, Integer role);
}
