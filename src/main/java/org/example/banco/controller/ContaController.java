package org.example.banco.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.banco.entity.dtos.ContaDTO;
import org.example.banco.entity.dtos.TransferenciaDTO;
import org.example.banco.exceptions.ContaExeption;
import org.example.banco.service.ContaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/v1/conta")
@RequiredArgsConstructor
public class ContaController {
    private final ContaService service;

    @GetMapping()
    public ResponseEntity<List<ContaDTO>> recuperarCotas() {
        return ResponseEntity.ok().body(service.consultarContasDb().stream().map(ContaDTO::new).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContaDTO> recuperarCotas(@PathVariable Long id) throws ContaExeption {
         return ResponseEntity.ok().body(new ContaDTO(service.consultarContaDb(id)));
    }

    @PutMapping("/depositar/{id}")
    public ResponseEntity<Void> depositar(@PathVariable("id") Long id,
                                          @Valid @RequestBody TransferenciaDTO dto) throws ContaExeption {
        service.depositar(id, dto.valor());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/debitar/{id}")
    public ResponseEntity<Void> debitar(@PathVariable("id") Long id,
                                         @Valid  @RequestBody TransferenciaDTO dto) throws ContaExeption {
        service.debitar(id, dto.valor());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id-cedente}/{id-destinatario}")
    public ResponseEntity<Void> transferir(@PathVariable("id-cedente") Long idCedente,
                                               @PathVariable("id-destinatario") Long idDestinatario,
                                              @Valid  @RequestBody TransferenciaDTO dto) throws ContaExeption {
        service.transferir(idCedente, idDestinatario, dto.valor());
        return ResponseEntity.ok().build();
    }

    @PostMapping()
    public ResponseEntity<Void> criarConta(
           @Valid  @RequestBody ContaDTO dto) {
        service.incluirContaDb(dto.titular(), dto.saldo());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarConta(@PathVariable Long id) {
        service.excluirContaDb(id);
        return ResponseEntity.ok().build();
    }
}
