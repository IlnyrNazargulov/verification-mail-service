package ru.ilnur.verificationmailservice.service.service;

import java.util.UUID;

public interface RandomService {
    UUID generateUUID();

    String generatePassword(int length);

    String generateCode(int length);
}
