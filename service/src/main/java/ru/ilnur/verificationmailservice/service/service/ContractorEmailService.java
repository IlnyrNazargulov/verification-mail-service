package ru.ilnur.verificationmailservice.service.service;

import ru.ilnur.verificationmailservice.dal.model.ContractorEmail;
import ru.ilnur.verificationmailservice.facade.exceptions.EntityNotFoundException;

public interface ContractorEmailService {
    ContractorEmail getContractorEmail(String email) throws EntityNotFoundException;
}
