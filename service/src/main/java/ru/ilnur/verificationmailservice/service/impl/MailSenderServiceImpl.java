package ru.ilnur.verificationmailservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import ru.ilnur.verificationmailservice.service.service.MailSenderService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
@RequiredArgsConstructor
public class MailSenderServiceImpl implements MailSenderService {

    private final JavaMailSender mailSender;

    @Override
    public void sendSimpleMessage(String emailTo, String subject, String message) {
        String from = ((JavaMailSenderImpl) mailSender).getUsername();
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(emailTo);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        mailMessage.setFrom(from);
        mailSender.send(mailMessage);
    }

    @Override
    public void sendMimeMessage(String emailTo, String subject, String message) throws MessagingException {
        String from = ((JavaMailSenderImpl) mailSender).getUsername();
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        helper.setText(message, true);
        helper.setTo(emailTo);
        helper.setSubject(subject);
        helper.setFrom(from);

        mailSender.send(mimeMessage);
    }
}
