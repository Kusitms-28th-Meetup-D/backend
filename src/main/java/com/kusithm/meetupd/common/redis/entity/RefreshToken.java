package com.kusithm.meetupd.common.redis.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Builder
@RedisHash(value = "refreshToken")
public class RefreshToken {

    @Id
    private Long userId;

    private String refreshToken;

    public static RefreshToken createRefreshToken(Long userId, String refreshToken) {
        return RefreshToken.builder()
                .userId(userId)
                .refreshToken(refreshToken)
                .build();
    }

    public void update(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}
