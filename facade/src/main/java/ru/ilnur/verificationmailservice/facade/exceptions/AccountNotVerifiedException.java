package ru.ilnur.verificationmailservice.facade.exceptions;

public class AccountNotVerifiedException extends BaseException {
    public AccountNotVerifiedException(String email) {
        super("Account with email " + email + " not verified.");
    }
}
