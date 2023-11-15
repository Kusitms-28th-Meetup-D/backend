package com.kusithm.meetupd.domain.team.mysql;

import com.kusithm.meetupd.domain.team.entity.TeamUser;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface TeamUserRepository extends JpaRepository<TeamUser,Long> {
    boolean existsByUserId(Long userId);
    boolean existsByRoleAndUserId(Integer role, Long userId);
    List<TeamUser> findAllByRoleAndTeamId(Integer code, Long teamId);
}
