package ru.ilnur.verificationmailservice.facade.exceptions;

public class RequestCodeAlreadyUsedException extends BaseException {
    public RequestCodeAlreadyUsedException() {
        super("Request code has already been used.");
    }
}
