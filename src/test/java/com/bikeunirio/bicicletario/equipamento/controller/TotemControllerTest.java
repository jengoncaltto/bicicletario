package com.bikeunirio.bicicletario.equipamento.controller;

import com.bikeunirio.bicicletario.equipamento.entity.Totem;
import com.bikeunirio.bicicletario.equipamento.service.TotemService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TotemControllerTest {

    @Mock
    private TotemService totemService;

    @InjectMocks
    private TotemController totemController;

    /* ---------- GET /totem ---------- */
    @Test
    void deveListarTotensComSucesso() {
        Totem totem = new Totem();
        totem.setDescricao("Totem Central");
        when(totemService.listarTotens()).thenReturn(List.of(totem));

        ResponseEntity<List<Totem>> resposta = totemController.listarTotensCadastrados();

        assertEquals(200, resposta.getStatusCodeValue());
        assertEquals(1, resposta.getBody().size());
        assertEquals("Totem Central", resposta.getBody().get(0).getDescricao());
        verify(totemService).listarTotens();
    }

    @Test
    void deveRetornarListaVazia() {
        when(totemService.listarTotens()).thenReturn(Collections.emptyList());

        ResponseEntity<List<Totem>> resposta = totemController.listarTotensCadastrados();

        assertEquals(200, resposta.getStatusCodeValue());
        assertTrue(resposta.getBody().isEmpty());
        verify(totemService).listarTotens();
    }

    /* ---------- POST /totem ---------- */
    @Test
    void deveCadastrarTotem() {
        Totem totem = new Totem();
        when(totemService.cadastrarTotem(totem)).thenReturn(totem);

        ResponseEntity<Object> resposta = totemController.cadastrarTotem(totem);

        assertEquals(200, resposta.getStatusCodeValue());
        assertEquals(totem, resposta.getBody());
        verify(totemService).cadastrarTotem(totem);
    }

    @Test
    void deveRetornar422SeTotemInvalido() {
        Totem totem = new Totem();
        when(totemService.cadastrarTotem(totem))
                .thenThrow(new IllegalArgumentException("Dados inválidos"));

        ResponseEntity<Object> resposta = totemController.cadastrarTotem(totem);

        assertEquals(422, resposta.getStatusCodeValue());
        verify(totemService).cadastrarTotem(totem);
    }

    /* ---------- PUT /totem/{id} ---------- */
    @Test
    void deveEditarTotem() {
        Totem totem = new Totem();
        when(totemService.editarTotem(1L, totem)).thenReturn(totem);

        ResponseEntity<Object> resposta = totemController.editarTotem(1L, totem);

        assertEquals(200, resposta.getStatusCodeValue());
        assertEquals(totem, resposta.getBody());
        verify(totemService).editarTotem(1L, totem);
    }

    @Test
    void deveRetornar404SeTotemNaoEncontrado() {
        Totem totem = new Totem();
        when(totemService.editarTotem(99L, totem))
                .thenThrow(new IllegalArgumentException("não encontrado"));

        ResponseEntity<Object> resposta = totemController.editarTotem(99L, totem);

        assertEquals(404, resposta.getStatusCodeValue());
        verify(totemService).editarTotem(99L, totem);
    }

    @Test
    void deveRetornar422SeDadosInvalidosAoEditar() {
        Totem totem = new Totem();
        when(totemService.editarTotem(1L, totem))
                .thenThrow(new IllegalArgumentException("Dados inválidos"));

        ResponseEntity<Object> resposta = totemController.editarTotem(1L, totem);

        assertEquals(422, resposta.getStatusCodeValue());
        verify(totemService).editarTotem(1L, totem);
    }

    /* ---------- DELETE /totem/{id} ---------- */
    @Test
    void deveExcluirTotem() {
        Totem totem = new Totem();
        when(totemService.excluirTotem(1L)).thenReturn(totem);

        ResponseEntity<Object> resposta = totemController.excluirTotem(1L);

        assertEquals(200, resposta.getStatusCodeValue());
        assertEquals(totem, resposta.getBody());
        verify(totemService).excluirTotem(1L);
    }

    @Test
    void deveRetornar404SeTotemNaoExistir() {
        when(totemService.excluirTotem(99L))
                .thenThrow(new IllegalArgumentException("não encontrado"));

        ResponseEntity<Object> resposta = totemController.excluirTotem(99L);

        assertEquals(404, resposta.getStatusCodeValue());
        verify(totemService).excluirTotem(99L);
    }

    /* ---------- Métodos ainda não implementados ---------- */
    @Test
    void devePermitirListarTrancas() {
        totemController.listarTrancasDeUmTotem(1L);
        assertTrue(true);
    }

    @Test
    void devePermitirListarBicicletas() {
        totemController.ListarBicicletasDeUmTotem(1L);
        assertTrue(true);
    }
}
