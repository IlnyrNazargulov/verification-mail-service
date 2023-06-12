package ru.ilnur.verificationmailservice.web.contracts.requests;

import lombok.Getter;
import lombok.Setter;
import ru.ilnur.verificationmailservice.facade.model.requests.UpdateAccountRequestFacade;

import javax.validation.constraints.NotBlank;


@Setter
@Getter
public class UpdateAccountRequest implements UpdateAccountRequestFacade {
    @NotBlank
    private String fullName;
}
