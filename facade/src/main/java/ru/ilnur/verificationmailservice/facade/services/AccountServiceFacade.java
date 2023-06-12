package ru.ilnur.verificationmailservice.facade.services;

import ru.ilnur.verificationmailservice.facade.exceptions.*;
import ru.ilnur.verificationmailservice.facade.model.AccountFacade;
import ru.ilnur.verificationmailservice.facade.model.RequestCodeStatusFacade;
import ru.ilnur.verificationmailservice.facade.model.identities.AccountIdentity;
import ru.ilnur.verificationmailservice.facade.model.requests.UpdateAccountRequestFacade;

import java.io.IOException;

public interface AccountServiceFacade {
    RequestCodeStatusFacade createAccountAndRequestCode(String email, String plainPassword)
            throws EntityNotFoundException, EmailAlreadyUseException, IOException, UnableSendMessageException;

    void verifyEmail(String email, String code) throws WrongRequestCodeException, EntityNotFoundException, ExpiredRequestCodeException, RequestCodeAlreadyUsedException;

    void resetPassword(String email);

    AccountFacade changePassword(AccountIdentity accountIdentity, String newPassword) throws EntityNotFoundException;

    AccountFacade updateAccount(AccountIdentity accountIdentity, UpdateAccountRequestFacade changeUserInfoRequest)
            throws EntityNotFoundException;

    AccountFacade getByCredentials(String email, String plainPassword) throws WrongCredentialsException, AccountNotVerifiedException;

    AccountFacade getAccount(AccountIdentity accountIdentity) throws EntityNotFoundException;


}
