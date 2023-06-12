package ru.ilnur.verificationmailservice.dal.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.ilnur.verificationmailservice.dal.model.accounts.Account;
import ru.ilnur.verificationmailservice.dal.model.RefreshToken;

import java.time.Instant;
import java.util.UUID;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, UUID> {
    @Modifying
    @Transactional
    @Query("delete from RefreshToken r where r = :refreshToken")
    int remove(final RefreshToken refreshToken);

    @Transactional
    void removeAllByAccount(final Account account);

    @Query("select count (r) from RefreshToken r where r.account = :account and r.createdAt > :lastHour")
    int findCountForTime(Account account, Instant lastHour);

    RefreshToken findByAccountAndToken(final Account account, final UUID token);
}
