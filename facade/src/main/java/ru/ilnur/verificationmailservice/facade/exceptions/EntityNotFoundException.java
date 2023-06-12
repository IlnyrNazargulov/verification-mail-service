package ru.ilnur.verificationmailservice.facade.exceptions;

import lombok.Getter;
import ru.ilnur.verificationmailservice.facade.model.identities.Identity;

import java.util.UUID;

@Getter
public class EntityNotFoundException extends BaseException {
    private final Class<?> entityClass;
    private final Object id;

    protected EntityNotFoundException(String message, Object id, Class<?> entityClass) {
        super(message);
        this.entityClass = entityClass;
        this.id = id;
    }

    public EntityNotFoundException(Identity identity, Class<?> entityClass) {
        this("there is not found entity " + entityClass + " with identity " + identity.getId(), identity.getId(), entityClass);
    }

    public EntityNotFoundException(UUID uuid, Class<?> entityClass) {
        this("there is not found entity " + entityClass + " with uuid " + uuid, uuid, entityClass);
    }

    public EntityNotFoundException(int id, Class<?> entityClass) {
        this("there is not found entity " + entityClass + " with id " + id, id, entityClass);
    }

    public EntityNotFoundException(String strId, Class<?> entityClass) {
        this("there is not found entity " + entityClass + " with id " + strId, strId, entityClass);
    }
}
