package com.kusithm.meetupd.common.redis.repository;


import com.kusithm.meetupd.common.redis.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
}
