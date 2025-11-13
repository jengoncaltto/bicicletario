package com.bikeunirio.bicicletario.equipamento.controller;

import com.bikeunirio.bicicletario.equipamento.dto.TrancaDTO;

import com.bikeunirio.bicicletario.equipamento.entity.Bicicleta;
import com.bikeunirio.bicicletario.equipamento.entity.Totem;
import com.bikeunirio.bicicletario.equipamento.entity.Tranca;
import com.bikeunirio.bicicletario.equipamento.service.TrancaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tranca")
public class TrancaController {

    private final TrancaService trancaService;

    public TrancaController(TrancaService trancaService) {
        this.trancaService = trancaService;
    }

    //---------- listar todas trancas ----------
    @GetMapping
    public ResponseEntity<List<Tranca>> listarTrancasCadastradas() {
        List<Tranca> trancas = trancaService.listarTrancas();
        return ResponseEntity.ok(trancas);
    }

    //---------- cadastrar tranca ----------

    @PostMapping
    public ResponseEntity<Object> cadastrarTranca(@RequestBody TrancaDTO trancaDTO) {
        try {
            // RN: Verificar se o ID da bicicleta é válido e se a bicicleta existe
            Bicicleta bicicleta = null;
            // Verifica se o ID da bicicleta foi enviado e é válido
            if (trancaDTO.getBicicletaId() != null && trancaDTO.getBicicletaId() <= 0) {
                throw new IllegalArgumentException("ID da bicicleta deve ser um valor positivo.");
            }

            // Mapeamento e criação da Tranca
            Tranca tranca = new Tranca();
            tranca.setModelo(trancaDTO.getModelo());
            tranca.setAnoDeFabricacao(trancaDTO.getAnoDeFabricacao());
            tranca.setBicicleta(bicicleta);

            // Se a entidade Tranca tem número e status, eles devem ser inicializados
            // ou definidos no serviço/entidade. Não podem ser passados pelo DTO.

            Tranca novaTranca = trancaService.cadastrarTranca(tranca);
            return ResponseEntity.ok(novaTranca);
        } catch (IllegalArgumentException e) {
            // Se a bicicleta não for encontrada ou dados inválidos
            return erro422(e);
        }
    }

     // ---------- buscar tranca por id ----------
    @GetMapping("/{idTranca}")
    public ResponseEntity<Object> buscarTrancaPorId(@PathVariable Long idTranca) {
        try {
            Tranca tranca = trancaService.buscarPorId(idTranca);
            return ResponseEntity.ok(tranca);
        } catch (IllegalArgumentException e) {
            return erro404(e);
        }
    }

     //---------- editar tranca ----------
     @PutMapping("/{idTranca}")
     public ResponseEntity<Object> editarTranca(@PathVariable Long idTranca,
                                                @RequestBody Tranca trancaAtualizada) {
         try {
             // 1. Busca a tranca existente
             Tranca trancaExistente = trancaService.buscarPorId(idTranca);

             // 2. Atualiza os campos, exceto id e numero
             if (trancaAtualizada.getModelo() != null && !trancaAtualizada.getModelo().isBlank()) {
                 trancaExistente.setModelo(trancaAtualizada.getModelo());
             }
             if (trancaAtualizada.getAnoDeFabricacao() != null && !trancaAtualizada.getAnoDeFabricacao().isBlank()) {
                 trancaExistente.setAnoDeFabricacao(trancaAtualizada.getAnoDeFabricacao());
             }
             if (trancaAtualizada.getStatus() != null) {
                 trancaExistente.setStatus(trancaAtualizada.getStatus());
             }

             // 3. Salva a entidade atualizada
             Tranca atualizada = trancaService.editarTranca(idTranca, trancaExistente);

             return ResponseEntity.ok(atualizada);

         } catch (IllegalArgumentException e) {
             if (e.getMessage().contains("não encontrada")) {
                 return erro404(e);
             }
             return erro422(e);
         }
     }

    // ---------- remover tranca----------
    @DeleteMapping("/{idTranca}")
    public ResponseEntity<Object> removerTranca(@PathVariable Long idTranca) {
        try {
            Tranca removida = trancaService.removerTranca(idTranca);
            return ResponseEntity.ok(removida);
        } catch (IllegalArgumentException e) {
            return erro404(e);
        }
    }

     //---------- RETORNAR BICICLETA ASSOCIADA ----------
    @GetMapping("/{idTranca}/bicicleta")
    public ResponseEntity<Object> retornarBicicletaNaTranca(@PathVariable Long idTranca) {
        try {
            Object bicicleta = trancaService.retornarBicicletaNaTranca(idTranca);
            return ResponseEntity.ok(bicicleta);
        } catch (IllegalArgumentException e) {
            return erro404(e);
        }
    }
     //---------- ALTERAR STATUS DA TRANCA ----------
    @PostMapping("/{idTranca}/status/{acao}")
    public ResponseEntity<Object> alterarStatusDaTranca(@PathVariable Long idTranca, @PathVariable String acao) {
        try {
            Tranca atualizada = trancaService.alterarStatusDaTranca(idTranca, acao);
            return ResponseEntity.ok(atualizada);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("não encontrada")) {
                return erro404(e);
            }
            return erro422(e);
        }
    }

    /* ---------- MÉTODOS AUXILIARES DE ERRO ---------- */
    private ResponseEntity<Object> erro404(IllegalArgumentException e) {
        return ResponseEntity.status(404).body(
                Map.of("codigo", "NAO_ENCONTRADO", "mensagem", e.getMessage())
        );
    }

    private ResponseEntity<Object> erro422(IllegalArgumentException e) {
        return ResponseEntity.status(422).body(
                Map.of("codigo", "DADOS_INVALIDOS", "mensagem", e.getMessage())
        );
    }
}
