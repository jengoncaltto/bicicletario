package com.bikeunirio.bicicletario.aluguel.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bikeunirio.bicicletario.aluguel.entity.Funcionario;

public class FuncionarioServiceTest {

    private FuncionarioService service;

    @BeforeEach
    void setUp() {
        this.service = new FuncionarioService();
    }

    @Test
    void deveRetornarFuncionarioQuandoIdForUm() {
        Long idEsperado = 1L;

        Funcionario resultado = service.buscarFuncionarioPorId(idEsperado);

        assertNotNull(resultado, "O funcionário não deve ser nulo para o ID 1L.");
        assertEquals(idEsperado, resultado.getId(), "O ID retornado deve ser 1L.");
        assertEquals("Funcionario Mock 1", resultado.getNome(), "O nome deve ser o mock esperado.");
    }

    @Test
    void deveRetornarNullQuandoIdDiferenteDeUm() {
        Long idInvalido = 99L;

        Funcionario resultado = service.buscarFuncionarioPorId(idInvalido);

        assertNull(resultado, "O funcionário deve ser nulo para um ID diferente de 1L.");
    }

    @Test
    void deveRetornarNullQuandoIdForNulo() {
        Funcionario resultado = service.buscarFuncionarioPorId(null);

        assertNull(resultado, "O funcionário deve ser nulo quando o ID for nulo.");
    }

    @Test
    void deveCadastrarFuncionarioComSucessoAtribuirIdFalso() {
        Funcionario novoFuncionario = new Funcionario();
        novoFuncionario.setNome("Novo Funcionário");
        novoFuncionario.setEmail("novo@teste.com");
        novoFuncionario.setCpf("11122233344");

        Funcionario resultado = service.cadastrarFuncionario(novoFuncionario);

        assertNotNull(resultado, "O resultado do cadastro não deve ser nulo.");
        assertEquals(99L, resultado.getId(), "Um ID falso (99L) deve ser atribuído ao novo funcionário.");
        assertEquals("Novo Funcionário", resultado.getNome(), "O nome deve ser mantido.");
    }

    @Test
    void deveLancarExcecaoQuandoNomeForNulo() {
        Funcionario novoFuncionario = new Funcionario();
        novoFuncionario.setEmail("valido@teste.com");
        novoFuncionario.setCpf("11122233344");

        assertThrows(IllegalArgumentException.class, () -> {
            service.cadastrarFuncionario(novoFuncionario);
        }, "Deve lançar exceção quando o Nome for nulo.");
    }

    @Test
    void deveLancarExcecaoQuandoEmailForVazio() {
        Funcionario novoFuncionario = new Funcionario();
        novoFuncionario.setNome("Nome Valido");
        novoFuncionario.setEmail(" "); // E-mail vazio (trim().isEmpty() deve capturar)
        novoFuncionario.setCpf("11122233344");

        IllegalArgumentException excecao = assertThrows(IllegalArgumentException.class, () -> {
            service.cadastrarFuncionario(novoFuncionario);
        }, "Deve lançar exceção quando o Email for vazio.");

        assertEquals("O e-mail do funcionário é obrigatório para o cadastro.", excecao.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoCpfForNulo() {
        Funcionario novoFuncionario = new Funcionario();
        novoFuncionario.setNome("Nome Valido");
        novoFuncionario.setEmail("valido@teste.com");

        assertThrows(IllegalArgumentException.class, () -> {
            service.cadastrarFuncionario(novoFuncionario);
        }, "Deve lançar exceção quando o CPF for nulo.");
    }
}