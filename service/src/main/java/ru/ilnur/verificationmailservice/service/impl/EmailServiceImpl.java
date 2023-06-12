package ru.ilnur.verificationmailservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import ru.ilnur.verificationmailservice.service.config.EmailProperties;
import ru.ilnur.verificationmailservice.service.service.EmailService;
import ru.ilnur.verificationmailservice.service.service.MailSenderService;

import javax.mail.MessagingException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final MailSenderService mailSenderService;
    private final EmailProperties emailProperties;
    @Value("classpath:verify-email.html")
    private Resource verifyEmail;

    @Override
    public void sendVerifyMessage(String email, String code) throws IOException, MessagingException {
        String template = getText(verifyEmail.getInputStream());
        String message = template
                .replaceAll("#code#", code)
                .replaceAll("#email#", email)
                .replaceAll("#host-path#", emailProperties.getHostPath());
        mailSenderService.sendMimeMessage(email, emailProperties.getSubjectValidationEmail(), message);
        log.info("Email successfully sent.");
    }

    @Override
    public void sendResetPasswordMessage(String email, String plainPassword) {
        String message = emailProperties.getTemplateResetPassword()
                .replaceAll("#plain-password#", plainPassword);

        mailSenderService.sendSimpleMessage(email, emailProperties.getSubjectResetPassword(), message);
        log.info("Email successfully sent.");
    }

    private String getText(InputStream inputStream) {
        return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));
    }
}

