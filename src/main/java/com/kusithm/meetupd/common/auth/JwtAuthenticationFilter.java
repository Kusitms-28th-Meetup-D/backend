package com.kusithm.meetupd.common.auth;

import com.kusithm.meetupd.common.error.ErrorCode;
import com.kusithm.meetupd.common.error.ForbiddenException;
import com.kusithm.meetupd.common.jwt.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = getAccessTokenFromHttpServletRequest(request);
        tokenProvider.validateAccessToken(accessToken);
        Long userId = tokenProvider.getTokenSubject(accessToken);
        setAuthentication(request, userId);
        filterChain.doFilter(request, response);
    }


    private String getAccessTokenFromHttpServletRequest(HttpServletRequest request) {
        String accessToken = request.getHeader(AuthConstants.AUTH_HEADER);
        if (StringUtils.hasText(accessToken) && accessToken.startsWith(AuthConstants.TOKEN_TYPE)) {
            return accessToken.split(" ")[1];
        }
        throw new ForbiddenException(ErrorCode.UNAUTHORIZED);
    }


    private void setAuthentication(HttpServletRequest request, Long userId) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId, null, null);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
