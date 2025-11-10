package com.bikeunirio.bicicletario.equipamento.controller;

import com.bikeunirio.bicicletario.equipamento.entity.Tranca;
import com.bikeunirio.bicicletario.equipamento.service.TrancaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tranca")
public class TrancaController {

    @Autowired
    private TrancaService trancaService;

    /* ---------- LISTAR TODAS AS TRANÇAS ---------- */
    @GetMapping
    public ResponseEntity<List<Tranca>> listarTrancasCadastradas() {
        List<Tranca> trancas = trancaService.listarTrancas();
        return ResponseEntity.ok(trancas);
    }

    /* ---------- CADASTRAR NOVA TRANCA ---------- */
    @PostMapping
    public ResponseEntity<Object> cadastrarTranca(@RequestBody Tranca tranca) {
        try {
            Tranca nova = trancaService.cadastrarTranca(tranca);
            return ResponseEntity.ok(nova);
        } catch (IllegalArgumentException e) {
            return erro422(e);
        }
    }

    /* ---------- BUSCAR TRANCA POR ID ---------- */
    @GetMapping("/{idTranca}")
    public ResponseEntity<Object> buscarTrancaPorId(@PathVariable Long idTranca) {
        try {
            Tranca tranca = trancaService.buscarPorId(idTranca);
            return ResponseEntity.ok(tranca);
        } catch (IllegalArgumentException e) {
            return erro404(e);
        }
    }

    /* ---------- EDITAR TRANCA ---------- */
    @PutMapping("/{idTranca}")
    public ResponseEntity<Object> editarTranca(@PathVariable Long idTranca, @RequestBody Tranca tranca) {
        try {
            Tranca atualizada = trancaService.editarTranca(idTranca, tranca);
            return ResponseEntity.ok(atualizada);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("não encontrada")) {
                return erro404(e);
            }
            return erro422(e);
        }
    }

    /* ---------- EXCLUIR TRANCA ---------- */
    @DeleteMapping("/{idTranca}")
    public ResponseEntity<Object> excluirTranca(@PathVariable Long idTranca) {
        try {
            Tranca removida = trancaService.excluirTranca(idTranca);
            return ResponseEntity.ok(removida);
        } catch (IllegalArgumentException e) {
            return erro404(e);
        }
    }

    /* ---------- RETORNAR BICICLETA ASSOCIADA ---------- */
    @GetMapping("/{idTranca}/bicicleta")
    public ResponseEntity<Object> retornarBicicletaNaTranca(@PathVariable Long idTranca) {
        try {
            Object bicicleta = trancaService.retornarBicicletaNaTranca(idTranca);
            return ResponseEntity.ok(bicicleta);
        } catch (IllegalArgumentException e) {
            return erro404(e);
        }
    }

    /* ---------- ALTERAR STATUS DA TRANCA ---------- */
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

    /*   @PostMapping("/integrarNaRede")
    public void integrarTrancaNaRede(@PathVariable int idTranca){}

    @PostMapping("/retirarDaRede")
    public void retirarTrancaDaRede(@PathVariable int idTranca){}

*/

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
