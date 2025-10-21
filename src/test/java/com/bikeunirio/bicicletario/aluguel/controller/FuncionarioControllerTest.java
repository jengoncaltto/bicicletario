package com.bikeunirio.bicicletario.aluguel.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.bikeunirio.bicicletario.aluguel.entity.Funcionario;
import com.bikeunirio.bicicletario.aluguel.service.FuncionarioService;

@WebMvcTest(FuncionarioController.class)
public class FuncionarioControllerTest {

    @Autowired
    private MockMvc mockMvc; // Simula as requisições HTTP

    @MockBean
    private FuncionarioService funcionarioService; // Mocka o Service


    @Test
    void deveRetornarStatus200EOlaMundoQuandoFuncionarioForEncontrado() throws Exception {

        Long idExistente = 1L;

        Funcionario funcionarioMock = new Funcionario();
        funcionarioMock.setId(idExistente);
        funcionarioMock.setNome("Funcionario Teste");

        when(funcionarioService.buscarFuncionarioPorId(anyLong()))
                .thenReturn(funcionarioMock);

        mockMvc.perform(get("/funcionario/{idFuncionario}", idExistente)
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk()) // Espera Status 200 OK
                .andExpect(content().string("Olá mundo!")); // Espera o corpo "Olá mundo!"
    }


    @Test
    void deveRetornarStatus404EOlaMundoQuandoFuncionarioNaoForEncontrado() throws Exception {

        Long idInexistente = 99L;

        when(funcionarioService.buscarFuncionarioPorId(anyLong()))
                .thenReturn(null);

        mockMvc.perform(get("/funcionario/{idFuncionario}", idInexistente)
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isNotFound()) // Espera Status 404 NOT FOUND
                .andExpect(content().string("Olá mundo!")); // Espera o corpo "Olá mundo!"
    }
}