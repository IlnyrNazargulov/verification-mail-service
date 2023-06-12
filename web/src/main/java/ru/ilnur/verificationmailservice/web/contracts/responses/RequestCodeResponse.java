package ru.ilnur.verificationmailservice.web.contracts.responses;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RequestCodeResponse {
    private final int timeout;
}
