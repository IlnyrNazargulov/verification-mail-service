package ru.ilnur.verificationmailservice.web.mixins;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ru.ilnur.verificationmailservice.facade.model.AccountFacade;

@JsonSerialize(as = AccountFacade.class)
public interface AccountMixin {
}
