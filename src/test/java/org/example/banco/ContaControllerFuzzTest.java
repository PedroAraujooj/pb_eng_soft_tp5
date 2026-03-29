package org.example.banco;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.Size;
import net.jqwik.spring.JqwikSpringSupport;
import org.assertj.core.api.Assertions;
import org.example.banco.config.ApiExceptionHandler;
import org.example.banco.controller.ContaController;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@JqwikSpringSupport
@WebMvcTest(controllers = ContaController.class)
@Import(ApiExceptionHandler.class)
class ContaControllerFuzzTest {

    @MockitoBean
    private org.example.banco.service.ContaService service;

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void setup() throws Exception {
        Mockito.doNothing().when(service).depositar(anyLong(), any());
    }

    @Property(tries = 200)
    void depositar_naoDeveVazarStackTrace_comPayloadArbitrario(
            @ForAll @Size(max = 500) String rawPayload
    ) throws Exception {

        var result = mvc.perform(put("/v1/conta/depositar/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(rawPayload))
                .andReturn();

        int status = result.getResponse().getStatus();
        String body = result.getResponse().getContentAsString();

        Assertions.assertThat(body).doesNotContain("Exception");
        Assertions.assertThat(body).doesNotContain("StackTrace");

        if (status == 500) {
            Assertions.assertThat(body).contains("Ocorreu um erro inesperado");
        }
    }
}