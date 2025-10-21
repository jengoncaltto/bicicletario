package com.bikeunirio.bicicletario.aluguel.service;

import org.springframework.stereotype.Service;

import com.bikeunirio.bicicletario.aluguel.entity.Funcionario;

@Service
public class FuncionarioService {
    public Funcionario buscarFuncionarioPorId(Long id) {

        // Simulação de busca.
        if (id != null && id.equals(1L)) {
            Funcionario funcionario = new Funcionario();
            funcionario.setId(1L);
            funcionario.setNome("Funcionario Mock 1");
            funcionario.setEmail("mock1@unirio.br");
            funcionario.setFuncao("Atendente");
            return funcionario;
        }

        return null; // Funcionário não encontrado
    }

    public Funcionario cadastrarFuncionario(Funcionario funcionario) {

        if (funcionario.getNome() == null || funcionario.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do funcionário é obrigatório para o cadastro.");
        }

        if (funcionario.getEmail() == null || funcionario.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("O e-mail do funcionário é obrigatório para o cadastro.");
        }

        if (funcionario.getCpf() == null || funcionario.getCpf().trim().isEmpty()) {
            throw new IllegalArgumentException("O CPF do funcionário é obrigatório para o cadastro.");
        }

        if (funcionario.getId() == null) {
            funcionario.setId(99L);
        }

        System.out.println("Funcionario (Mock) cadastrado: " + funcionario.getNome());

        return funcionario;
    }
}