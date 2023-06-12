package ru.ilnur.verificationmailservice.service.service;

import ru.ilnur.verificationmailservice.dal.model.RequestCode;
import ru.ilnur.verificationmailservice.facade.exceptions.EntityNotFoundException;
import ru.ilnur.verificationmailservice.facade.exceptions.ExpiredRequestCodeException;
import ru.ilnur.verificationmailservice.facade.exceptions.RequestCodeAlreadyUsedException;
import ru.ilnur.verificationmailservice.facade.exceptions.WrongRequestCodeException;
import ru.ilnur.verificationmailservice.service.model.RequestCodeStatus;

import java.time.Instant;

public interface RequestCodeService {
    RequestCode create(String email, Instant now);

    RequestCode save(RequestCode requestCode);

    RequestCodeStatus tryRequestNewCodeOrGetExisting(RequestCode requestCode);

    void verify(String email, String code, Instant now) throws WrongRequestCodeException, EntityNotFoundException, ExpiredRequestCodeException, RequestCodeAlreadyUsedException;
}
