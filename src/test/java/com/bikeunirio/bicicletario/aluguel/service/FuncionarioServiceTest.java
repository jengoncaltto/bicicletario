package com.bikeunirio.bicicletario.aluguel.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.bikeunirio.bicicletario.aluguel.dto.FuncionarioRequest;
// Removendo importações não utilizadas
// import com.bikeunirio.bicicletario.aluguel.entity.Funcionario;
// import com.bikeunirio.bicicletario.aluguel.repository.FuncionarioRepository;
import com.bikeunirio.bicicletario.aluguel.webservice.ExampleCpfValidationClient;

public class FuncionarioServiceTest {

    // @Mock - O repository está comentado no Service, então o mock não é necessário/usado
    // private FuncionarioRepository repository;

    @Mock
    private ExampleCpfValidationClient cpfClient;

    @InjectMocks
    private FuncionarioService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // --- TESTE DE SUCESSO AJUSTADO ---
    @Test
    void deveProcessarCadastroQuandoCpfValidoESenhasCoincidem() {
        // ARRANGE (Preparação)
        FuncionarioRequest request = new FuncionarioRequest();
        request.setCpf("12345678901");
        request.setSenha("123456");
        request.setConfirmacaoSenha("123456");

        // Simula que o serviço externo retorna true (CPF válido)
        when(cpfClient.validarCpf(request.getCpf())).thenReturn(true);

        // ACT (Execução)
        // O método não retorna nada (void), então apenas o executamos e verificamos que não lança exceção
        service.cadastrarFuncionario(request);

        // ASSERT (Verificação)
        // Verifica se o método de validação do CPF foi chamado exatamente uma vez
        verify(cpfClient, times(1)).validarCpf(request.getCpf());
        // Não é possível verificar o save no repositório, pois ele está comentado.
    }

    // --- TESTE DE FALHA DE SENHAS ---
    @Test
    void deveLancarExceptionQuandoSenhasNaoCoincidem() {
        // ARRANGE (Preparação)
        FuncionarioRequest request = new FuncionarioRequest();
        request.setSenha("123");
        request.setConfirmacaoSenha("456");

        // ACT & ASSERT (Execução e Verificação)
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.cadastrarFuncionario(request);
        });

        assertEquals("Senhas não coincidem", exception.getMessage());
        // Verifica que o cpfClient NÃO foi chamado, pois o erro de senha é o primeiro
        verify(cpfClient, times(0)).validarCpf(anyString());
    }

    // --- TESTE DE FALHA DE CPF INVÁLIDO ---
    @Test
    void deveLancarExceptionQuandoCpfInvalido() {
        // ARRANGE (Preparação)
        FuncionarioRequest request = new FuncionarioRequest();
        request.setSenha("123");
        request.setConfirmacaoSenha("123");
        request.setCpf("123");

        // Simula que o serviço externo retorna false (CPF inválido)
        when(cpfClient.validarCpf(request.getCpf())).thenReturn(false);

        // ACT & ASSERT (Execução e Verificação)
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.cadastrarFuncionario(request);
        });

        assertEquals("CPF inválido segundo serviço externo", exception.getMessage());
        // Verifica se o cpfClient foi chamado exatamente uma vez para a validação
        verify(cpfClient, times(1)).validarCpf(request.getCpf());
    }
}