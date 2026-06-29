package org.example.exception;

import org.springframework.http.HttpStatus;

public class GoogleAuthException extends ApiException {
    public GoogleAuthException(String message) {
        super(HttpStatus.UNAUTHORIZED, message); // Erros de autenticação Google geralmente resultam em UNAUTHORIZED
    }

    public GoogleAuthException(String message, Throwable cause) {
        super(HttpStatus.UNAUTHORIZED, message, cause);
    }
}
