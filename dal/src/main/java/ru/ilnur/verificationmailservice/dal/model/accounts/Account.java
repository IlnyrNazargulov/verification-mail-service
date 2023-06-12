package ru.ilnur.verificationmailservice.dal.model.accounts;

import lombok.*;
import ru.ilnur.verificationmailservice.facade.model.AccountFacade;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Entity
@Table(name = "account")
@EqualsAndHashCode(of = {"id"})
@DiscriminatorColumn(name = "type")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Account implements AccountFacade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(updatable = false, nullable = false)
    private Instant createdAt;
    @Setter
    @Column(nullable = false)
    private String email;
    @Setter
    @Column(nullable = false)
    private String passwordHash;
    @Setter
    @Column(nullable = false)
    private String fullName;
    @Setter
    private boolean isVerified;

    public Account(Instant createdAt, String email, String passwordHash) {
        this.createdAt = createdAt;
        this.email = email;
        this.passwordHash = passwordHash;
    }
}