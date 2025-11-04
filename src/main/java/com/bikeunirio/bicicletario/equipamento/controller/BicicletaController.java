package com.bikeunirio.bicicletario.equipamento.controller;

import com.bikeunirio.bicicletario.equipamento.service.BicicletaService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Bicicleta")
public class BicicletaController {

    private BicicletaService  service;
    public BicicletaController(BicicletaService service) {}
}
