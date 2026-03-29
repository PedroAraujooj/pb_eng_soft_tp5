package org.example.banco;


import net.jqwik.api.*;
import net.jqwik.spring.JqwikSpringSupport;
import org.example.banco.config.ApiExceptionHandler;
import org.example.banco.controller.ContaController;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@JqwikSpringSupport
@WebMvcTest(controllers = ContaController.class)
@Import(ApiExceptionHandler.class)
class TransferenciaValorFuzzTest {

    @MockitoBean
    private org.example.banco.service.ContaService service;

    @Autowired
    private MockMvc mvc;


    @Provide
    Arbitrary<BigDecimal> weirdBigDecimals() {
        var normal = Arbitraries.bigDecimals()
                .between(new BigDecimal("-1000000000"), new BigDecimal("1000000000"));

        var huge = Arbitraries.integers().between(1, 2000)
                .map(n -> BigDecimal.TEN.pow(n));

        var fractional = Arbitraries.integers().between(1, 200)
                .map(scale -> new BigDecimal("1").movePointLeft(scale));
        return Arbitraries.oneOf(normal, huge, fractional);
    }

    @Property(tries = 200)
    void depositar_deveResponderComSeguranca_paraValoresExtremos(@ForAll("weirdBigDecimals") BigDecimal v) throws Exception {
        Mockito.doNothing().when(service).depositar(anyLong(), any());

        String json = "{\"valor\": " + v.toPlainString() + "}";

        var res = mvc.perform(put("/v1/conta/depositar/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn();

        int status = res.getResponse().getStatus();
        String body = res.getResponse().getContentAsString();

        org.junit.jupiter.api.Assertions.assertFalse(body.contains("StackTrace"));
        org.junit.jupiter.api.Assertions.assertFalse(body.contains("Exception"));

        if (v.compareTo(BigDecimal.ZERO) <= 0) {
            org.junit.jupiter.api.Assertions.assertEquals(400, status);
        }
    }
}