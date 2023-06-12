package ru.ilnur.verificationmailservice.web.model;

import org.springframework.security.access.AccessDeniedException;
import ru.ilnur.verificationmailservice.facade.model.identities.AccountIdentity;

public class AnonymousPrincipal implements TokenPrincipal {
    @Override
    public AccountIdentity getAccountIdentity() {
        throw new AccessDeniedException("Anonymous user has not identity.");
    }
}
