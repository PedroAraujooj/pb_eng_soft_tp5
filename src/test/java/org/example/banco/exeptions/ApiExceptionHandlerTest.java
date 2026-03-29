package org.example.banco.exeptions;


import org.example.banco.config.ApiExceptionHandler;
import org.example.banco.exceptions.ContaExeption;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;

class ApiExceptionHandlerTest {

    private final ApiExceptionHandler handler = new ApiExceptionHandler();

    @Test
    void handleConta_deveRetornar404_quandoMensagemIndicarContaNaoExiste() {
        ResponseEntity<ProblemDetail> response =
                handler.handleConta(new ContaExeption("Conta não existe"));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Regra de negócio", response.getBody().getTitle());
        assertEquals("Conta não existe", response.getBody().getDetail());
    }

    @Test
    void handleConta_deveRetornar409_quandoForOutraRegraDeNegocio() {
        ResponseEntity<ProblemDetail> response =
                handler.handleConta(new ContaExeption("Saldo insuficiente"));

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Regra de negócio", response.getBody().getTitle());
        assertEquals("Saldo insuficiente", response.getBody().getDetail());
    }

    @Test
    void handleTimeout_deveRetornar504() {
        ResponseEntity<ProblemDetail> response =
                handler.handleTimeout(new TimeoutException("timeout"));

        assertEquals(HttpStatus.GATEWAY_TIMEOUT, response.getStatusCode());
        assertEquals("Timeout", response.getBody().getTitle());
        assertEquals("Operação excedeu o tempo limite.", response.getBody().getDetail());
    }

    @Test
    void handleDataAccess_deveRetornar503() {
        ResponseEntity<ProblemDetail> response =
                handler.handleDataAccess(new DataAccessResourceFailureException("db down"));

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertEquals("Indisponibilidade temporária", response.getBody().getTitle());
        assertEquals("Serviço indisponível no momento. Tente novamente.", response.getBody().getDetail());
    }

    @Test
    void handleGeneric_deveRetornar500() {
        ResponseEntity<ProblemDetail> response =
                handler.handleGeneric(new RuntimeException("boom"));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Erro inesperado", response.getBody().getTitle());
        assertEquals("Ocorreu um erro inesperado.", response.getBody().getDetail());
    }

    @Test
    void handleQueryTimeout_deveRetornar504() {
        ResponseEntity<ProblemDetail> response =
                handler.handleQueryTimeout(new QueryTimeoutException("timeout"));

        assertEquals(HttpStatus.GATEWAY_TIMEOUT, response.getStatusCode());
        assertEquals("Timeout", response.getBody().getTitle());
        assertEquals("Operação excedeu o tempo limite.", response.getBody().getDetail());
    }
}