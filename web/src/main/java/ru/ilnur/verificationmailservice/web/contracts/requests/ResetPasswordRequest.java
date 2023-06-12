package ru.ilnur.verificationmailservice.web.contracts.requests;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;


@Setter
@Getter
public class ResetPasswordRequest {
    @Email
    private String email;
}
