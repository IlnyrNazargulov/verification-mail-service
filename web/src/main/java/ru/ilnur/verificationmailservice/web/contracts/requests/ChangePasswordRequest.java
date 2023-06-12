package ru.ilnur.verificationmailservice.web.contracts.requests;

import lombok.Getter;
import lombok.Setter;
import ru.ilnur.verificationmailservice.web.VerificationMailServiceApplication;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Setter
@Getter
public class ChangePasswordRequest {
    @Size(min = VerificationMailServiceApplication.MIN_PASSWORD_LENGTH)
    @NotNull
    private String plainPassword;
}
