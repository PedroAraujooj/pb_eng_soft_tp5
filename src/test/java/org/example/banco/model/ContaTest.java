package org.example.banco.model;

import org.example.banco.entity.Conta;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ContaTest {

    @Test
    void construtor_deveArredondarSaldoInicialParaDuasCasas() {
        Conta conta = new Conta(1L, "Pedro", new BigDecimal("10.126"));

        assertEquals(new BigDecimal("10.13"), conta.getSaldo());
        assertEquals("Pedro", conta.getNome());
        assertEquals(1L, conta.getId());
    }

    @Test
    void depositar_deveSomarEArredondar() {
        Conta conta = new Conta(1L, "Pedro", new BigDecimal("10.00"));

        conta.depositar(new BigDecimal("2.345"));

        assertEquals(new BigDecimal("12.35"), conta.getSaldo());
    }

    @Test
    void depositar_deveLancarExcecao_quandoValorForNulo() {
        Conta conta = new Conta(1L, "Pedro", new BigDecimal("10.00"));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> conta.depositar(null)
        );

        assertEquals("Valor inválido", ex.getMessage());
    }

    @Test
    void depositar_deveLancarExcecao_quandoValorForZeroOuNegativo() {
        Conta conta = new Conta(1L, "Pedro", new BigDecimal("10.00"));

        IllegalArgumentException ex1 = assertThrows(
                IllegalArgumentException.class,
                () -> conta.depositar(BigDecimal.ZERO)
        );
        IllegalArgumentException ex2 = assertThrows(
                IllegalArgumentException.class,
                () -> conta.depositar(new BigDecimal("-1.00"))
        );

        assertEquals("Valor inválido", ex1.getMessage());
        assertEquals("Valor inválido", ex2.getMessage());
    }

    @Test
    void debitar_deveSubtrairEArredondar() {
        Conta conta = new Conta(1L, "Pedro", new BigDecimal("20.00"));

        conta.debitar(new BigDecimal("5.555"));

        assertEquals(new BigDecimal("14.45"), conta.getSaldo());
    }

    @Test
    void debitar_deveLancarExcecao_quandoSaldoInsuficiente() {
        Conta conta = new Conta(1L, "Pedro", new BigDecimal("10.00"));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> conta.debitar(new BigDecimal("11.00"))
        );

        assertEquals("Saldo insuficiente", ex.getMessage());
    }

    @Test
    void debitar_deveLancarExcecao_quandoValorForInvalido() {
        Conta conta = new Conta(1L, "Pedro", new BigDecimal("10.00"));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> conta.debitar(BigDecimal.ZERO)
        );

        assertEquals("Valor inválido", ex.getMessage());
    }
}