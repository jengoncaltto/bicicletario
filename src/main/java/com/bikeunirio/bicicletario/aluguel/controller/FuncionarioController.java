package com.bikeunirio.bicicletario.aluguel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bikeunirio.bicicletario.aluguel.entity.Funcionario;
import com.bikeunirio.bicicletario.aluguel.service.FuncionarioService;

@RestController
@RequestMapping("/funcionario")
public class FuncionarioController {

    @Autowired
    private FuncionarioService funcionarioService;

    @GetMapping("/{idFuncionario}")
    public ResponseEntity<?> FuncionarioPorId(@PathVariable(value = "idFuncionario") Long idFuncionario) {

        Funcionario funcionario = funcionarioService.buscarFuncionarioPorId(idFuncionario);

        if (funcionario == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Olá mundo!");
        }

        return ResponseEntity.ok("Olá mundo!");
    }
}