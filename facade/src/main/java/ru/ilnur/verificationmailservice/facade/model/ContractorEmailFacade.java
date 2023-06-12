package ru.ilnur.verificationmailservice.facade.model;

import ru.ilnur.verificationmailservice.facade.model.enums.AccountType;

public interface ContractorEmailFacade {
    String getEmail();

    AccountType getAccountType();
}
