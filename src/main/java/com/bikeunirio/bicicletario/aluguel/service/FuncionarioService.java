package com.bikeunirio.bicicletario.aluguel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bikeunirio.bicicletario.aluguel.dto.FuncionarioRequest;
import com.bikeunirio.bicicletario.aluguel.entity.Funcionario;
import com.bikeunirio.bicicletario.aluguel.repository.FuncionarioRepository;
import com.bikeunirio.bicicletario.aluguel.webservice.ExampleCpfValidationClient;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository repository;

    @Autowired
    private ExampleCpfValidationClient cpfClient;

    public Funcionario cadastrarFuncionario(FuncionarioRequest request) {
        // Verifica senha
        if (!request.getSenha().equals(request.getConfirmacaoSenha())) {
            throw new IllegalArgumentException("Senhas não coincidem");
        }

        // Verifica duplicidade
        if (repository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email já cadastrado");
        }
        if (repository.existsByCpf(request.getCpf())) {
            throw new IllegalArgumentException("CPF já cadastrado");
        }

        if(!cpfClient.validarCpf(request.getCpf())){
            throw new IllegalArgumentException("CPF inválido segundo serviço externo");
        }

        // Cria entidade
        Funcionario funcionario = new Funcionario();
        funcionario.setNome(request.getNome());
        funcionario.setEmail(request.getEmail());
        funcionario.setIdade(request.getIdade());
        funcionario.setFuncao(request.getFuncao());
        funcionario.setCpf(request.getCpf());
        funcionario.setSenha(request.getSenha()); // ideal criptografar

        return repository.save(funcionario);
    }
}
