package com.bikeunirio.bicicletario.equipamento.controller;

import com.bikeunirio.bicicletario.equipamento.entity.Bicicleta;
import com.bikeunirio.bicicletario.equipamento.service.BicicletaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bicicleta")
public class BicicletaController {

    private BicicletaService  service;
    public BicicletaController(BicicletaService service) {}

    @GetMapping
    public List<Bicicleta> listarBicicletas() {
        List<Bicicleta> bicicletas = service.listarBicicletas();
        if(bicicletas.isEmpty()){
            return null;
        }
        return bicicletas;
    }

    @PostMapping
    public void cadastrarBicicleta(@RequestBody Bicicleta bicicleta) {}

    @PostMapping("/integrarNaRede")
    public void integrarBicicletaNaRede(@RequestBody Bicicleta bicicleta) {}

    @PostMapping("/retirarDaRede")
    public void retirarBicicletaDaRede(@RequestBody Bicicleta bicicleta) {}

    @GetMapping("/{idBicicleta}")
    public void retornarBicicleta(@PathVariable Long idBicicleta) {}

    @PutMapping("/{idBicicleta}")
    public void editarDadosBicicleta(@PathVariable Long idBicicleta) {}

    @DeleteMapping("/{idBicicleta}")
    public void removerBicicleta(@PathVariable Long idBicicleta) {}

    @PostMapping("/{idBicicleta}/status/{acao}")
    public void alterarStatusBicicleta(){}

}
