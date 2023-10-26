package com.kusithm.meetupd.common.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kusithm.meetupd.common.error.ApplicationException;
import com.kusithm.meetupd.common.error.ErrorCode;
import com.kusithm.meetupd.common.error.UnauthorizedException;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Slf4j
@Component
public class TokenProvider {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.access-expiration-hours}")
    private long accessExpirationHours;;

    @Value("${jwt.refresh-expiration-hours}")
    private long refreshExpirationHours;
    private final ObjectMapper objectMapper = new ObjectMapper();
    public static final String ACCESS_TOKEN = "Access_Token";
    public static final String REFRESH_TOKEN = "Refresh_Token";

    // access token 발급 method
    public String createAccessToken(Long userId) {
        return Jwts.builder()
                .signWith(new SecretKeySpec(secretKey.getBytes(), SignatureAlgorithm.HS512.getJcaName()))   // HS512 알고리즘을 사용하여 secretKey를 이용해 서명
                .setSubject(String.valueOf(userId))  // JWT 토큰 제목
                .setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))    // JWT 토큰 발급 시간
                .setExpiration(Date.from(Instant.now().plus(accessExpirationHours, ChronoUnit.HOURS)))    // JWT 토큰 만료 시간
                .compact(); // JWT 토큰 생성
    }

    // refresh token 발급 method
    public String createRefreshToken() {
        return Jwts.builder()
                .signWith(new SecretKeySpec(secretKey.getBytes(), SignatureAlgorithm.HS512.getJcaName()))   // HS512 알고리즘을 사용하여 secretKey를 이용해 서명
                .setExpiration(Date.from(Instant.now().plus(refreshExpirationHours, ChronoUnit.HOURS)))    // JWT 토큰 만료 시간
                .compact(); // JWT 토큰 생성
    }

    public String validateTokenAndGetSubject(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // 토큰 subject꺼내기 (유저 id)
    public Long getTokenSubject(String token) {
        return Long.valueOf(Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject());
    }


    // 액세스 토큰 검증
    public Boolean validateAccessToken(String accessToken) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey.getBytes()).build().parseClaimsJws(accessToken);
            return true;
        } catch (ExpiredJwtException e) {
            log.error(e.getMessage());
            throw new UnauthorizedException(ErrorCode.EXPIRED_JWT_ACCESS_TOKEN);
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            log.error(e.getMessage());
            throw new UnauthorizedException(ErrorCode.INVALID_JWT_ACCESS_TOKEN);
        }
    }

    // 리프레시 토큰 검증
    public Boolean validateRefreshToken(String refreshToken) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey.getBytes()).build().parseClaimsJws(refreshToken);
            return true;
        } catch (ExpiredJwtException e) {
            log.error(e.getMessage());
            throw new UnauthorizedException(ErrorCode.EXPIRED_JWT_REFRESH_TOKEN);
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            log.error(e.getMessage());
            throw new UnauthorizedException(ErrorCode.INVALID_JWT_REFRESH_TOKEN);
        }
    }


    private Claims getClaimsFormToken(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(secretKey.getBytes())
                    .parseClaimsJws(token).getBody();
            return claims;
        } catch (Exception e){
            throw new ApplicationException(ErrorCode.INVALID_JWT_REFRESH_TOKEN);
        }
    }

    public Long getExpDateFromToken(String token) {
        Claims claims = getClaimsFormToken(token);

        return Long.parseLong(claims.get("exp").toString());
    }


    public String decodeJwtPayloadSubject(String oldAccessToken) throws JsonProcessingException {
        return objectMapper.readValue(
                new String(Base64.getDecoder().decode(oldAccessToken.split("\\.")[1]), StandardCharsets.UTF_8),
                Map.class
        ).get("sub").toString();
    }
}

