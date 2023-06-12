package ru.ilnur.verificationmailservice.dal.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.ilnur.verificationmailservice.dal.model.RequestCode;

import java.time.Instant;
import java.util.List;

public interface RequestCodeRepository extends CrudRepository<RequestCode, Integer> {
    RequestCode findTopRequestCodeByEmailOrderByCreatedAtDesc(String login);

    @Query("from RequestCode where email = :email and createdAt >= :lowBoundary order by createdAt desc")
    List<RequestCode> findLastRequestsByFilter(String email, Instant lowBoundary);
}
