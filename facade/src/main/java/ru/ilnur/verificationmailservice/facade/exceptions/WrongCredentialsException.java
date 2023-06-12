package ru.ilnur.verificationmailservice.facade.exceptions;

public class WrongCredentialsException extends BaseException {
    public WrongCredentialsException(String login) {
        this(login, false);
    }

    public WrongCredentialsException(String login, boolean password) {
        super(password ? "Wrong password for user " + login : "User with login " + login + " does not exists.");
    }
}
