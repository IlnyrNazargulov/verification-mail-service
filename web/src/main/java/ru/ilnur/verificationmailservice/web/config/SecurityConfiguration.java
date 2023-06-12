package ru.ilnur.verificationmailservice.web.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import ru.ilnur.verificationmailservice.facade.model.enums.Role;
import ru.ilnur.verificationmailservice.web.model.AnonymousPrincipal;

import java.time.Instant;
import java.util.Date;

@EnableWebSecurity
@EnableOAuth2Client
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .headers()
                .cacheControl().disable()
                .frameOptions().disable()
                .httpStrictTransportSecurity().disable()
                .xssProtection().disable()
            .and()
            .csrf().disable()
            .cors().disable()
            .formLogin().disable()
            .logout().disable()
            .httpBasic().disable()
            .jee().disable()
            .anonymous().authorities(Role.ANONYMOUS).principal(new AnonymousPrincipal()).and()
            .portMapper().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .oauth2ResourceServer()
                .authenticationManagerResolver(context -> http.getSharedObject(AuthenticationManager.class));
        // @formatter:on
    }

    @Bean
    public Algorithm jwtSigningAlgorithm(SecurityProperties securityProperties) {
        return Algorithm.HMAC256(securityProperties.getSigningKey());
    }

    @Bean
    public JWTVerifier jwtVerifier(Algorithm algorithm, SecurityProperties securityProperties) {
        JWTVerifier.BaseVerification builder = (JWTVerifier.BaseVerification) JWT
                .require(algorithm)
                .withIssuer(securityProperties.getIssuer());
        return builder.build(() -> Date.from(Instant.now()));
    }
}
