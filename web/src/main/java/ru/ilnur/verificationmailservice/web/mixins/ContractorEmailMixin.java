package ru.ilnur.verificationmailservice.web.mixins;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ru.ilnur.verificationmailservice.facade.model.AccountFacade;
import ru.ilnur.verificationmailservice.facade.model.ContractorEmailFacade;

@JsonSerialize(as = ContractorEmailFacade.class)
public interface ContractorEmailMixin {
}
