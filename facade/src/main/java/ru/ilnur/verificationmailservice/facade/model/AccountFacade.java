package ru.ilnur.verificationmailservice.facade.model;

import ru.ilnur.verificationmailservice.facade.model.enums.Role;

import java.time.Instant;

public interface AccountFacade {
    Integer getId();

    String getFullName();

    Instant getCreatedAt();

    String getEmail();

    Role getRole();
}
