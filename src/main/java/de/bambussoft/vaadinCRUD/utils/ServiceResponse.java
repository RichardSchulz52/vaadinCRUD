package de.bambussoft.vaadinCRUD.utils;


import de.bambussoft.vaadinCRUD.backend.superclasses.AbstractEntity;
import lombok.*;

import javax.validation.ConstraintViolation;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public @Data
class ServiceResponse {

    @NonNull
    private Action action;
    private String message;
    private AbstractEntity entity;

    public static ServiceResponse error(Set<ConstraintViolation<Object>> violations) {
        String errorMessage = violations.stream()
                .map(cv -> cv.getPropertyPath() + " " + cv.getMessage())
                .collect(Collectors.joining("\n"));
        return new ServiceResponse(Action.ERROR, errorMessage, null);
    }

    public static ServiceResponse saved(AbstractEntity entity) {
        return new ServiceResponse(Action.SAVED, null, entity);
    }

    public static ServiceResponse saved(AbstractEntity entity, String message) {
        return new ServiceResponse(Action.SAVED, message, entity);
    }

    public static ServiceResponse deleted(AbstractEntity entity) {
        return new ServiceResponse(Action.DELETED, null, entity);
    }

    public enum Action {
        ERROR, SAVED, DELETED, NONE
    }
}
