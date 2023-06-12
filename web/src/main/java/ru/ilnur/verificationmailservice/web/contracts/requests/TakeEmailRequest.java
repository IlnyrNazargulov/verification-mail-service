package ru.ilnur.verificationmailservice.web.contracts.requests;

import lombok.Getter;
import lombok.Setter;
import ru.ilnur.verificationmailservice.web.VerificationMailServiceApplication;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Getter
@Setter
public class TakeEmailRequest {
    @Email
    private String email;
    @Size(min = VerificationMailServiceApplication.MIN_PASSWORD_LENGTH)
    private String plainPassword;
}

