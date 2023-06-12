package ru.ilnur.verificationmailservice.service.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("ru.ilnur.verificationmailservice.refresh-token")
public class RefreshTokenProperties {
    private int maxTokensPerHour;
}
