package ru.ilnur.verificationmailservice.service.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("ru.ilnur.verificationmailservice.email")
public class EmailProperties {
    private String serviceEmail;
    private String templateValidationEmail;
    private String subjectValidationEmail;
    private String templateResetPassword;
    private String subjectResetPassword;
    private String hostPath;
}
