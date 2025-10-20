package com.bikeunirio.bicicletario.aluguel.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/funcionario")
public class FuncionarioController {

	@GetMapping("/{idFuncionario}")
	public String getFuncionarioPorId (@PathVariable (value ="palavra") String idFuncionario){
		return "Olá mundo!";					
	}
	
}