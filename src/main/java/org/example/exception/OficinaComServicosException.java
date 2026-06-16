package org.example.exception;

import org.springframework.http.HttpStatus;

/**
 * Lançada quando o usuário tenta excluir uma oficina que ainda possui
 * serviços cadastrados. A exclusão em cascata foi evitada de propósito
 * para que o usuário decida explicitamente o que fazer com os serviços.
 */
public class OficinaComServicosException extends ApiException {
    public OficinaComServicosException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
