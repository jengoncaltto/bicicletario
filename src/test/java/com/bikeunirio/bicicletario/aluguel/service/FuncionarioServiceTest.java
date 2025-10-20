package com.bikeunirio.bicicletario.aluguel.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.bikeunirio.bicicletario.aluguel.dto.FuncionarioRequest;
import com.bikeunirio.bicicletario.aluguel.entity.Funcionario;
import com.bikeunirio.bicicletario.aluguel.repository.FuncionarioRepository;
import com.bikeunirio.bicicletario.aluguel.webservice.ExampleCpfValidationClient;

public class FuncionarioServiceTest {

    @Mock
    private FuncionarioRepository repository;

    @Mock
    private ExampleCpfValidationClient cpfClient;

    @InjectMocks
    private FuncionarioService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCadastrarFuncionarioQuandoCpfValidoESemDuplicidade() {
        FuncionarioRequest request = new FuncionarioRequest();
        request.setNome("João");
        request.setEmail("joao@example.com");
        request.setCpf("12345678901");
        request.setIdade(30);
        request.setFuncao("Atendente");
        request.setSenha("123456");
        request.setConfirmacaoSenha("123456");

        when(repository.existsByEmail(request.getEmail())).thenReturn(false);
        when(repository.existsByCpf(request.getCpf())).thenReturn(false);
        when(cpfClient.validarCpf(request.getCpf())).thenReturn(true);

        Funcionario funcionarioSalvo = new Funcionario();
        funcionarioSalvo.setId(1L);
        when(repository.save(any(Funcionario.class))).thenReturn(funcionarioSalvo);

        Funcionario resultado = service.cadastrarFuncionario(request);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    void deveLancarExceptionQuandoSenhasNaoCoincidem() {
        FuncionarioRequest request = new FuncionarioRequest();
        request.setSenha("123");
        request.setConfirmacaoSenha("456");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.cadastrarFuncionario(request);
        });

        assertEquals("Senhas não coincidem", exception.getMessage());
    }

    @Test
    void deveLancarExceptionQuandoCpfInvalido() {
        FuncionarioRequest request = new FuncionarioRequest();
        request.setSenha("123");
        request.setConfirmacaoSenha("123");
        request.setEmail("teste@example.com");
        request.setCpf("123"); // inválido

        when(repository.existsByEmail(anyString())).thenReturn(false);
        when(repository.existsByCpf(anyString())).thenReturn(false);
        when(cpfClient.validarCpf(anyString())).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.cadastrarFuncionario(request);
        });

        assertEquals("CPF inválido segundo serviço externo", exception.getMessage());
    }
}
