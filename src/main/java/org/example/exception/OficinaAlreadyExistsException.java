package org.example.exception;

import org.springframework.http.HttpStatus;

public class OficinaAlreadyExistsException extends ApiException {
    public OficinaAlreadyExistsException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
