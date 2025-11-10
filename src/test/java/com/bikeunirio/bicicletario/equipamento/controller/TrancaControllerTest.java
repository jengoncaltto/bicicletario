package com.bikeunirio.bicicletario.equipamento.controller;

import com.bikeunirio.bicicletario.equipamento.entity.Tranca;
import com.bikeunirio.bicicletario.equipamento.enums.StatusTranca;
import com.bikeunirio.bicicletario.equipamento.service.TrancaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrancaControllerTest {

    @Mock
    private TrancaService trancaService;

    @InjectMocks
    private TrancaController trancaController;

    /* ---------- listarTrancasCadastradas ---------- */
    @Test
    void deveRetornarStatus200EListaDeTrancas() {
        Tranca t1 = new Tranca();
        t1.setNumero(101);
        t1.setModelo("Tranca A");

        when(trancaService.listarTrancas()).thenReturn(List.of(t1));

        ResponseEntity<List<Tranca>> resposta = trancaController.listarTrancasCadastradas();

        assertEquals(200, resposta.getStatusCodeValue());
        assertEquals(1, resposta.getBody().size());
        assertEquals("Tranca A", resposta.getBody().get(0).getModelo());
        verify(trancaService).listarTrancas();
    }

    @Test
    void deveRetornarListaVaziaComSucesso() {
        when(trancaService.listarTrancas()).thenReturn(Collections.emptyList());

        ResponseEntity<List<Tranca>> resposta = trancaController.listarTrancasCadastradas();

        assertEquals(200, resposta.getStatusCodeValue());
        assertTrue(resposta.getBody().isEmpty());
        verify(trancaService).listarTrancas();
    }

    /* ---------- cadastrarTranca ---------- */
    @Test
    void deveCadastrarTrancaComSucesso() {
        Tranca nova = new Tranca();
        nova.setNumero(202);
        nova.setModelo("Nova Tranca");

        when(trancaService.cadastrarTranca(nova)).thenReturn(nova);

        ResponseEntity<Object> resposta = trancaController.cadastrarTranca(nova);

        assertEquals(200, resposta.getStatusCodeValue());
        assertEquals(nova, resposta.getBody());
        verify(trancaService).cadastrarTranca(nova);
    }

    @Test
    void deveRetornarErro422AoCadastrarTrancaInvalida() {
        Tranca invalida = new Tranca();
        when(trancaService.cadastrarTranca(invalida))
                .thenThrow(new IllegalArgumentException("Dados inválidos."));

        ResponseEntity<Object> resposta = trancaController.cadastrarTranca(invalida);

        assertEquals(422, resposta.getStatusCodeValue());
        verify(trancaService).cadastrarTranca(invalida);
    }

    /* ---------- buscarTrancaPorId ---------- */
    @Test
    void deveBuscarTrancaPorIdComSucesso() {
        Tranca tranca = new Tranca();
        tranca.setModelo("Tranca B");

        when(trancaService.buscarPorId(1L)).thenReturn(tranca);

        ResponseEntity<Object> resposta = trancaController.buscarTrancaPorId(1L);

        assertEquals(200, resposta.getStatusCodeValue());
        assertEquals(tranca, resposta.getBody());
        verify(trancaService).buscarPorId(1L);
    }

    @Test
    void deveRetornarErro404AoBuscarTrancaInexistente() {
        when(trancaService.buscarPorId(99L))
                .thenThrow(new IllegalArgumentException("Tranca não encontrada"));

        ResponseEntity<Object> resposta = trancaController.buscarTrancaPorId(99L);

        assertEquals(404, resposta.getStatusCodeValue());
        verify(trancaService).buscarPorId(99L);
    }

    /* ---------- editarTranca ---------- */
    @Test
    void deveEditarTrancaComSucesso() {
        Tranca atualizada = new Tranca();
        atualizada.setModelo("Tranca Atualizada");

        when(trancaService.editarTranca(1L, atualizada)).thenReturn(atualizada);

        ResponseEntity<Object> resposta = trancaController.editarTranca(1L, atualizada);

        assertEquals(200, resposta.getStatusCodeValue());
        assertEquals(atualizada, resposta.getBody());
        verify(trancaService).editarTranca(1L, atualizada);
    }

    @Test
    void deveRetornarErro404AoEditarTrancaNaoEncontrada() {
        Tranca tranca = new Tranca();
        when(trancaService.editarTranca(99L, tranca))
                .thenThrow(new IllegalArgumentException("Tranca não encontrada"));

        ResponseEntity<Object> resposta = trancaController.editarTranca(99L, tranca);

        assertEquals(404, resposta.getStatusCodeValue());
        verify(trancaService).editarTranca(99L, tranca);
    }

    @Test
    void deveRetornarErro422AoEditarTrancaInvalida() {
        Tranca tranca = new Tranca();
        when(trancaService.editarTranca(1L, tranca))
                .thenThrow(new IllegalArgumentException("Dados inválidos."));

        ResponseEntity<Object> resposta = trancaController.editarTranca(1L, tranca);

        assertEquals(422, resposta.getStatusCodeValue());
        verify(trancaService).editarTranca(1L, tranca);
    }

    /* ---------- excluirTranca ---------- */
    @Test
    void deveExcluirTrancaComSucesso() {
        Tranca tranca = new Tranca();
        when(trancaService.excluirTranca(1L)).thenReturn(tranca);

        ResponseEntity<Object> resposta = trancaController.excluirTranca(1L);

        assertEquals(200, resposta.getStatusCodeValue());
        assertEquals(tranca, resposta.getBody());
        verify(trancaService).excluirTranca(1L);
    }

    @Test
    void deveRetornarErro404AoExcluirTrancaNaoEncontrada() {
        when(trancaService.excluirTranca(99L))
                .thenThrow(new IllegalArgumentException("Tranca não encontrada"));

        ResponseEntity<Object> resposta = trancaController.excluirTranca(99L);

        assertEquals(404, resposta.getStatusCodeValue());
        verify(trancaService).excluirTranca(99L);
    }

    /* ---------- retornarBicicletaNaTranca ---------- */
    @Test
    void deveRetornarBicicletaNaTrancaComSucesso() {
        when(trancaService.retornarBicicletaNaTranca(1L)).thenReturn(10L); // Exemplo: bicicleta ID = 10

        ResponseEntity<Object> resposta = trancaController.retornarBicicletaNaTranca(1L);

        assertEquals(200, resposta.getStatusCodeValue());
        assertEquals(10L, resposta.getBody());
        verify(trancaService).retornarBicicletaNaTranca(1L);
    }

    @Test
    void deveRetornarErro404SeTrancaNaoPossuirBicicleta() {
        when(trancaService.retornarBicicletaNaTranca(1L))
                .thenThrow(new IllegalArgumentException("Nenhuma bicicleta nesta tranca."));

        ResponseEntity<Object> resposta = trancaController.retornarBicicletaNaTranca(1L);

        assertEquals(404, resposta.getStatusCodeValue());
        verify(trancaService).retornarBicicletaNaTranca(1L);
    }

    /* ---------- alterarStatusDaTranca ---------- */
    @Test
    void deveAlterarStatusComSucesso() {
        Tranca tranca = new Tranca();
        tranca.setStatus(StatusTranca.OCUPADA);

        when(trancaService.alterarStatusDaTranca(1L, "OCUPADA")).thenReturn(tranca);

        ResponseEntity<Object> resposta = trancaController.alterarStatusDaTranca(1L, "OCUPADA");

        assertEquals(200, resposta.getStatusCodeValue());
        assertEquals(tranca, resposta.getBody());
        verify(trancaService).alterarStatusDaTranca(1L, "OCUPADA");
    }

    @Test
    void deveRetornarErro404AoAlterarStatusTrancaNaoEncontrada() {
        when(trancaService.alterarStatusDaTranca(99L, "OCUPADA"))
                .thenThrow(new IllegalArgumentException("Tranca não encontrada"));

        ResponseEntity<Object> resposta = trancaController.alterarStatusDaTranca(99L, "OCUPADA");

        assertEquals(404, resposta.getStatusCodeValue());
        verify(trancaService).alterarStatusDaTranca(99L, "OCUPADA");
    }

    @Test
    void deveRetornarErro422QuandoStatusForInvalido() {
        when(trancaService.alterarStatusDaTranca(1L, "QUEBRADA"))
                .thenThrow(new IllegalArgumentException("Status inválido: QUEBRADA"));

        ResponseEntity<Object> resposta = trancaController.alterarStatusDaTranca(1L, "QUEBRADA");

        assertEquals(422, resposta.getStatusCodeValue());
        verify(trancaService).alterarStatusDaTranca(1L, "QUEBRADA");
    }
}
