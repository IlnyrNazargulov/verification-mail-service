package ru.ilnur.verificationmailservice.web.exceptions;

import ru.ilnur.verificationmailservice.facade.exceptions.BaseException;

public class RefreshTokenExpiredException extends BaseException {
    public RefreshTokenExpiredException() {
        super("The specified refresh token is expired.");
    }
}
