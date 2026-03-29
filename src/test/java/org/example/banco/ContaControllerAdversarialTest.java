package org.example.banco;

import org.example.banco.config.ApiExceptionHandler;
import org.example.banco.controller.ContaController;
import org.example.banco.exceptions.ContaExeption;
import org.example.banco.service.ContaService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ContaController.class)
@Import(ApiExceptionHandler.class)
class ContaControllerAdversarialTest {

    @MockitoBean
    private ContaService service;
    @Autowired
    private MockMvc mvc;


    @Test
    void depositar_deveFalharEarly_quandoValorNegativo() throws Exception {
        mvc.perform(put("/v1/conta/depositar/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"valor\": -10}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.title").value("Entrada inválida"));
        Mockito.verifyNoInteractions(service);
    }

    @Test
    void depositar_deveFalharEarly_quandoBodyInvalido() throws Exception {
        mvc.perform(put("/v1/conta/depositar/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"valor\": \"abc\"}"))
                .andExpect(status().is5xxServerError());
        Mockito.verifyNoInteractions(service);
    }

    @Test
    void depositar_deveRetornar404_quandoContaNaoExiste() throws Exception {
        Mockito.doThrow(new ContaExeption("Conta não existe"))
                .when(service).depositar(eq(1L), any());

        mvc.perform(put("/v1/conta/depositar/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"valor\": 10}"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").value("Conta não existe"));
    }

    @Test
    void debitar_deveRetornar409_quandoRegraNegocioViolada() throws Exception {
        Mockito.doThrow(new ContaExeption("Saldo insuficiente"))
                .when(service).debitar(eq(1L), any());

        mvc.perform(put("/v1/conta/debitar/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"valor\": 999999}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.title").value("Regra de negócio"))
                .andExpect(jsonPath("$.detail").value("Saldo insuficiente"));
    }

    @Test
    void transferir_deveRetornar503_quandoFalhaDeInfraSimulada() throws Exception {
        Mockito.doThrow(new DataAccessResourceFailureException("DB down"))
                .when(service).transferir(eq(1L), eq(2L), any());

        mvc.perform(put("/v1/conta/1/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"valor\": 10}"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.title").value("Indisponibilidade temporária"))
                .andExpect(jsonPath("$.detail").value("Serviço indisponível no momento. Tente novamente."));
    }

    @Test
    void transferir_deveRetornar504_quandoTimeoutSimulado() throws Exception {
        Mockito.doThrow(new QueryTimeoutException("timeout", null))
                .when(service).transferir(eq(1L), eq(2L), any());

        mvc.perform(put("/v1/conta/1/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"valor\": 10}"))
                .andExpect(status().isGatewayTimeout())
                .andExpect(jsonPath("$.title").value("Timeout"));
    }
}