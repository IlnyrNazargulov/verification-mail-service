package ru.ilnur.verificationmailservice.dal.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.ilnur.verificationmailservice.dal.model.accounts.Account;

public interface AccountRepository extends CrudRepository<Account, Integer> {
    Account findAccountByEmail(String email);
}
