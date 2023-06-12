package ru.ilnur.verificationmailservice.web.model;

import ru.ilnur.verificationmailservice.facade.model.identities.AccountIdentity;

public interface TokenPrincipal {
    AccountIdentity getAccountIdentity();
}
