package ru.ilnur.verificationmailservice.facade.exceptions;

import lombok.Getter;

@Getter
public class WrongRequestCodeException extends BaseException {

    public WrongRequestCodeException(String email, String code) {
        super("Request code " + code + " for email " + email + " is invalid.");
    }
}
