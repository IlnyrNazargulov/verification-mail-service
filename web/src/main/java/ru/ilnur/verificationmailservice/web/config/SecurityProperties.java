package ru.ilnur.verificationmailservice.web.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Getter
@Setter
@ConfigurationProperties("ru.ilnur.verificationmailservice.security")
public class SecurityProperties {
    private Duration accessTokenTTL;
    private Duration refreshTokenTTL;
    private String signingKey;
    private String issuer;
}
