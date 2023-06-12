package ru.ilnur.verificationmailservice.web.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.ilnur.verificationmailservice.facade.exceptions.*;
import ru.ilnur.verificationmailservice.facade.model.AccountFacade;
import ru.ilnur.verificationmailservice.facade.model.ContractorEmailFacade;
import ru.ilnur.verificationmailservice.facade.model.RequestCodeStatusFacade;
import ru.ilnur.verificationmailservice.facade.model.enums.Role;
import ru.ilnur.verificationmailservice.facade.services.AccountServiceFacade;
import ru.ilnur.verificationmailservice.facade.services.ContractorEmailServiceFacade;
import ru.ilnur.verificationmailservice.facade.services.RefreshTokenServiceFacade;
import ru.ilnur.verificationmailservice.web.contracts.requests.*;
import ru.ilnur.verificationmailservice.web.contracts.responses.AccountWithTokenResponse;
import ru.ilnur.verificationmailservice.web.contracts.responses.ApiResponse;
import ru.ilnur.verificationmailservice.web.contracts.responses.RequestCodeResponse;
import ru.ilnur.verificationmailservice.web.exceptions.RefreshTokenExpiredException;
import ru.ilnur.verificationmailservice.web.model.TokenPrincipal;
import ru.ilnur.verificationmailservice.web.services.SecurityTokenService;

import java.io.IOException;

@RequestMapping(value = "/accounts/", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequiredArgsConstructor
public class AccountController {
    private final SecurityTokenService securityTokenService;
    private final AccountServiceFacade accountService;
    private final RefreshTokenServiceFacade refreshTokenService;
    private final ContractorEmailServiceFacade contractorEmailService;

    @Value("${ru.ilnur.verificationmailservice.web.unique-admin-user-id}")
    private int uniqueAdminValue;

    @PostMapping(value = "/request-code/")
    public ResponseEntity<ApiResponse<RequestCodeResponse>> registerRequestCode(
            @Validated @RequestBody TakeEmailRequest takeEmailRequest
    ) throws EmailAlreadyUseException, EntityNotFoundException, IOException, UnableSendMessageException {
        RequestCodeStatusFacade requestCodeStatus = accountService.createAccountAndRequestCode(takeEmailRequest.getEmail(), takeEmailRequest.getPlainPassword());
        RequestCodeResponse requestCodeResponse = new RequestCodeResponse(requestCodeStatus.getNextAttemptAfter());
        if (requestCodeStatus.isCodeSent()) {
            return ApiResponse.success(requestCodeResponse);
        }
        else {
            return ApiResponse.failure(requestCodeResponse);
        }
    }

    @GetMapping(value = "/verify-email/")
    public ResponseEntity verifyEmail(
            @RequestParam String email,
            @RequestParam String code
    ) throws WrongRequestCodeException, EntityNotFoundException,
            ExpiredRequestCodeException, RequestCodeAlreadyUsedException {
        accountService.verifyEmail(email, code);
        return ApiResponse.success();
    }

    @PostMapping(value = "/login/")
    public ResponseEntity<ApiResponse<AccountWithTokenResponse>> login(
            @Validated @RequestBody LoginRequest loginRequest
    )
            throws WrongCredentialsException, AccountNotVerifiedException {
        AccountFacade account = accountService.getByCredentials(loginRequest.getEmail(), loginRequest.getPlainPassword());
        OAuth2AccessToken accessTokenByAccount = securityTokenService.createAccessTokenByAccount(account);
        return ApiResponse.success(new AccountWithTokenResponse(accessTokenByAccount, account));
    }

    @Secured({Role.SELLER, Role.ADMIN})
    @GetMapping("/current/")
    public ResponseEntity<?> getCurrent(@AuthenticationPrincipal TokenPrincipal tokenPrincipal) throws EntityNotFoundException {
        AccountFacade account = accountService.getAccount(tokenPrincipal.getAccountIdentity());
        return ApiResponse.success(account);
    }

    @Secured({Role.SELLER, Role.ADMIN})
    @PutMapping(value = "/current/")
    public ResponseEntity<ApiResponse<AccountFacade>> updateAccount(
            @AuthenticationPrincipal TokenPrincipal tokenPrincipal,
            @Validated @RequestBody UpdateAccountRequest updateAccountRequest
    )
            throws EntityNotFoundException {
        AccountFacade account = accountService.updateAccount(tokenPrincipal.getAccountIdentity(), updateAccountRequest);
        return ApiResponse.success(account);
    }

    @PostMapping("/current/refresh/")
    public ResponseEntity<ApiResponse<OAuth2AccessToken>> refreshToken(
            @Validated @RequestBody RefreshTokenRequest refreshTokenRequest
    )
            throws RefreshTokenExpiredException {
        OAuth2AccessToken accessToken = securityTokenService.refreshAccessToken(refreshTokenRequest.getToken());
        return ApiResponse.success(accessToken);
    }

    @Secured({Role.SELLER, Role.ADMIN})
    @PostMapping("/current/logout/")
    public ResponseEntity<?> logout(
            @AuthenticationPrincipal TokenPrincipal tokenPrincipal,
            @RequestBody RefreshTokenRequest refreshTokenRequest,
            @RequestParam boolean allDevices
    ) throws EntityNotFoundException {
        if (!allDevices) {
            refreshTokenService.releaseOne(tokenPrincipal.getAccountIdentity(), refreshTokenRequest.getToken());
            return ApiResponse.success();
        }
        refreshTokenService.releaseAll(tokenPrincipal.getAccountIdentity(), tokenPrincipal.getAccountIdentity());
        return ApiResponse.success();
    }

    @PostMapping(value = "/reset-password/")
    public ResponseEntity resetPassword(
            @RequestBody ResetPasswordRequest resetPasswordRequest
    ) {
        accountService.resetPassword(resetPasswordRequest.getEmail());
        return ApiResponse.success();
    }

    @Secured({Role.SELLER, Role.ADMIN})
    @PostMapping(value = "/current/change-password/")
    public ResponseEntity<ApiResponse<AccountFacade>> changePassword(
            @AuthenticationPrincipal TokenPrincipal tokenPrincipal,
            @Validated @RequestBody ChangePasswordRequest changePasswordRequest
    )
            throws EntityNotFoundException {
        AccountFacade account = accountService
                .changePassword(tokenPrincipal.getAccountIdentity(), changePasswordRequest.getPlainPassword());
        return ApiResponse.success(account);
    }

    @Secured(Role.ADMIN)
    @PostMapping(value = "/admins/")
    public ResponseEntity<ApiResponse<ContractorEmailFacade>> addAdmin(
            @AuthenticationPrincipal TokenPrincipal tokenPrincipal,
            @Validated @RequestBody AddUserRequest addUserRequest
    ) throws ContractorEmailAlreadyAddException {
        if (tokenPrincipal.getAccountIdentity().getId() != uniqueAdminValue) {
            throw new AccessDeniedException("Access only for a unique user.");
        }
        ContractorEmailFacade contractorEmailAdmin = contractorEmailService
                .createContractorEmailAdmin(addUserRequest.getEmail());
        return ApiResponse.success(contractorEmailAdmin);
    }

    @Secured(Role.ADMIN)
    @PostMapping(value = "/sellers/")
    public ResponseEntity<ApiResponse<ContractorEmailFacade>> addSeller(
            @AuthenticationPrincipal TokenPrincipal tokenPrincipal,
            @Validated @RequestBody AddUserRequest addUserRequest
    ) throws ContractorEmailAlreadyAddException {
        //access to only one account
        if (tokenPrincipal.getAccountIdentity().getId() != uniqueAdminValue) {
            throw new AccessDeniedException("Access only for a unique user.");
        }
        ContractorEmailFacade contractorEmailSeller = contractorEmailService
                .createContractorEmailSeller(addUserRequest.getEmail());
        return ApiResponse.success(contractorEmailSeller);
    }
}
