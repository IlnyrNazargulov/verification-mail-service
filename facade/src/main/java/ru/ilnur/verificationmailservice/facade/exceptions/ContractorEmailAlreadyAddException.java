package ru.ilnur.verificationmailservice.facade.exceptions;

public class ContractorEmailAlreadyAddException extends BaseException {
    public ContractorEmailAlreadyAddException(String email) {
        super("Email " + email + " already added to the list.");
    }
}
