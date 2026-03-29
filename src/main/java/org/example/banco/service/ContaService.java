package org.example.banco.service;

import org.example.banco.entity.Conta;
import org.example.banco.exceptions.ContaExeption;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public interface ContaService {
    void excluirContaDb(Long id);

    List<Conta> consultarContasDb();

    Conta consultarContaDb(Long id) throws ContaExeption ;

    void incluirContaDb(String nome, BigDecimal saldo) ;

    void depositar(long l, BigDecimal v) throws ContaExeption ;

    void debitar(long l, BigDecimal v) throws ContaExeption ;

    void transferir(long origem, long destino, BigDecimal v) throws ContaExeption ;
}
