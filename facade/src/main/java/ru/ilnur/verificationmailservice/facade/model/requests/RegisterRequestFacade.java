package ru.ilnur.verificationmailservice.facade.model.requests;

public interface RegisterRequestFacade {
    String getSecondName();

    String getFirstName();

    String getPatronymic();

    String getLogin();

    String getPlainPassword();
}
