package com.bikeunirio.bicicletario.equipamento.controller;

import com.bikeunirio.bicicletario.equipamento.entity.Bicicleta;
import com.bikeunirio.bicicletario.equipamento.service.BicicletaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/Bicicleta")
public class BicicletaController {

    private BicicletaService  service;
    public BicicletaController(BicicletaService service) {}

    @GetMapping
    public List<Bicicleta> listar() {
        List<Bicicleta> bicicletas = service.listar();
        if(bicicletas.isEmpty()){
            return null;
        }
        return bicicletas;
    }
}
