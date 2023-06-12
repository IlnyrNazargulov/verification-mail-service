package ru.ilnur.verificationmailservice.service.service;

import javax.mail.MessagingException;

public interface MailSenderService {
    void sendSimpleMessage(String emailTo, String subject, String message);

    void sendMimeMessage(String emailTo, String subject, String message) throws MessagingException;
}
