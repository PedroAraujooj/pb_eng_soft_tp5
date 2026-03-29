package org.example.banco.controller;


import org.example.banco.config.ApiExceptionHandler;
import org.example.banco.entity.Conta;
import org.example.banco.service.ContaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ContaController.class)
@Import(ApiExceptionHandler.class)
class ContaControllerTest {

    @MockitoBean
    private ContaService service;

    @Autowired
    private MockMvc mvc;

    @Test
    void listarContas_deveRetornar200ELista() throws Exception {
        when(service.consultarContasDb()).thenReturn(List.of(
                new Conta(1L, "Pedro", new BigDecimal("10.00")),
                new Conta(2L, "Maria", new BigDecimal("20.00"))
        ));

        mvc.perform(get("/v1/conta"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].titular").value("Pedro"))
                .andExpect(jsonPath("$[0].saldo").value(10.00))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].titular").value("Maria"))
                .andExpect(jsonPath("$[1].saldo").value(20.00));
    }

    @Test
    void buscarContaPorId_deveRetornar200EConta() throws Exception {
        when(service.consultarContaDb(1L))
                .thenReturn(new Conta(1L, "Pedro", new BigDecimal("99.99")));

        mvc.perform(get("/v1/conta/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titular").value("Pedro"))
                .andExpect(jsonPath("$.saldo").value(99.99));
    }

    @Test
    void criarConta_deveRetornar200() throws Exception {
        mvc.perform(post("/v1/conta")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "titular": "Pedro",
                                  "saldo": 100.00
                                }
                                """))
                .andExpect(status().isOk());

        verify(service).incluirContaDb("Pedro", new BigDecimal("100.00"));
    }

    @Test
    void deletarConta_deveRetornar200() throws Exception {
        mvc.perform(delete("/v1/conta/10"))
                .andExpect(status().isOk());

        verify(service).excluirContaDb(10L);
    }

    @Test
    void depositar_deveRetornar200() throws Exception {
        mvc.perform(put("/v1/conta/depositar/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "valor": 15.50
                                }
                                """))
                .andExpect(status().isOk());

        verify(service).depositar(1L, new BigDecimal("15.50"));
    }

    @Test
    void debitar_deveRetornar200() throws Exception {
        mvc.perform(put("/v1/conta/debitar/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "valor": 7.25
                                }
                                """))
                .andExpect(status().isOk());

        verify(service).debitar(1L, new BigDecimal("7.25"));
    }

    @Test
    void transferir_deveRetornar200() throws Exception {
        mvc.perform(put("/v1/conta/1/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "valor": 30.00
                                }
                                """))
                .andExpect(status().isOk());

        verify(service).transferir(1L, 2L, new BigDecimal("30.00"));
    }
}