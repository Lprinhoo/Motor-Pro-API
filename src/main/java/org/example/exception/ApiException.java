package org.example.exception;

import org.springframework.http.HttpStatus;

/**
 * Classe base para exceções de negócio que devem ser traduzidas
 * diretamente em uma resposta HTTP com status e mensagem específicos.
 *
 * Usar esta classe (em vez de depender apenas de @ResponseStatus) garante
 * que o GlobalExceptionHandler trate essas exceções de forma previsível,
 * independente da ordem dos HandlerExceptionResolver do Spring.
 */
public abstract class ApiException extends RuntimeException {

    private final HttpStatus status;

    protected ApiException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    // Construtor adicionado para suportar a causa (Throwable)
    protected ApiException(HttpStatus status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
