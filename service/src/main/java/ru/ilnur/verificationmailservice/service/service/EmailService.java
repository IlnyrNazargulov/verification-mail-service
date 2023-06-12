package ru.ilnur.verificationmailservice.service.service;

import javax.mail.MessagingException;
import java.io.IOException;

public interface EmailService {
    void sendVerifyMessage(String email, String code) throws IOException, MessagingException;

    void sendResetPasswordMessage(String email, String plainPassword);
}
