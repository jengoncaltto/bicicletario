package com.bikeunirio.bicicletario.equipamento.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/restaurarBanco")
public class BDController {

    @GetMapping
    public void restauraBancoDados(){

    }
}
