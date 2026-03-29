package org.example.banco.entity.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.example.banco.entity.Conta;

import java.math.BigDecimal;

public record ContaDTO(
        Long id,
        @NotBlank(message = "titular é obrigatório")
        String titular,
        @NotNull(message = "saldo é obrigatório")
        @PositiveOrZero(message = "saldo deve ser >= 0")
        BigDecimal saldo
) {
    public ContaDTO(Conta conta){
        this(
                conta.getId(),
                conta.getNome(),
                conta.getSaldo()
        );
    }
}
