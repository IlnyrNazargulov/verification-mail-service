package ru.ilnur.verificationmailservice.facade.model;

public interface RequestCodeStatusFacade {

    int getNextAttemptAfter();

    boolean isCodeSent();
}
