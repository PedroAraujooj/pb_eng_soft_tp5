package org.example.banco.config;


import org.example.banco.exceptions.ContaExeption;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.concurrent.TimeoutException;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidation(MethodArgumentNotValidException ex) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Entrada inválida");
        pd.setType(URI.create("https://example.org/problems/validation"));
        pd.setDetail(ex.getBindingResult().getAllErrors().stream()
                .findFirst()
                .map(e -> e.getDefaultMessage())
                .orElse("Payload inválido"));
        return ResponseEntity.badRequest().body(pd);
    }

    @ExceptionHandler(ContaExeption.class)
    public ResponseEntity<ProblemDetail> handleConta(ContaExeption ex) {
        HttpStatus status = ex.getMessage() != null && ex.getMessage().toLowerCase().contains("não existe")
                ? HttpStatus.NOT_FOUND
                : HttpStatus.CONFLICT;

        ProblemDetail pd = ProblemDetail.forStatus(status);
        pd.setTitle("Regra de negócio");
        pd.setType(URI.create("https://example.org/problems/business"));
        pd.setDetail(ex.getMessage());
        return ResponseEntity.status(status).body(pd);
    }

    @ExceptionHandler(TimeoutException.class)
    public ResponseEntity<ProblemDetail> handleTimeout(TimeoutException ex) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.GATEWAY_TIMEOUT);
        pd.setTitle("Timeout");
        pd.setType(URI.create("https://example.org/problems/timeout"));
        pd.setDetail("Operação excedeu o tempo limite.");
        return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(pd);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ProblemDetail> handleDataAccess(DataAccessException ex) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.SERVICE_UNAVAILABLE);
        pd.setTitle("Indisponibilidade temporária");
        pd.setType(URI.create("https://example.org/problems/unavailable"));
        pd.setDetail("Serviço indisponível no momento. Tente novamente.");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(pd);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGeneric(Exception ex) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        pd.setTitle("Erro inesperado");
        pd.setType(URI.create("https://example.org/problems/internal"));
        pd.setDetail("Ocorreu um erro inesperado.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(pd);
    }

    @ExceptionHandler(org.springframework.dao.QueryTimeoutException.class)
    public ResponseEntity<ProblemDetail> handleQueryTimeout(QueryTimeoutException ex) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.GATEWAY_TIMEOUT);
        pd.setTitle("Timeout");
        pd.setDetail("Operação excedeu o tempo limite.");
        return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(pd);
    }
}