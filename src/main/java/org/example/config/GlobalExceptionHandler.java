package org.example.config;

import org.example.exception.ApiException;
import org.example.exception.UsernameAlreadyExistsException; // Importação corrigida
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // O método handleUsernameExists foi removido, pois UsernameAlreadyExistsException
    // agora estende ApiException e será tratado por handleApiException.
    // @ExceptionHandler(UsernameAlreadyExistsException.class)
    // public ResponseEntity<Map<String, String>> handleUsernameExists(UsernameAlreadyExistsException ex) {
    //     return ResponseEntity.status(HttpStatus.CONFLICT)
    //             .body(Map.of("error", "Nome de usuário já está em uso."));
    // }

    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class})
    public ResponseEntity<Map<String, String>> handleAuthErrors(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Credenciais inválidas."));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> fields = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        f -> f.getField(),
                        f -> f.getDefaultMessage(),
                        (a, b) -> a
                ));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("errors", fields));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, String>> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("error", "Acesso negado."));
    }

    // Trata todas as exceções de negócio que carregam seu próprio status HTTP
    // (ResourceNotFoundException -> 404, ForbiddenException -> 403,
    // UsernameAlreadyExistsException -> 409, etc.)
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Map<String, String>> handleApiException(ApiException ex) {
        return ResponseEntity.status(ex.getStatus())
                .body(Map.of("error", ex.getMessage()));
    }

    // Erro de integridade do banco (ex: violação de FK, constraint unique etc.)
    // Não expõe detalhes internos do banco para o cliente.
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        log.warn("Violação de integridade de dados: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", "Não foi possível concluir a operação devido a uma restrição de dados."));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntime(RuntimeException ex) {
        log.error("Erro interno: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Erro interno. Tente novamente mais tarde."));
    }
}
