package ru.ilnur.verificationmailservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ilnur.verificationmailservice.dal.model.ContractorEmail;
import ru.ilnur.verificationmailservice.dal.repositories.ContractorEmailRepository;
import ru.ilnur.verificationmailservice.facade.exceptions.ContractorEmailAlreadyAddException;
import ru.ilnur.verificationmailservice.facade.exceptions.EntityNotFoundException;
import ru.ilnur.verificationmailservice.facade.model.enums.AccountType;
import ru.ilnur.verificationmailservice.facade.services.ContractorEmailServiceFacade;
import ru.ilnur.verificationmailservice.service.service.ContractorEmailService;

@Component
@RequiredArgsConstructor
public class ContractorEmailServiceImpl implements ContractorEmailService, ContractorEmailServiceFacade {
    private final ContractorEmailRepository contractorEmailRepository;

    @Override
    public ContractorEmail getContractorEmail(String email) throws EntityNotFoundException {
        ContractorEmail contractorEmail = contractorEmailRepository.findByEmail(email);
        if (contractorEmail == null) {
            throw new EntityNotFoundException(email, ContractorEmail.class);
        }
        return contractorEmail;
    }

    @Override
    public ContractorEmail createContractorEmailAdmin(String email) throws ContractorEmailAlreadyAddException {
        ContractorEmail existCE = contractorEmailRepository.findByEmail(email);
        if (existCE != null) {
            throw new ContractorEmailAlreadyAddException(email);
        }
        ContractorEmail ce = new ContractorEmail(email, AccountType.ADMIN);
        return contractorEmailRepository.save(ce);
    }

    @Override
    public ContractorEmail createContractorEmailSeller(String email) throws ContractorEmailAlreadyAddException {
        ContractorEmail existCE = contractorEmailRepository.findByEmail(email);
        if (existCE != null) {
            throw new ContractorEmailAlreadyAddException(email);
        }
        ContractorEmail ce = new ContractorEmail(email, AccountType.SELLER);
        return contractorEmailRepository.save(ce);
    }
}
