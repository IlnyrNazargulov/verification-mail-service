package ru.ilnur.verificationmailservice.service.service;

import ru.ilnur.verificationmailservice.dal.model.accounts.Account;
import ru.ilnur.verificationmailservice.facade.exceptions.EntityNotFoundException;
import ru.ilnur.verificationmailservice.facade.model.identities.AccountIdentity;

public interface AccountService {
    Account getAccount(AccountIdentity accountIdentity) throws EntityNotFoundException;
}
