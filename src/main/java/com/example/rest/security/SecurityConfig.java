package com.example.rest.security;


import com.example.rest.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private CustomizedMemberDetailsService memberService;
    private AuthenticationConfiguration authenticationConfiguration;

    @Value("${jwt.secretCode}")
    private String secretCode;

    @Autowired
    public SecurityConfig(CustomizedMemberDetailsService memberService,
                          AuthenticationConfiguration authenticationConfiguration) {
        this.memberService = memberService;
        this.authenticationConfiguration = authenticationConfiguration;
    }

    @Bean
    @SneakyThrows(Exception.class)
    public SecurityFilterChain filterChain(HttpSecurity http) {

        JwtAuthenticationFilter authenticationFilter =
                new JwtAuthenticationFilter(authenticationManager(authenticationConfiguration), secretCode);
        JwtAuthorizationFilter authorizationFilter =
                new JwtAuthorizationFilter(authenticationManager(authenticationConfiguration), memberService);
        // csrf disable
        http.csrf().disable();
//        Http basic Auth 기반 제거
        http.httpBasic().disable();
//        form 기반 로그인 제거
        http.formLogin().disable();
//        무상태 설정
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//        // /api/admin/ 과 관련된 모든 uri에 권한 설정
        http.authorizeRequests()
                .antMatchers("/api/admin/**")
                .hasRole("USER");
//
//        // 그 이외의 나머지 요청에는 모든 접근 권한을 허용한다.
        http.authorizeRequests().antMatchers("/**").permitAll();
        http.addFilter(authenticationFilter)
                .addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }


//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails userDetails = User.builder()
//                .username("lsek")
//                .password("{noop}123456")
//                .roles("USER")
//                .build();
//        return new InMemoryUserDetailsManager(userDetails);
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
