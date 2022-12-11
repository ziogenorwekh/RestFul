package com.example.rest.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Verification;
import com.example.rest.vo.LoginVo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private final String secretCode;


    @Override
    @SneakyThrows(IOException.class)
    public Authentication attemptAuthentication(HttpServletRequest request,

                                                HttpServletResponse response) throws AuthenticationException {
        LoginVo login = new ObjectMapper().readValue(request.getInputStream(), LoginVo.class);
        log.debug(login.toString());
        UsernamePasswordAuthenticationToken authenticationToken = null;
        try {
            authenticationToken =
                    new UsernamePasswordAuthenticationToken(login.getName(), login.getPassword());
        } catch (Exception e) {
            log.debug(e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType(APPLICATION_JSON_VALUE);
            response.addHeader("error", "Login Error");
        }
        log.debug(authenticationToken.toString());
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {
        Map<String, String> map = new HashMap<>();
        CustomizedMemberDetails details = (CustomizedMemberDetails) authResult.getPrincipal();
        String accessToken = JWT.create().withSubject(details.getMember().getMemberId())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1 * 60 * 1000))
                .withAudience(details.getUsername())
                .withNotBefore(new Date())
                .sign(Algorithm.HMAC256(secretCode.getBytes()));
        map.put("access", accessToken);

        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), map);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.debug(failed.getMessage());
        response.setHeader("error", "Wrong Login");
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}
