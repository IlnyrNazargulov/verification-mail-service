package ru.ilnur.verificationmailservice.facade.exceptions;

public class UnableSendMessageException extends BaseException {

    public UnableSendMessageException() {
        super("Unable to send message.");
    }
}
