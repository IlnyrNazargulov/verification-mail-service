package ru.ilnur.verificationmailservice.web.contracts.requests;

import lombok.Getter;
import lombok.Setter;
import ru.ilnur.verificationmailservice.web.VerificationMailServiceApplication;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
@Getter
public class LoginRequest {

    @NotNull
    @Email
    private String email;
    @NotNull
    @Size(min = VerificationMailServiceApplication.MIN_PASSWORD_LENGTH)
    private String plainPassword;
}
