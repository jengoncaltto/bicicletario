package com.bikeunirio.bicicletario.equipamento.controller;

import com.bikeunirio.bicicletario.equipamento.entity.Bicicleta;
import com.bikeunirio.bicicletario.equipamento.entity.Totem;
import com.bikeunirio.bicicletario.equipamento.entity.Tranca;
import com.bikeunirio.bicicletario.equipamento.service.TotemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/totem")
public class TotemController {
    private static final String NAO_ENCONTRADO = "não encontrado";

    private final TotemService totemService;


    public TotemController(TotemService totemService) {
        this.totemService = totemService;
    }

    @GetMapping
    public ResponseEntity<List<Totem>> listarTotensCadastrados(){
        List<Totem> totens = totemService.listarTotens();
        return ResponseEntity.ok(totens);
    }

    @PostMapping
    public ResponseEntity<Object> cadastrarTotem(@RequestBody Totem totem){
        try {
            Totem novo = totemService.cadastrarTotem(totem);
            return ResponseEntity.ok(novo);
        } catch (IllegalArgumentException e) {
            return erro422(e);
        }
    }

    @DeleteMapping("/{idTotem}")
    public ResponseEntity<Object> excluirTotem(@PathVariable Long idTotem) {
        try {
            Totem removido = totemService.excluirTotem(idTotem);
            return ResponseEntity.ok(removido);
        } catch (IllegalArgumentException e) {
            return erro404(e);
        }
    }

    @GetMapping("/{idTotem}/trancas")
    public ResponseEntity<Object> listarTrancasDeUmTotem(@PathVariable Long idTotem) {
        try {
            List<Tranca> trancas = totemService.listarTrancasDeUmTotem(idTotem);
            return ResponseEntity.ok(trancas);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains(NAO_ENCONTRADO)) {
                return erro404(e);
            }
            return erro422(e);
        }
    }

    @GetMapping("/{idTotem}/bicicletas")
    public ResponseEntity<Object> listarBicicletasDeUmTotem(@PathVariable Long idTotem){
        try{
            List<Bicicleta> bicicletas = totemService.listarBicicletasDeUmTotem(idTotem);
            return ResponseEntity.ok(bicicletas);
        } catch (IllegalArgumentException e) {
            if(e.getMessage().contains(NAO_ENCONTRADO)){
                return erro404(e);
            }
            return erro422(e);
        }
    }


    // fora dos casos de uso implementados
    @PutMapping("/{idTotem}")
    public ResponseEntity<Object> editarTotem(@PathVariable Long idTotem, @RequestBody Totem totem) {
        try {
            Totem atualizado = totemService.editarTotem(idTotem, totem);
            return ResponseEntity.ok(atualizado);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains(NAO_ENCONTRADO)) {
                return erro404(e);
            }
            return erro422(e);
        }
    }

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
