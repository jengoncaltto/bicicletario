package com.bikeunirio.bicicletario.aluguel.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import com.bikeunirio.bicicletario.aluguel.entity.Funcionario;
import com.bikeunirio.bicicletario.aluguel.webservice.ExampleCpfValidationClient;

public class FuncionarioServiceTest {

    /* * * @Mock private FuncionarioRepository repository; */

    @Mock
    private ExampleCpfValidationClient cpfClient;

    @InjectMocks
    private FuncionarioService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCadastrarERetornarFuncionarioQuandoCpfValidoESenhasCoincidem() {
        FuncionarioRequest request = new FuncionarioRequest();
        request.setNome("Maria");
        request.setEmail("maria@example.com");
        request.setCpf("12345678901");
        request.setSenha("senha123");
        request.setConfirmacaoSenha("senha123");

        when(cpfClient.validarCpf(request.getCpf())).thenReturn(true);

        Funcionario resultado = service.cadastrarFuncionario(request);

        assertNotNull(resultado, "O objeto Funcionario não deve ser nulo.");
        assertNotNull(resultado.getId(), "O ID deve ser gerado.");
        assertEquals(request.getNome(), resultado.getNome(), "O nome deve ser o mesmo do request.");

        verify(cpfClient, times(1)).validarCpf(request.getCpf());
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
        verify(cpfClient, times(0)).validarCpf(anyString());
    }

    @Test
    void deveLancarExceptionQuandoCpfInvalido() {
        FuncionarioRequest request = new FuncionarioRequest();
        request.setSenha("123");
        request.setConfirmacaoSenha("123");
        request.setCpf("99999999999"); // CPF de teste

        when(cpfClient.validarCpf(request.getCpf())).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.cadastrarFuncionario(request);
        });

        assertEquals("CPF inválido segundo serviço externo", exception.getMessage());
        verify(cpfClient, times(1)).validarCpf(request.getCpf());
    }
}