package ru.ilnur.verificationmailservice.facade.exceptions;

public class ExpiredRequestCodeException extends BaseException {
    public ExpiredRequestCodeException() {
        super("The request code has expired.");
    }
}
