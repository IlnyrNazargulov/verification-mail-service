package ru.ilnur.verificationmailservice.service.impl;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class PasswordService {

    public String hashPassword(String plainPassword) {
        String hashPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
        return hashPassword;
    }

    public boolean comparePassword(String plainPassword, String hashPassword) {
        return BCrypt.checkpw(plainPassword, hashPassword);
    }
}
