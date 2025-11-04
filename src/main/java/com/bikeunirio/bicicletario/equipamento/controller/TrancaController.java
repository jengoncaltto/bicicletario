package com.bikeunirio.bicicletario.equipamento.controller;

import com.bikeunirio.bicicletario.equipamento.repository.TrancaRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tranca")
public class TrancaController {

    private TrancaRepository trancaRepository;
    public TrancaController(TrancaRepository trancaRepository) {}
}
