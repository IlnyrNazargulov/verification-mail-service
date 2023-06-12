package ru.ilnur.verificationmailservice.facade.exceptions;

public class EmailAlreadyUseException extends BaseException {
    public EmailAlreadyUseException(String email) {
        super("Email " + email + " is already in use.");
    }
}
