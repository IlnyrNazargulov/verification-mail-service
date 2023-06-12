package ru.ilnur.verificationmailservice.dal.model.accounts;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.ilnur.verificationmailservice.facade.model.enums.AccountType;
import ru.ilnur.verificationmailservice.facade.model.enums.Role;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.Instant;

@Getter
@Entity
@DiscriminatorValue(AccountType.ADMIN_VALUE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminAccount extends Account {
    @Override
    public Role getRole() {
        return Role.ROLE_ADMIN;
    }

    public AdminAccount(Instant createdAt, String email, String passwordHash) {
        super(createdAt, email, passwordHash);
    }
}
