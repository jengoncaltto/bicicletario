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
    private BicicletaService service;

    @GetMapping
    public ResponseEntity<List<Bicicleta>> listarBicicletas() {
        List<Bicicleta> bicicletas = service.listarBicicletas();
        if (bicicletas.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204
        }
        return ResponseEntity.ok(bicicletas);
    }

    @PostMapping
    public ResponseEntity<Object> cadastrarBicicleta(@RequestBody Bicicleta bicicleta) {
        try {
            Bicicleta novaBicicleta = service.cadastrarBicicleta(bicicleta);
            return ResponseEntity.ok(novaBicicleta);
        } catch (IllegalArgumentException e) {
            // Caso os dados sejam inválidos
            return ResponseEntity.unprocessableEntity()
                    .body(List.of(Map.of(
                            "codigo", "DADOS_INVALIDOS",
                            "mensagem", e.getMessage()
                    )));
        }
    }

    @GetMapping("/{idBicicleta}")
    public ResponseEntity<?> retornarBicicleta(@PathVariable Long idBicicleta) {
        try {
            Bicicleta bicicleta = service.retornarBicicleta(idBicicleta);
            return ResponseEntity.ok(bicicleta);

        } catch (IllegalArgumentException e) {
            // Caso a mensagem indique que não foi encontrada → 404
            if (e.getMessage().contains("não encontrada")) {
                return ResponseEntity.status(404).body(
                        Map.of("codigo", "NAO_ENCONTRADO", "mensagem", e.getMessage())
                );
            }

            // Caso contrário → 422
            return ResponseEntity.status(422).body(
                    Map.of("codigo", "DADOS_INVALIDOS", "mensagem", e.getMessage())
            );
        }
    }

    @PutMapping("/{idBicicleta}")
    public void editarDadosBicicleta(@PathVariable Long idBicicleta) {}

    @DeleteMapping("/{idBicicleta}")
    public void removerBicicleta(@PathVariable Long idBicicleta) {}

    @PostMapping("/{idBicicleta}/status/{acao}")
    public void alterarStatusBicicleta(){}

    @PostMapping("/integrarNaRede")
    public void integrarBicicletaNaRede(@RequestBody Bicicleta bicicleta) {}

    @PostMapping("/retirarDaRede")
    public void retirarBicicletaDaRede(@RequestBody Bicicleta bicicleta) {}

}
