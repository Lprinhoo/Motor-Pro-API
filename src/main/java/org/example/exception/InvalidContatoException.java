package org.example.exception;

import org.springframework.http.HttpStatus;

public class InvalidContatoException extends ApiException {
    public InvalidContatoException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
