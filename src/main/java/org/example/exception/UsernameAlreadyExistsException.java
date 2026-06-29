package org.example.exception;

import org.springframework.http.HttpStatus;

public class UsernameAlreadyExistsException extends ApiException {
    public UsernameAlreadyExistsException(String message) {
        super(HttpStatus.CONFLICT, message); // Usando HttpStatus.CONFLICT para indicar que o recurso já existe
    }
}
