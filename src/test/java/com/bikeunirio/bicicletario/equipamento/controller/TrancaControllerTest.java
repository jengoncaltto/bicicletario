package com.bikeunirio.bicicletario.equipamento.controller;

import com.bikeunirio.bicicletario.equipamento.dto.IntegrarTrancaRequestDTO;
import com.bikeunirio.bicicletario.equipamento.dto.RetiradaTrancaRequestDTO;
import com.bikeunirio.bicicletario.equipamento.dto.TrancaDTO;
import com.bikeunirio.bicicletario.equipamento.entity.Bicicleta;
import com.bikeunirio.bicicletario.equipamento.entity.Tranca;
import com.bikeunirio.bicicletario.equipamento.enums.StatusTranca;
import com.bikeunirio.bicicletario.equipamento.service.TrancaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

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

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals(1, resposta.getBody().size());
        assertEquals("Tranca A", resposta.getBody().get(0).getModelo());
        verify(trancaService).listarTrancas();
    }

    @Test
    void deveRetornarListaVaziaComSucesso() {
        when(trancaService.listarTrancas()).thenReturn(Collections.emptyList());
        ResponseEntity<List<Tranca>> resposta = trancaController.listarTrancasCadastradas();

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertTrue(resposta.getBody().isEmpty());
        verify(trancaService).listarTrancas();
    }

    /* ---------- cadastrarTranca ---------- */
    @Test
    void deveCadastrarTrancaComSucesso() {
        TrancaDTO dto = new TrancaDTO("Nova Tranca", "2023", null);
        Tranca nova = new Tranca();
        nova.setModelo(dto.getModelo());
        nova.setAnoDeFabricacao(dto.getAnoDeFabricacao());

        when(trancaService.cadastrarTranca(any(Tranca.class))).thenReturn(nova);

        ResponseEntity<Object> resposta = trancaController.cadastrarTranca(dto);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals(nova, resposta.getBody());
        verify(trancaService).cadastrarTranca(any(Tranca.class));
    }

    @Test
    void deveRetornarErro422AoCadastrarTrancaInvalida() {
        TrancaDTO dto = new TrancaDTO("", "", null);

        when(trancaService.cadastrarTranca(any(Tranca.class)))
                .thenThrow(new IllegalArgumentException("Dados inválidos."));

        ResponseEntity<Object> resposta = trancaController.cadastrarTranca(dto);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, resposta.getStatusCode());
        verify(trancaService).cadastrarTranca(any(Tranca.class));
    }


    /* ---------- buscarTrancaPorId ---------- */
    @Test
    void deveBuscarTrancaPorIdComSucesso() {
        Tranca tranca = new Tranca();
        tranca.setModelo("Tranca B");
        when(trancaService.buscarPorId(1L)).thenReturn(tranca);

        ResponseEntity<Object> resposta = trancaController.buscarTrancaPorId(1L);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals(tranca, resposta.getBody());
        verify(trancaService).buscarPorId(1L);
    }

    @Test
    void deveRetornarErro404AoBuscarTrancaInexistente() {
        when(trancaService.buscarPorId(99L))
                .thenThrow(new IllegalArgumentException("Tranca não encontrada"));

        ResponseEntity<Object> resposta = trancaController.buscarTrancaPorId(99L);

        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
        verify(trancaService).buscarPorId(99L);
    }

    /* ---------- editarTranca ---------- */
    @Test
    void deveEditarTrancaComSucesso() {
        // Cria a tranca existente
        Tranca trancaExistente = new Tranca();
        trancaExistente.setModelo("Tranca Original");
        trancaExistente.setAnoDeFabricacao("2020");

        // Cria a tranca que será retornada após edição
        Tranca trancaAtualizada = new Tranca();
        trancaAtualizada.setModelo("Tranca Atualizada");
        trancaAtualizada.setAnoDeFabricacao("2025");

        // Mock da busca por id
        when(trancaService.buscarPorId(1L)).thenReturn(trancaExistente);
        // Mock da edição
        when(trancaService.editarTranca(eq(1L), any(Tranca.class))).thenReturn(trancaAtualizada);

        // Dados enviados pelo controller
        Tranca trancaParaEditar = new Tranca();
        trancaParaEditar.setModelo("Tranca Atualizada");
        trancaParaEditar.setAnoDeFabricacao("2025");

        ResponseEntity<Object> resposta = trancaController.editarTranca(1L, trancaParaEditar);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals(trancaAtualizada, resposta.getBody());

        verify(trancaService).editarTranca(eq(1L), any(Tranca.class));
    }

    @Test
    void deveRetornarErro404AoEditarTrancaNaoEncontrada() {
        Tranca trancaParaEditar = new Tranca();
        trancaParaEditar.setModelo("Tranca Inexistente");
        trancaParaEditar.setAnoDeFabricacao("2025");

        // Mock da busca por id lançando exceção
        when(trancaService.buscarPorId(99L))
                .thenThrow(new IllegalArgumentException("Tranca não encontrada"));

        ResponseEntity<Object> resposta = trancaController.editarTranca(99L, trancaParaEditar);

        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
        verify(trancaService, never()).editarTranca(eq(99L), any(Tranca.class));
    }

    @Test
    void deveRetornarErro422AoEditarTrancaInvalida() {
        Tranca trancaExistente = new Tranca();
        trancaExistente.setModelo("Tranca Original");
        trancaExistente.setAnoDeFabricacao("2020");

        // Mock da busca por id
        when(trancaService.buscarPorId(1L)).thenReturn(trancaExistente);

        Tranca trancaParaEditar = new Tranca();
        trancaParaEditar.setModelo(""); // inválido
        trancaParaEditar.setAnoDeFabricacao("2025");

        // Mock da edição lançando exceção
        when(trancaService.editarTranca(eq(1L), any(Tranca.class)))
                .thenThrow(new IllegalArgumentException("Dados inválidos."));

        ResponseEntity<Object> resposta = trancaController.editarTranca(1L, trancaParaEditar);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, resposta.getStatusCode());
        verify(trancaService).editarTranca(eq(1L), any(Tranca.class));
    }


    /* ---------- excluirTranca ---------- */
    @Test
    void deveRemoverTrancaComSucesso() {
        Tranca tranca = new Tranca();
        when(trancaService.removerTranca(1L)).thenReturn(tranca);

        ResponseEntity<Object> resposta = trancaController.removerTranca(1L);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals(tranca, resposta.getBody());
        verify(trancaService).removerTranca(1L);
    }

    @Test
    void deveRetornarErro404AoRemoverTrancaNaoEncontrada() {
        when(trancaService.removerTranca(99L))
                .thenThrow(new IllegalArgumentException("Tranca não encontrada"));

        ResponseEntity<Object> resposta = trancaController.removerTranca(99L);

        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
        verify(trancaService).removerTranca(99L);
    }

    /* ---------- retornarBicicletaNaTranca ---------- */
    @Test
    void deveRetornarBicicletaNaTrancaComSucesso() {
        when(trancaService.retornarBicicletaNaTranca(1L)).thenReturn(10L);

        ResponseEntity<Object> resposta = trancaController.retornarBicicletaNaTranca(1L);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals(10L, resposta.getBody());
        verify(trancaService).retornarBicicletaNaTranca(1L);
    }

    @Test
    void deveRetornarErro404SeTrancaNaoPossuirBicicleta() {
        when(trancaService.retornarBicicletaNaTranca(1L))
                .thenThrow(new IllegalArgumentException("Nenhuma bicicleta nesta tranca."));

        ResponseEntity<Object> resposta = trancaController.retornarBicicletaNaTranca(1L);

        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
        verify(trancaService).retornarBicicletaNaTranca(1L);
    }

    /* ---------- alterarStatusDaTranca ---------- */
    @Test
    void deveAlterarStatusComSucesso() {
        Tranca tranca = new Tranca();
        tranca.setStatus(StatusTranca.OCUPADA);
        when(trancaService.alterarStatusDaTranca(1L, "OCUPADA")).thenReturn(tranca);

        ResponseEntity<Object> resposta = trancaController.alterarStatusDaTranca(1L, "OCUPADA");

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals(tranca, resposta.getBody());
        verify(trancaService).alterarStatusDaTranca(1L, "OCUPADA");
    }

    @Test
    void deveRetornarErro404AoAlterarStatusTrancaNaoEncontrada() {
        when(trancaService.alterarStatusDaTranca(99L, "OCUPADA"))
                .thenThrow(new IllegalArgumentException("Tranca não encontrada"));

        ResponseEntity<Object> resposta = trancaController.alterarStatusDaTranca(99L, "OCUPADA");

        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
        verify(trancaService).alterarStatusDaTranca(99L, "OCUPADA");
    }

    @Test
    void deveRetornarErro422QuandoStatusForInvalido() {
        when(trancaService.alterarStatusDaTranca(1L, "QUEBRADA"))
                .thenThrow(new IllegalArgumentException("Status inválido: QUEBRADA"));

        ResponseEntity<Object> resposta = trancaController.alterarStatusDaTranca(1L, "QUEBRADA");

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, resposta.getStatusCode());
        verify(trancaService).alterarStatusDaTranca(1L, "QUEBRADA");
    }

    /*------------------trancar------------*/
    @Test
    void deveTrancarComSucesso() {
        Tranca tranca = new Tranca();

        when(trancaService.trancar(1L)).thenReturn(tranca);

        ResponseEntity<Object> resposta = trancaController.trancar(1L);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertSame(tranca, resposta.getBody());

        verify(trancaService).trancar(1L);
    }

    @Test
    void deveRetornar404AoTrancarQuandoTrancaNaoEncontrada() {
        Long idTranca = 10L;

        when(trancaService.trancar(idTranca))
                .thenThrow(new IllegalArgumentException("Tranca não encontrada"));

        ResponseEntity<Object> resposta = trancaController.trancar(idTranca);

        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());

        Map<String, Object> body = (Map<String, Object>) resposta.getBody();
        assertEquals("Tranca não encontrada", body.get("mensagem"));

        verify(trancaService).trancar(idTranca);
    }

    @Test
    void deveRetornar422AoTrancarQuandoFalhar() {
        Long idTranca = 5L;

        when(trancaService.trancar(idTranca))
                .thenThrow(new IllegalArgumentException("Falha ao trancar"));

        ResponseEntity<Object> resposta = trancaController.trancar(idTranca);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, resposta.getStatusCode());

        Map<String, Object> body = (Map<String, Object>) resposta.getBody();
        assertEquals("Falha ao trancar", body.get("mensagem"));

        verify(trancaService).trancar(idTranca);
    }


    /*---------------destrancar-----------*/

    @Test
    void deveDestrancarComSucesso() {
        Long idTranca = 2L;
        Tranca tranca = new Tranca();

        when(trancaService.destrancar(idTranca)).thenReturn(tranca);

        ResponseEntity<Object> resposta = trancaController.destrancar(idTranca);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals(tranca, resposta.getBody());

        verify(trancaService).destrancar(idTranca);
    }

    @Test
    void deveRetornar404AoDestrancarQuandoTrancaNaoEncontrada() {
        Long idTranca = 20L;

        when(trancaService.destrancar(idTranca))
                .thenThrow(new IllegalArgumentException("Tranca não encontrada"));

        ResponseEntity<Object> resposta = trancaController.destrancar(idTranca);

        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());

        Map<String, Object> body = (Map<String, Object>) resposta.getBody();
        assertEquals("Tranca não encontrada", body.get("mensagem"));

        verify(trancaService).destrancar(idTranca);
    }

    @Test
    void deveRetornar422AoDestrancarQuandoFalhar() {
        Long idTranca = 3L;

        when(trancaService.destrancar(idTranca))
                .thenThrow(new IllegalArgumentException("Tranca com problema"));

        ResponseEntity<Object> resposta = trancaController.destrancar(idTranca);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, resposta.getStatusCode());

        Map<String, Object> body = (Map<String, Object>) resposta.getBody();
        assertEquals("Tranca com problema", body.get("mensagem"));

        verify(trancaService).destrancar(idTranca);
    }

    /* ---------- integrarNaRede ---------- */
    @Test
    void deveIntegrarTrancaNaRedeComSucesso() {
        IntegrarTrancaRequestDTO dto = new IntegrarTrancaRequestDTO();
        dto.setNumeroTranca(100);
        dto.setMatriculaReparador(123L);

        Tranca trancaIntegrada = new Tranca();
        trancaIntegrada.setNumero(100);

        when(trancaService.integrarNaRede(100, 123L)).thenReturn(trancaIntegrada);

        ResponseEntity<Object> resposta = trancaController.integrarNaRede(dto);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals(trancaIntegrada, resposta.getBody());

        verify(trancaService).integrarNaRede(100, 123L);
    }

    @Test
    void deveRetornarErro422QuandoFalharIntegracao() {
        IntegrarTrancaRequestDTO dto = new IntegrarTrancaRequestDTO();
        dto.setNumeroTranca(50);
        dto.setMatriculaReparador(999L);

        when(trancaService.integrarNaRede(50, 999L))
                .thenThrow(new IllegalArgumentException("Falha ao integrar tranca."));

        ResponseEntity<Object> resposta = trancaController.integrarNaRede(dto);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, resposta.getStatusCode());

        Map<String, Object> body = (Map<String, Object>) resposta.getBody();
        assertEquals("Falha ao integrar tranca.", body.get("mensagem"));

        verify(trancaService).integrarNaRede(50, 999L);
    }

    /* ---------- retirarTrancaDaRede ---------- */
    @Test
    void deveRetirarTrancaDaRedeComSucesso() {
        RetiradaTrancaRequestDTO dto = new RetiradaTrancaRequestDTO();
        dto.setNumeroTranca(30);
        dto.setOperacao("REPARO");
        dto.setMatriculaReparador(111L);

        when(trancaService.retirarTrancaDaRede(30, "REPARO", 111L))
                .thenReturn("Tranca retirada com sucesso.");

        ResponseEntity<Object> resposta = trancaController.retirarTrancaDaRede(dto);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());

        Map<String, Object> body = (Map<String, Object>) resposta.getBody();
        assertEquals("Tranca retirada com sucesso.", body.get("mensagem"));

        verify(trancaService).retirarTrancaDaRede(30, "REPARO", 111L);
    }

    @Test
    void deveRetornar404AoRetirarTrancaComNumeroInvalido() {
        RetiradaTrancaRequestDTO dto = new RetiradaTrancaRequestDTO();
        dto.setNumeroTranca(-1);
        dto.setOperacao("REPARO");
        dto.setMatriculaReparador(111L);

        when(trancaService.retirarTrancaDaRede(-1, "REPARO", 111L))
                .thenThrow(new IllegalArgumentException("Número inválido."));

        ResponseEntity<Object> resposta = trancaController.retirarTrancaDaRede(dto);

        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());

        Map<String, Object> body = (Map<String, Object>) resposta.getBody();
        assertEquals("Número inválido.", body.get("mensagem"));

        verify(trancaService).retirarTrancaDaRede(-1, "REPARO", 111L);
    }

    @Test
    void deveRetornar422AoFalharRetiradaDaRede() {
        RetiradaTrancaRequestDTO dto = new RetiradaTrancaRequestDTO();
        dto.setNumeroTranca(90);
        dto.setOperacao("APOSENTADORIA");
        dto.setMatriculaReparador(222L);

        when(trancaService.retirarTrancaDaRede(90, "APOSENTADORIA", 222L))
                .thenThrow(new IllegalArgumentException("Operação não permitida."));

        ResponseEntity<Object> resposta = trancaController.retirarTrancaDaRede(dto);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, resposta.getStatusCode());

        Map<String, Object> body = (Map<String, Object>) resposta.getBody();
        assertEquals("Operação não permitida.", body.get("mensagem"));

        verify(trancaService).retirarTrancaDaRede(90, "APOSENTADORIA", 222L);
    }


}
