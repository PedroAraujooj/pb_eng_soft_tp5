package org.example.banco.service;

import org.example.banco.entity.Conta;
import org.example.banco.exceptions.ContaExeption;
import org.example.banco.repository.ContaRepository;
import org.example.banco.service.impl.ContaServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContaServiceImplTest {

    @Mock
    private ContaRepository contaRepository;

    @InjectMocks
    private ContaServiceImpl service;

    @Test
    void excluirContaDb_deveDelegarParaRepository() {
        service.excluirContaDb(1L);

        verify(contaRepository).deleteById(1L);
    }

    @Test
    void consultarContasDb_deveRetornarListaDoRepository() {
        List<Conta> contas = List.of(
                new Conta(1L, "A", new BigDecimal("10.00")),
                new Conta(2L, "B", new BigDecimal("20.00"))
        );
        when(contaRepository.findAll()).thenReturn(contas);

        List<Conta> resultado = service.consultarContasDb();

        assertEquals(2, resultado.size());
        assertSame(contas, resultado);
        verify(contaRepository).findAll();
    }

    @Test
    void consultarContaDb_deveRetornarConta_quandoExistir() throws Exception {
        Conta conta = new Conta(1L, "Pedro", new BigDecimal("10.00"));
        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));

        Conta resultado = service.consultarContaDb(1L);

        assertSame(conta, resultado);
        verify(contaRepository).findById(1L);
    }

    @Test
    void consultarContaDb_deveLancarExcecao_quandoNaoExistir() {
        when(contaRepository.findById(99L)).thenReturn(Optional.empty());

        ContaExeption ex = assertThrows(
                ContaExeption.class,
                () -> service.consultarContaDb(99L)
        );

        assertEquals("Conta não existe", ex.getMessage());
        verify(contaRepository).findById(99L);
    }

    @Test
    void incluirContaDb_deveCriarESalvarConta() {
        service.incluirContaDb("Pedro", new BigDecimal("123.456"));

        ArgumentCaptor<Conta> captor = ArgumentCaptor.forClass(Conta.class);
        verify(contaRepository).save(captor.capture());

        Conta salva = captor.getValue();
        assertNull(salva.getId());
        assertEquals("Pedro", salva.getNome());
        assertEquals(new BigDecimal("123.46"), salva.getSaldo());
    }

    @Test
    void depositar_deveAlterarSaldoDaConta() throws Exception {
        Conta conta = new Conta(1L, "Pedro", new BigDecimal("10.00"));
        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));

        service.depositar(1L, new BigDecimal("5.00"));

        assertEquals(new BigDecimal("15.00"), conta.getSaldo());
        verify(contaRepository).findById(1L);
        verify(contaRepository, never()).save(any());
    }

    @Test
    void debitar_deveAlterarSaldoESalvarConta() throws Exception {
        Conta conta = new Conta(1L, "Pedro", new BigDecimal("10.00"));
        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));

        service.debitar(1L, new BigDecimal("4.00"));

        assertEquals(new BigDecimal("6.00"), conta.getSaldo());
        verify(contaRepository).findById(1L);
        verify(contaRepository).save(conta);
    }

    @Test
    void transferir_deveDebitarOrigemEDepositarDestino() throws Exception {
        Conta origem = new Conta(1L, "Origem", new BigDecimal("100.00"));
        Conta destino = new Conta(2L, "Destino", new BigDecimal("50.00"));

        when(contaRepository.findById(1L)).thenReturn(Optional.of(origem));
        when(contaRepository.findById(2L)).thenReturn(Optional.of(destino));

        service.transferir(1L, 2L, new BigDecimal("20.00"));

        assertEquals(new BigDecimal("80.00"), origem.getSaldo());
        assertEquals(new BigDecimal("70.00"), destino.getSaldo());

        verify(contaRepository).findById(1L);
        verify(contaRepository).findById(2L);
        verify(contaRepository).save(origem);
    }
}