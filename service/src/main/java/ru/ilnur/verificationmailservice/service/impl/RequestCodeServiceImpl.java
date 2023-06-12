package ru.ilnur.verificationmailservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.ilnur.verificationmailservice.dal.model.RequestCode;
import ru.ilnur.verificationmailservice.dal.repositories.RequestCodeRepository;
import ru.ilnur.verificationmailservice.facade.exceptions.EntityNotFoundException;
import ru.ilnur.verificationmailservice.facade.exceptions.ExpiredRequestCodeException;
import ru.ilnur.verificationmailservice.facade.exceptions.RequestCodeAlreadyUsedException;
import ru.ilnur.verificationmailservice.facade.exceptions.WrongRequestCodeException;
import ru.ilnur.verificationmailservice.service.config.RequestCodeProperties;
import ru.ilnur.verificationmailservice.service.model.RequestCodeStatus;
import ru.ilnur.verificationmailservice.service.service.RandomService;
import ru.ilnur.verificationmailservice.service.service.RequestCodeService;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RequestCodeServiceImpl implements RequestCodeService {
    private final RequestCodeRepository requestCodeRepository;
    private final RandomService randomService;
    private final RequestCodeProperties requestCodeProperties;

    @Override
    @Transactional
    public RequestCodeStatus tryRequestNewCodeOrGetExisting(RequestCode requestCode) {
        Instant now = requestCode.getCreatedAt();
        Instant maxLowBoundary = now.minus(requestCodeProperties.getRequestCodeInterval(), ChronoUnit.MILLIS);

        RequestCode tooOfterRequest = null;
        List<RequestCode> lastRequests = requestCodeRepository.findLastRequestsByFilter(requestCode.getEmail(), maxLowBoundary);
        if (lastRequests.size() >= requestCodeProperties.getMaxCodeRequestsPerInterval()) {
            Instant lastCreatedAt = lastRequests.stream().findFirst().get().getCreatedAt();
            Instant nextAttemptAt = lastCreatedAt.plus(requestCodeProperties.getRequestCodeInterval(), ChronoUnit.MILLIS);
            return new RequestCodeStatus(
                    (int) (Math.max(Duration.between(now, nextAttemptAt).toMillis() / 1000, 0)),
                    false
            );
        }
        for (RequestCode lastRequest : lastRequests) {
            long interval = Duration.between(lastRequest.getCreatedAt(), now).toMillis();

            // проверяем не слишком ли часто
            if (interval < requestCodeProperties.getMinInterval()) {
                tooOfterRequest = lastRequest;
                break;
            }
        }

        // по умолчанию следующий можно сделать от текущего + минимальный интервал
        Instant nextAttemptAt = now.plus(requestCodeProperties.getMinInterval(), ChronoUnit.MILLIS);

        if ((lastRequests.size() + 1) >= requestCodeProperties.getMaxCodeRequestsPerInterval()) {
            nextAttemptAt = now.plus(requestCodeProperties.getRequestCodeInterval(), ChronoUnit.MILLIS);
        }

        if (tooOfterRequest != null) {
            // если слишком частые запросы, то следующий можно сделать от последнего + минимальный интервал
            nextAttemptAt = tooOfterRequest.getCreatedAt().plus(requestCodeProperties.getMinInterval(), ChronoUnit.MILLIS);
        }
        return new RequestCodeStatus(
                (int) (Math.max(Duration.between(now, nextAttemptAt).toMillis() / 1000, 0)),
                tooOfterRequest == null
        );
    }

    @Override
    public RequestCode create(String email, Instant now) {
        String generateCode = randomService.generateCode(15);
        RequestCode requestCode = new RequestCode(email, generateCode, now);
        return requestCode;
    }

    @Override
    public RequestCode save(RequestCode requestCode) {
        return requestCodeRepository.save(requestCode);
    }

    @Override
    public void verify(String email, String code, Instant now) throws WrongRequestCodeException, EntityNotFoundException, ExpiredRequestCodeException, RequestCodeAlreadyUsedException {
        RequestCode requestCode = requestCodeRepository.findTopRequestCodeByEmailOrderByCreatedAtDesc(email);
        if (requestCode == null) {
            throw new EntityNotFoundException(email, RequestCode.class);
        }
        if (requestCode.isUsed()) {
            throw new RequestCodeAlreadyUsedException();
        }
        if (requestCode.getCreatedAt().plusSeconds(requestCodeProperties.getExpiresIn()).isBefore(now)) {
            throw new ExpiredRequestCodeException();
        }
        if (requestCode.getCode().equals(code)) {
            requestCode.setUsed(true);
            return;
        }
        else {
            throw new WrongRequestCodeException(email, code);
        }
    }
}
