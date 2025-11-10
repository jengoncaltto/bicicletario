package com.bikeunirio.bicicletario.equipamento.controller;

import com.bikeunirio.bicicletario.equipamento.entity.Bicicleta;
import com.bikeunirio.bicicletario.equipamento.enums.StatusBicicleta;
import com.bikeunirio.bicicletario.equipamento.service.BicicletaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BicicletaControllerTest {

    @Mock
    private BicicletaService service;

    @InjectMocks
    private BicicletaController controller;

    /* ---------- listarBicicletas ---------- */

    @Test
    void deveRetornarStatus200EListaDeBicicletas() {
        // Arrange — cria uma lista simulada de bicicletas
        Bicicleta bike = new Bicicleta();
        bike.setMarca("Caloi");
        bike.setModelo("Elite");
        bike.setAno("2023");
        bike.setNumero(123);
        bike.setStatus(StatusBicicleta.DISPONIVEL);

        when(service.listarBicicletas()).thenReturn(List.of(bike));

        // Act — chama o método do controller diretamente
        ResponseEntity<List<Bicicleta>> resposta = controller.listarBicicletas();

        // Assert — verifica se o resultado está correto
        assertEquals(200, resposta.getStatusCodeValue()); // status HTTP 200
        assertNotNull(resposta.getBody());                // corpo não é nulo
        assertEquals(1, resposta.getBody().size());       // 1 bicicleta na lista
        assertEquals("Caloi", resposta.getBody().get(0).getMarca()); // campo correto

        // Verifica se o service foi chamado exatamente uma vez
        verify(service, times(1)).listarBicicletas();
    }

    @Test
    void deveRetornarListaVaziaComSucesso() {
        // Simula que o service não encontrou nenhuma bicicleta
        when(service.listarBicicletas()).thenReturn(Collections.emptyList());

        // Chama o método do controller diretamente
        ResponseEntity<List<Bicicleta>> resposta = controller.listarBicicletas();

        // Verifica se o status retornado é 200 (OK)
        assertEquals(200, resposta.getStatusCodeValue());

        // Verifica se o corpo da resposta é uma lista vazia
        List<Bicicleta> bicicletas = resposta.getBody();
        assertNotNull(bicicletas);
        assertTrue(bicicletas.isEmpty());
    }

    /* ---------- cadastrarBicicleta ---------- */
    @Test
    void deveCadastrarBicicletaComSucesso() {
        // Arrange – cria uma bicicleta simulada
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setMarca("Caloi");
        bicicleta.setModelo("Elite");

        when(service.cadastrarBicicleta(bicicleta)).thenReturn(bicicleta);

        // Act – chama o método do controller diretamente
        ResponseEntity<Object> resposta = controller.cadastrarBicicleta(bicicleta);

        // Assert – verifica se o status e o corpo estão corretos
        assertEquals(200, resposta.getStatusCodeValue());
        assertEquals(bicicleta, resposta.getBody());
        verify(service, times(1)).cadastrarBicicleta(bicicleta);
    }

    @Test
    void deveRetornarErro422QuandoDadosInvalidos() {
        // Arrange – simula exceção lançada pelo service
        Bicicleta bicicleta = new Bicicleta(); // sem marca e modelo
        when(service.cadastrarBicicleta(bicicleta))
                .thenThrow(new IllegalArgumentException("Marca e modelo são obrigatórios."));

        // Act
        ResponseEntity<Object> resposta = controller.cadastrarBicicleta(bicicleta);

        // Assert
        assertEquals(422, resposta.getStatusCodeValue());
        verify(service, times(1)).cadastrarBicicleta(bicicleta);
    }

    /* ---------- retornarBicicleta ---------- */

    @Test
    void deveRetornarBicicletaPorIdComSucesso() {
        Bicicleta bike = new Bicicleta();
        bike.setMarca("Caloi");
        bike.setModelo("Elite");

        when(service.retornarBicicleta(1L)).thenReturn(bike);

        ResponseEntity<?> resposta = controller.retornarBicicleta(1L);

        assertEquals(200, resposta.getStatusCodeValue());
        assertEquals(bike, resposta.getBody());
        verify(service).retornarBicicleta(1L);
    }

    @Test
    void deveRetornar404QuandoBicicletaNaoEncontrada() {
        when(service.retornarBicicleta(99L))
                .thenThrow(new IllegalArgumentException("Bicicleta não encontrada"));

        ResponseEntity<?> resposta = controller.retornarBicicleta(99L);

        assertEquals(404, resposta.getStatusCodeValue());
        verify(service).retornarBicicleta(99L);
    }

    /* ---------- editarBicicleta ---------- */

    @Test
    void deveEditarBicicletaComSucesso() {}


    /* ---------- removerBicicleta ---------- */
    @Test
    void deveRetornarStatus200QuandoRemoverComSucesso() {
        // Arrange
        Bicicleta bike = new Bicicleta();
        bike.setMarca("Caloi");
        bike.setModelo("Elite");
        bike.setStatus(StatusBicicleta.DISPONIVEL);

        when(service.removerBicicleta(1L)).thenReturn(bike);

        // Act
        ResponseEntity<Object> resposta = controller.removerBicicleta(1L);

        // Assert
        assertEquals(200, resposta.getStatusCodeValue());
        assertEquals(bike, resposta.getBody());
        verify(service, times(1)).removerBicicleta(1L);
    }

    @Test
    void deveRetornarStatus404QuandoBicicletaNaoForEncontrada() {
        // Arrange
        when(service.removerBicicleta(99L))
                .thenThrow(new IllegalArgumentException("Bicicleta não encontrada"));

        // Act
        ResponseEntity<Object> resposta = controller.removerBicicleta(99L);

        // Assert
        assertEquals(404, resposta.getStatusCodeValue());
        verify(service, times(1)).removerBicicleta(99L);
    }

    /* ---------- alterarStatusBicicleta ---------- */
    @Test
    void deveRetornar200QuandoStatusAlteradoComSucesso() {
        // Simula uma bicicleta com status atualizado
        Bicicleta bike = new Bicicleta();
        bike.setStatus(StatusBicicleta.EM_REPARO);

        // Quando o service for chamado, retorna a bicicleta
        when(service.alterarStatusBicicleta(1L, "EM_REPARO")).thenReturn(bike);

        // Chama o método do controller
        ResponseEntity<Object> resposta = controller.alterarStatusBicicleta(1L, "EM_REPARO");

        // Verifica o resultado
        assertEquals(200, resposta.getStatusCodeValue());
        assertEquals(bike, resposta.getBody());
    }

    @Test
    void deveRetornar404QuandoBicicletaNaoForEncontrada() {
        // Quando o service lançar erro de "não encontrada"
        when(service.alterarStatusBicicleta(99L, "DISPONIVEL"))
                .thenThrow(new IllegalArgumentException("Bicicleta não encontrada"));

        // Chama o método do controller
        ResponseEntity<Object> resposta = controller.alterarStatusBicicleta(99L, "DISPONIVEL");

        // Verifica se retornou o status 404
        assertEquals(404, resposta.getStatusCodeValue());
    }

    @Test
    void deveRetornar422QuandoStatusForInvalido() {
        // Quando o service lançar erro de status inválido
        when(service.alterarStatusBicicleta(1L, "QUEBRADA"))
                .thenThrow(new IllegalArgumentException("Status inválido: QUEBRADA"));

        // Chama o método do controller
        ResponseEntity<Object> resposta = controller.alterarStatusBicicleta(1L, "QUEBRADA");

        // Verifica se retornou o status 422
        assertEquals(422, resposta.getStatusCodeValue());
    }


}
