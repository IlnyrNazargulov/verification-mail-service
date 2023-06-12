package ru.ilnur.verificationmailservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.ilnur.verificationmailservice.dal.model.ContractorEmail;
import ru.ilnur.verificationmailservice.dal.model.RequestCode;
import ru.ilnur.verificationmailservice.dal.model.accounts.Account;
import ru.ilnur.verificationmailservice.dal.model.accounts.AdminAccount;
import ru.ilnur.verificationmailservice.dal.model.accounts.SellerAccount;
import ru.ilnur.verificationmailservice.dal.repositories.AccountRepository;
import ru.ilnur.verificationmailservice.facade.exceptions.*;
import ru.ilnur.verificationmailservice.facade.model.enums.AccountType;
import ru.ilnur.verificationmailservice.facade.model.identities.AccountIdentity;
import ru.ilnur.verificationmailservice.facade.model.requests.UpdateAccountRequestFacade;
import ru.ilnur.verificationmailservice.facade.services.AccountServiceFacade;
import ru.ilnur.verificationmailservice.service.model.RequestCodeStatus;
import ru.ilnur.verificationmailservice.service.service.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.Instant;

@Component
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountServiceFacade, AccountService {

    private final AccountRepository accountRepository;
    private final PasswordService passwordService;
    private final EmailService emailService;
    private final RequestCodeService requestCodeService;
    private final ContractorEmailService contractorEmailService;
    private final RandomService randomService;

    @Override
    @Transactional
    public RequestCodeStatus createAccountAndRequestCode(String email, String plainPassword)
            throws EntityNotFoundException, EmailAlreadyUseException, IOException, UnableSendMessageException {
        Instant now = Instant.now();
        ContractorEmail contractorEmail = contractorEmailService.getContractorEmail(email);
        RequestCode requestCode = requestCodeService.create(email, now);
        Account account = accountRepository.findAccountByEmail(email);
        if (account != null && account.isVerified()) {
            throw new EmailAlreadyUseException(email);
        }
        RequestCodeStatus requestCodeStatus = requestCodeService.tryRequestNewCodeOrGetExisting(requestCode);
        if (!requestCodeStatus.isCodeSent()) {
            return requestCodeStatus;
        }
        String passwordHash = passwordService.hashPassword(plainPassword);
        if (account != null) {
            account.setPasswordHash(passwordHash);
            account.setVerified(false);
        }
        else {
            if (contractorEmail.getAccountType() == AccountType.ADMIN) {
                account = new AdminAccount(now, email, passwordHash);
            }
            else {
                account = new SellerAccount(now, email, passwordHash);
            }
        }
        try {
            emailService.sendVerifyMessage(email, requestCode.getCode());
        }
        catch (MessagingException e) {
            throw new UnableSendMessageException();
        }
        accountRepository.save(account);
        requestCodeService.save(requestCode);
        return requestCodeStatus;
    }

    @Transactional
    public void verifyEmail(String email, String code)
            throws WrongRequestCodeException, EntityNotFoundException, ExpiredRequestCodeException, RequestCodeAlreadyUsedException {
        Instant now = Instant.now();
        Account account = accountRepository.findAccountByEmail(email);
        if (account == null) {
            throw new EntityNotFoundException(email, Account.class);
        }
        requestCodeService.verify(email, code, now);
        account.setVerified(true);
    }

    @Override
    public Account getByCredentials(String email, String plainPassword) throws
            WrongCredentialsException, AccountNotVerifiedException {
        Account existAccount = accountRepository.findAccountByEmail(email);
        if (existAccount == null) {
            throw new WrongCredentialsException(email);
        }
        if (!passwordService.comparePassword(plainPassword, existAccount.getPasswordHash())) {
            throw new WrongCredentialsException(email, true);
        }
        if (!existAccount.isVerified()) {
            throw new AccountNotVerifiedException(email);
        }
        return existAccount;

    }

    @Override
    public Account getAccount(AccountIdentity accountIdentity) throws EntityNotFoundException {
        return accountRepository.findById(accountIdentity.getId())
                .orElseThrow(() -> new EntityNotFoundException(accountIdentity, Account.class));
    }

    @Override
    @Transactional
    public void resetPassword(String email) {
        Account existAccount = accountRepository.findAccountByEmail(email);
        if (existAccount == null) {
            return;
        }
        String plainPassword = randomService.generatePassword(6);
        String hashPassword = passwordService.hashPassword(plainPassword);
        existAccount.setPasswordHash(hashPassword);
        emailService.sendResetPasswordMessage(email, plainPassword);
    }

    @Override
    @Transactional
    public Account changePassword(AccountIdentity accountIdentity, String newPassword) throws
            EntityNotFoundException {
        Account existAccount = getAccount(accountIdentity);
        String newHashPassword = passwordService.hashPassword(newPassword);
        existAccount.setPasswordHash(newHashPassword);
        return existAccount;
    }

    @Override
    @Transactional
    public Account updateAccount(AccountIdentity accountIdentity, UpdateAccountRequestFacade changeUserInfoRequest)
            throws EntityNotFoundException {
        Account existAccount = getAccount(accountIdentity);
        existAccount.setFullName(changeUserInfoRequest.getFullName());
        return existAccount;
    }
}
