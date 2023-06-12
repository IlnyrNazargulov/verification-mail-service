package ru.ilnur.verificationmailservice.service.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("ru.ilnur.verificationmailservice.service.request-code")
public class RequestCodeProperties {
    private int maxCodeRequestsPerInterval;
    private long requestCodeInterval;
    private long minInterval;
    private long expiresIn;
}
