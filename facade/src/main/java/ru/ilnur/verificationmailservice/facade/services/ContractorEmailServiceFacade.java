package ru.ilnur.verificationmailservice.facade.services;

import ru.ilnur.verificationmailservice.facade.exceptions.ContractorEmailAlreadyAddException;
import ru.ilnur.verificationmailservice.facade.model.ContractorEmailFacade;

public interface ContractorEmailServiceFacade {
    ContractorEmailFacade createContractorEmailAdmin(String email) throws ContractorEmailAlreadyAddException;

    ContractorEmailFacade createContractorEmailSeller(String email) throws ContractorEmailAlreadyAddException;
}
