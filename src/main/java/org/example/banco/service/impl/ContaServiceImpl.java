package org.example.banco.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.banco.entity.Conta;
import org.example.banco.exceptions.ContaExeption;
import org.example.banco.repository.ContaRepository;
import org.example.banco.service.ContaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContaServiceImpl implements ContaService {
    private final ContaRepository contaRepository;

    @Transactional
    @Override
    public void excluirContaDb(Long id) {
        contaRepository.deleteById(id);
    }
    @Override
    public List<Conta> consultarContasDb() {
        return contaRepository.findAll();
    }
    @Override
    public Conta consultarContaDb(Long id) throws ContaExeption {
        return contaRepository.findById(id).orElseThrow(() -> new ContaExeption("Conta não existe"));
    }
    @Override
    @Transactional
    public void incluirContaDb(String nome, BigDecimal saldo) {
        Conta conta = new Conta(null, nome, (saldo));
        contaRepository.save(conta);
    }

    @Override
    @Transactional
    public void depositar(long l, BigDecimal v) throws ContaExeption {
        Conta c = consultarContaDb(l);
        c.depositar((v));
    }
    @Override
    @Transactional
    public void debitar(long l, BigDecimal v) throws ContaExeption {
        Conta c = consultarContaDb(l);
        c.debitar((v));
        contaRepository.save(c);
    }

    @Override
    @Transactional
    public void transferir(long origem, long destino, BigDecimal v) throws ContaExeption {
        debitar(origem, v);
        depositar(destino, v);

    }
}
