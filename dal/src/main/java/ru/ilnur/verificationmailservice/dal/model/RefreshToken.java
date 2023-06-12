package ru.ilnur.verificationmailservice.dal.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.ilnur.verificationmailservice.dal.model.accounts.Account;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Getter
@Entity
@Table(name = "refresh_token")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {
    @Id
    private UUID token;
    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false, updatable = false)
    private Account account;
    @Column(updatable = false, nullable = false)
    private Instant createdAt;
    @Column(updatable = false, nullable = false)
    private Instant expiredAt;

    public RefreshToken(UUID token, Account account, Instant createdAt, Instant expiredAt) {
        this.token = token;
        this.account = account;
        this.createdAt = createdAt;
        this.expiredAt = expiredAt;
    }
}
