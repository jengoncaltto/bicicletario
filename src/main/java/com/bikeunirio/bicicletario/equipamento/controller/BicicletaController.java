package com.bikeunirio.bicicletario.equipamento.controller;

import com.bikeunirio.bicicletario.equipamento.entity.Bicicleta;
import com.bikeunirio.bicicletario.equipamento.enums.StatusBicicleta;
import com.bikeunirio.bicicletario.equipamento.service.BicicletaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bicicleta")
public class BicicletaController {
    private static final String MSG_NAO_ENCONTRADA = "não encontrada";
    private static final String CODIGO_NAO_ENCONTRADO = "NAO ENCONTRADO";
    private static final String CODIGO_DADOS_INVALIDOS = "DADOS INVALIDOS";
    private static final String COD = "codigo";
    private static final String MSG = "mensagem";


    private final BicicletaService bicicletaService;

    public BicicletaController(BicicletaService bicicletaService) {
        this.bicicletaService = bicicletaService;
    }

    @GetMapping
    public ResponseEntity<List<Bicicleta>> listarBicicletas() {
        List<Bicicleta> bicicletas = bicicletaService.listarBicicletas();
        return ResponseEntity.ok(bicicletas);
    }

    @PostMapping
    public ResponseEntity<Object> cadastrarBicicleta(@RequestBody Bicicleta bicicleta) {
        try {
            if (bicicleta.getNumero() != null || bicicleta.getStatus() != null) {
                return erro422(new IllegalArgumentException(
                        "Número e status não podem ser enviados na criação."
                ));
            }
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
            if (e.getMessage().contains(MSG_NAO_ENCONTRADA)) {
                return erro404(e);
            }
            // Caso contrário → 422
            return erro422(e);
        }
    }

    @PutMapping("/{idBicicleta}")
    public ResponseEntity<Object> editarDadosBicicleta(@PathVariable Long idBicicleta, @RequestBody Bicicleta bicicleta) {
        try {
            //  Recupera do banco primeiro para comparar
            Bicicleta atual = bicicletaService.retornarBicicleta(idBicicleta);

            // Se tentar mudar o número → bloquear
            if (bicicleta.getNumero() != null &&
                    !bicicleta.getNumero().equals(atual.getNumero())) {
                throw new IllegalArgumentException("O número da bicicleta não pode ser editado.");
            }

            //  Se tentar mudar o status → bloquear
            if (bicicleta.getStatus() != null &&
                    bicicleta.getStatus() != atual.getStatus()) {
                throw new IllegalArgumentException("O status da bicicleta não pode ser editado pelo UC10.");
            }

            Bicicleta atualizada = bicicletaService.editarBicicleta(idBicicleta, bicicleta);
            return ResponseEntity.ok(atualizada);
        } catch (IllegalArgumentException e) {
            // Caso a mensagem indique que não foi encontrada → 404
            if (e.getMessage().contains(MSG_NAO_ENCONTRADA)) {
                return erro404(e);
            }
            // Caso contrário → 422
            return erro422(e);
        }
    }

    @DeleteMapping("/{idBicicleta}")
    public ResponseEntity<Object> removerBicicleta(@PathVariable Long idBicicleta) {
        try {
            Bicicleta excluida = bicicletaService.removerBicicleta(idBicicleta);
            return ResponseEntity.ok(excluida);
        } catch (IllegalArgumentException e) {
            return erro404(e);
        }
    }

    @PostMapping("/integrarNaRede")
    public ResponseEntity<Object> incluirBicicletaNaRede(@RequestBody BicicletaRedeDTO dto) {

        try {
            String mensagem = bicicletaService.incluirBicicletaNaRede(dto.getIdBicicleta());
            return ResponseEntity.ok(Map.of(MSG, mensagem));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(422).body(Map.of(
                    "erro", e.getMessage()
            ));
        }
    }


    @PostMapping("/{idBicicleta}/status/{acao}")
    public ResponseEntity<Object> alterarStatusBicicleta(@PathVariable Long idBicicleta, @PathVariable StatusBicicleta acao) {
        try {
            Bicicleta atualizada = bicicletaService.alterarStatusBicicleta(idBicicleta, acao);
            return ResponseEntity.ok(atualizada);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains(MSG_NAO_ENCONTRADA)) {
                return erro404(e);
            }
            return erro422(e);
        }
    }


    public static class BicicletaRedeDTO {
        private Long idBicicleta;

        public Long getIdBicicleta() {
            return idBicicleta;
        }

        public void setIdBicicleta(Long idBicicleta) {
            this.idBicicleta = idBicicleta;
        }
    }

    private ResponseEntity<Object> erro404(IllegalArgumentException e) {
        return ResponseEntity.status(404).body(
                Map.of(COD, CODIGO_NAO_ENCONTRADO, MSG, e.getMessage())
        );
    }

    private ResponseEntity<Object> erro422(IllegalArgumentException e) {
        return ResponseEntity.status(422).body(
                Map.of(COD, CODIGO_DADOS_INVALIDOS, MSG, e.getMessage())
        );
    }
}
