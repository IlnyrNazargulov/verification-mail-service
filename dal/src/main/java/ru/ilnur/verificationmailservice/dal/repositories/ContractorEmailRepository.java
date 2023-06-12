package ru.ilnur.verificationmailservice.dal.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.ilnur.verificationmailservice.dal.model.ContractorEmail;

public interface ContractorEmailRepository extends CrudRepository<ContractorEmail, Integer> {
    @Query("select ce from ContractorEmail ce " +
            "where ce.email = :email")
    ContractorEmail findByEmail(String email);
}
