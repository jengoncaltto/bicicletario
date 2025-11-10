package com.bikeunirio.bicicletario.equipamento.controller;

import com.bikeunirio.bicicletario.equipamento.entity.Bicicleta;
import com.bikeunirio.bicicletario.equipamento.service.BicicletaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bicicleta")
public class BicicletaController {

    @Autowired
    private BicicletaService bicicletaService;

    @GetMapping
    public ResponseEntity<List<Bicicleta>> listarBicicletas() {
        List<Bicicleta> bicicletas = bicicletaService.listarBicicletas();
        return ResponseEntity.ok(bicicletas);
    }

    @PostMapping
    public ResponseEntity<Object> cadastrarBicicleta(@RequestBody Bicicleta bicicleta) {
        try {
            Bicicleta novaBicicleta = bicicletaService.cadastrarBicicleta(bicicleta);
            return ResponseEntity.ok(novaBicicleta);
        } catch (IllegalArgumentException e) {
            // Caso os dados sejam inválidos
            return erro422(e);
        }
    }

    @GetMapping("/{idBicicleta}")
    public ResponseEntity<Object> retornarBicicleta(@PathVariable Long idBicicleta) {
        try {
            Bicicleta bicicleta = bicicletaService.retornarBicicleta(idBicicleta);
            return ResponseEntity.ok(bicicleta);


        } catch (IllegalArgumentException e) {
            // Caso a mensagem indique que não foi encontrada → 404
            if (e.getMessage().contains("não encontrada")) {
                return erro404(e);
            }
            // Caso contrário → 422
            return erro422(e);
        }
    }

    @PutMapping("/{idBicicleta}")
    public ResponseEntity<Object> editarDadosBicicleta(@PathVariable Long idBicicleta, @RequestBody Bicicleta bicicleta) {
        try {
            Bicicleta atualizada = bicicletaService.editarBicicleta(idBicicleta, bicicleta);
            return ResponseEntity.ok(atualizada);
        } catch (IllegalArgumentException e) {
            // Caso a mensagem indique que não foi encontrada → 404
            if (e.getMessage().contains("não encontrada")) {
                return erro404(e);
            }
            // Caso contrário → 422
            return erro422(e);
        }
    }

    @DeleteMapping("/{idBicicleta}")
    public ResponseEntity<Object> removerBicicleta(@PathVariable Long idBicicleta) {
        try{
            Bicicleta excluida = bicicletaService.removerBicicleta(idBicicleta);
            return ResponseEntity.ok(excluida);
        }catch (IllegalArgumentException e){
            return erro404(e);
        }
    }

    @PostMapping("/{idBicicleta}/status/{acao}")
    public ResponseEntity<Object> alterarStatusBicicleta(@PathVariable Long idBicicleta, @PathVariable String acao) {
        try {
            Bicicleta atualizada = bicicletaService.alterarStatusBicicleta(idBicicleta, acao);
            return ResponseEntity.ok(atualizada);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("não encontrada")) {
                return erro404(e);
            }
            return erro422(e);
        }
    }

    /*    @PostMapping("/integrarNaRede")
    public void integrarBicicletaNaRede(@RequestBody Bicicleta bicicleta) {}

    @PostMapping("/retirarDaRede")
    public void retirarBicicletaDaRede(@RequestBody Bicicleta bicicleta) {}
*/

    private ResponseEntity<Object> erro404(IllegalArgumentException e){
        return ResponseEntity.status(404).body(
                Map.of("codigo", "NAO ENCONTRADO", "mensagem", e.getMessage())
        );
    }
    private ResponseEntity<Object> erro422(IllegalArgumentException e){
        return ResponseEntity.status(422).body(
                Map.of("codigo", "DADOS INVALIDOS", "mensagem", e.getMessage())
        );

    }
}
