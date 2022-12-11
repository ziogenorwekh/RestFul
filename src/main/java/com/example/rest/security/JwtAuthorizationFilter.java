package com.example.rest.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.rest.exception.NotFoundMember;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final CustomizedMemberDetailsService detailsService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, CustomizedMemberDetailsService detailsService) {
        super(authenticationManager);
        this.detailsService = detailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.debug("doFilterInternal");
        String header = request.getHeader(AUTHORIZATION);
//        헤더에 값이 없거나,  Bearer-> JWT 토큰으로 시작하지 않거나, 로그인일 경우 흘려보냄
        if (header == null || !header.startsWith("Bearer ") || request.getServletPath().equals("/login")) {
            chain.doFilter(request, response);
            return;
        }
//        CustomMemberDetails 생성 후 호출
        Authentication customizedMemberDetails = this.getCustomizedMemberDetails(header, response);
        // 권한 인증 진행
        SecurityContextHolder.getContext().setAuthentication(customizedMemberDetails);
        chain.doFilter(request, response);
    }

    private Authentication getCustomizedMemberDetails(String header, HttpServletResponse response) {
        String token = header.substring("Bearer ".length());
        String memberId = JWT.decode(token).getSubject();
        CustomizedMemberDetails userDetails;
        UsernamePasswordAuthenticationToken authenticationToken = null;
        try {
            userDetails = (CustomizedMemberDetails) detailsService.loadMemberByMemberId(memberId);
            authenticationToken = new UsernamePasswordAuthenticationToken(userDetails.getMember(), null, userDetails.getAuthorities());
        } catch (NotFoundMember e) {
            exceptionResponse(response, e.getMessage(), SC_NOT_FOUND);
        } catch (TokenExpiredException e) {
            exceptionResponse(response, e.getMessage(), SC_GONE);
        } catch (Exception e) {
            exceptionResponse(response, e.getMessage(), SC_INTERNAL_SERVER_ERROR);
        }
        return authenticationToken;
    }

    private void exceptionResponse(HttpServletResponse response, String error, int status) {
        response.setStatus(status);
        response.setHeader("error", error);
    }
}
