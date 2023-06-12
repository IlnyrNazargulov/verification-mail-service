package ru.ilnur.verificationmailservice.facade.services;


import ru.ilnur.verificationmailservice.facade.exceptions.EntityNotFoundException;
import ru.ilnur.verificationmailservice.facade.exceptions.RefreshTokenLimitExceededException;
import ru.ilnur.verificationmailservice.facade.model.AccountFacade;
import ru.ilnur.verificationmailservice.facade.model.identities.AccountIdentity;

import java.time.Instant;
import java.util.UUID;

public interface RefreshTokenServiceFacade {
    /**
     * Claim new refresh token for the specified identity.
     *
     * @param accountIdentity account identity.
     * @param now             current time
     * @param expiredAt       expiration date.
     * @return New unique refresh token.
     * @throws EntityNotFoundException            if there is no account with the specified identity.
     * @throws RefreshTokenLimitExceededException if too many refresh token for the specified account.
     */
    UUID claimToken(AccountIdentity accountIdentity, Instant now, Instant expiredAt) throws EntityNotFoundException, RefreshTokenLimitExceededException;

    /**
     * Release refresh token if it not released yet and not expired.
     *
     * @param token token to release.
     * @param now   current time.
     * @return Account related with the specified token or null if token is expired.
     */
    AccountFacade releaseToken(UUID token, Instant now);

    /**
     * Release one refresh token for the specified identity.
     *
     * @param accountIdentity Account identity.
     * @param token           Refresh token to release.
     * @throws EntityNotFoundException If there is no such token for the specified account.
     */
    void releaseOne(AccountIdentity accountIdentity, UUID token) throws EntityNotFoundException;

    /**
     * Release all refresh token for the specified identity.
     *
     * @param targetIdentity target Account identity.
     * @param currentIdentity current Account identity.
     * @throws EntityNotFoundException if there is no account with the specified identity.
     */
    void releaseAll(AccountIdentity targetIdentity, AccountIdentity currentIdentity) throws EntityNotFoundException;
}
