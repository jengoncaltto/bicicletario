package com.bikeunirio.bicicletario.equipamento.controller;

import com.bikeunirio.bicicletario.equipamento.dto.BicicletaRedeDTO;
import com.bikeunirio.bicicletario.equipamento.dto.RetiradaBicicletaRequestDTO;
import com.bikeunirio.bicicletario.equipamento.entity.Bicicleta;
import com.bikeunirio.bicicletario.equipamento.enums.StatusBicicleta;
import com.bikeunirio.bicicletario.equipamento.service.BicicletaService;
import org.junit.jupiter.api.BeforeEach;
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

@ExtendWith(MockitoExtension.class)
class BicicletaControllerTest {

    @Mock
    private BicicletaService service;

    @InjectMocks
    private BicicletaController controller;

    private BicicletaRedeDTO dto;

    @BeforeEach
    void setup() {
        dto = new BicicletaRedeDTO();
    }

    /* ---------- listarBicicletas ---------- */
    @Test
    void deveRetornarStatus200EListaDeBicicletas() {
        Bicicleta bike = new Bicicleta();
        bike.setMarca("Caloi");
        bike.setModelo("Elite");
        bike.setAno("2023");
        bike.setNumero(123);
        bike.setStatus(StatusBicicleta.DISPONIVEL);
        when(service.listarBicicletas()).thenReturn(List.of(bike));

        ResponseEntity<List<Bicicleta>> resposta = controller.listarBicicletas();

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertNotNull(resposta.getBody());
        assertEquals(1, resposta.getBody().size());
        assertEquals("Caloi", resposta.getBody().get(0).getMarca());

        verify(service, times(1)).listarBicicletas();
    }

    @Test
    void deveRetornarListaVaziaComSucesso() {
        when(service.listarBicicletas()).thenReturn(Collections.emptyList());
        ResponseEntity<List<Bicicleta>> resposta = controller.listarBicicletas();

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        List<Bicicleta> bicicletas = resposta.getBody();
        assertNotNull(bicicletas);
        assertTrue(bicicletas.isEmpty());
    }

    /* ---------- cadastrarBicicleta ---------- */
    @Test
    void deveCadastrarBicicletaComSucesso() {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setMarca("Caloi");
        bicicleta.setModelo("Elite");
        when(service.cadastrarBicicleta(bicicleta)).thenReturn(bicicleta);

        ResponseEntity<Object> resposta = controller.cadastrarBicicleta(bicicleta);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals(bicicleta, resposta.getBody());
        verify(service, times(1)).cadastrarBicicleta(bicicleta);
    }

    @Test
    void deveRetornarErro422QuandoDadosInvalidos() {
        Bicicleta bicicleta = new Bicicleta(); // sem marca e modelo
        when(service.cadastrarBicicleta(bicicleta))
                .thenThrow(new IllegalArgumentException("Marca e modelo são obrigatórios."));

        ResponseEntity<Object> resposta = controller.cadastrarBicicleta(bicicleta);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, resposta.getStatusCode());
        verify(service, times(1)).cadastrarBicicleta(bicicleta);
    }

    @Test
    void deveRetornarErro422QuandoNumeroOuStatusForEnviadoNaCriacao() {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setNumero(100);
        bicicleta.setStatus(StatusBicicleta.NOVA);

        ResponseEntity<Object> resposta = controller.cadastrarBicicleta(bicicleta);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, resposta.getStatusCode());
        String corpo = resposta.getBody().toString();
        assertTrue(corpo.contains("DADOS INVALIDOS"));
        assertTrue(corpo.contains("Número e status não podem ser enviados na criação."));
    }

    @Test
    void deveRetornarErro422QuandoServiceLancarExcecaoDuranteCadastro() {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setMarca("Caloi");
        bicicleta.setModelo("Elite");

        when(service.cadastrarBicicleta(bicicleta))
                .thenThrow(new IllegalArgumentException("Dados inválidos ao cadastrar."));

        ResponseEntity<Object> resposta = controller.cadastrarBicicleta(bicicleta);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, resposta.getStatusCode());
        assertTrue(resposta.getBody().toString().contains("Dados inválidos ao cadastrar."));
    }

    /* ---------- retornarBicicleta ---------- */
    @Test
    void deveRetornarBicicletaPorIdComSucesso() {
        Bicicleta bike = new Bicicleta();
        bike.setMarca("Caloi");
        bike.setModelo("Elite");

        when(service.retornarBicicleta(1L)).thenReturn(bike);
        ResponseEntity<?> resposta = controller.retornarBicicleta(1L);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals(bike, resposta.getBody());
        verify(service).retornarBicicleta(1L);
    }

    @Test
    void deveRetornar404QuandoBicicletaNaoEncontrada() {
        when(service.retornarBicicleta(99L))
                .thenThrow(new IllegalArgumentException("Bicicleta não encontrada"));

        ResponseEntity<?> resposta = controller.retornarBicicleta(99L);

        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
        verify(service).retornarBicicleta(99L);
    }

    @Test
    void deveRetornarErro422QuandoDadosForemInvalidos() {
        Long idInvalido = -1L;
        when(service.retornarBicicleta(idInvalido))
                .thenThrow(new IllegalArgumentException("Número negativo não é aceito."));

        ResponseEntity<Object> resposta = controller.retornarBicicleta(idInvalido);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, resposta.getStatusCode());
        assertTrue(resposta.getBody().toString().contains("DADOS INVALIDOS"));
        assertTrue(resposta.getBody().toString().contains("Número negativo não é aceito"));
    }

    /* ---------- editarBicicleta ---------- */
    @Test
    void deveEditarBicicletaComSucesso() {
        Bicicleta atual = new Bicicleta();
        atual.setNumero(10);
        atual.setStatus(StatusBicicleta.DISPONIVEL);
        atual.setMarca("Caloi");
        atual.setModelo("City Tour");
        atual.setAno("2020");

        Bicicleta novosDados = new Bicicleta();
        novosDados.setMarca("Sense");
        novosDados.setModelo("Urban");
        novosDados.setAno("2024");

        when(service.retornarBicicleta(1L)).thenReturn(atual);

        Bicicleta atualizada = new Bicicleta();
        atualizada.setNumero(10);
        atualizada.setStatus(StatusBicicleta.DISPONIVEL);
        atualizada.setMarca("Sense");
        atualizada.setModelo("Urban");
        atualizada.setAno("2024");

        when(service.editarBicicleta(1L, novosDados)).thenReturn(atualizada);

        ResponseEntity<Object> resposta = controller.editarDadosBicicleta(1L, novosDados);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals(atualizada, resposta.getBody());
        verify(service).retornarBicicleta(1L);
        verify(service).editarBicicleta(1L, novosDados);
    }

    @Test
    void deveRetornarErro422QuandoTentarEditarNumeroDaBicicleta() {
        Bicicleta atual = new Bicicleta();
        atual.setNumero(10);
        atual.setStatus(StatusBicicleta.DISPONIVEL);
        when(service.retornarBicicleta(1L)).thenReturn(atual);

        Bicicleta nova = new Bicicleta();
        nova.setNumero(99);

        ResponseEntity<Object> resposta = controller.editarDadosBicicleta(1L, nova);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, resposta.getStatusCode());
        String body = resposta.getBody().toString();
        assertTrue(body.contains("O número da bicicleta não pode ser editado."));
    }

    @Test
    void deveRetornarErro422QuandoServiceLancarIllegalArgumentExceptionGenerica() {
        Bicicleta nova = new Bicicleta();
        when(service.retornarBicicleta(1L)).thenThrow(new IllegalArgumentException("Dados inválidos."));

        ResponseEntity<Object> resposta = controller.editarDadosBicicleta(1L, nova);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, resposta.getStatusCode());
        assertTrue(resposta.getBody().toString().contains("Dados inválidos."));
    }

    @Test
    void deveRetornarErro422QuandoTentarEditarStatusDaBicicleta() {
        Bicicleta atual = new Bicicleta();
        atual.setNumero(10);
        atual.setStatus(StatusBicicleta.DISPONIVEL);
        when(service.retornarBicicleta(1L)).thenReturn(atual);

        Bicicleta nova = new Bicicleta();
        nova.setStatus(StatusBicicleta.EM_REPARO);

        ResponseEntity<Object> resposta = controller.editarDadosBicicleta(1L, nova);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, resposta.getStatusCode());
        String body = resposta.getBody().toString();
        assertTrue(body.contains("O status da bicicleta não pode ser editado pelo UC10."));
    }

    /* ---------- removerBicicleta ---------- */
    @Test
    void deveRetornarStatus200QuandoRemoverComSucesso() {
        Bicicleta bike = new Bicicleta();
        bike.setMarca("Caloi");
        bike.setModelo("Elite");
        bike.setStatus(StatusBicicleta.DISPONIVEL);

        when(service.removerBicicleta(1L)).thenReturn(bike);

        ResponseEntity<Object> resposta = controller.removerBicicleta(1L);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals(bike, resposta.getBody());
        verify(service, times(1)).removerBicicleta(1L);
    }

    @Test
    void deveRetornarStatus404QuandoBicicletaNaoForEncontrada() {
        when(service.removerBicicleta(99L))
                .thenThrow(new IllegalArgumentException("NAO ENCONTRADO"));

        ResponseEntity<Object> resposta = controller.removerBicicleta(99L);

        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
        verify(service, times(1)).removerBicicleta(99L);
    }

    /* ---------- alterarStatusBicicleta ---------- */
    @Test
    void deveRetornar200QuandoStatusAlteradoComSucesso() {
        Bicicleta bike = new Bicicleta();
        bike.setStatus(StatusBicicleta.EM_REPARO);

        // O service ainda trabalha com enum
        when(service.alterarStatusBicicleta(1L, StatusBicicleta.EM_REPARO)).thenReturn(bike);

        // Passando string "EM_REPARO" para o controller
        ResponseEntity<Object> resposta = controller.alterarStatusBicicleta(1L, "EM_REPARO");

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals(bike, resposta.getBody());

        // Verifica se o service foi chamado com o enum correto
        verify(service).alterarStatusBicicleta(1L, StatusBicicleta.EM_REPARO);
    }

    @Test
    void deveRetornarErro404QuandoBicicletaNaoForEncontrada() {
        when(service.alterarStatusBicicleta(1L, StatusBicicleta.EM_REPARO))
                .thenThrow(new IllegalArgumentException("não encontrada"));

        // Passando string "EM_REPARO" para o controller
        ResponseEntity<Object> resposta = controller.alterarStatusBicicleta(1L, "EM_REPARO");

        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
        Map<String, Object> body = (Map<String, Object>) resposta.getBody();
        assertEquals("NAO ENCONTRADO", body.get("codigo"));
        assertTrue(((String) body.get("mensagem")).contains("não encontrada"));

        verify(service).alterarStatusBicicleta(1L, StatusBicicleta.EM_REPARO);
    }

    @Test
    void deveRetornarErro422QuandoStatusForInvalido() {
        // Não precisa mockar o service, o controller já lança IllegalArgumentException antes
        ResponseEntity<Object> resposta = controller.alterarStatusBicicleta(1L, "INVALIDO");

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, resposta.getStatusCode());
        Map<String, Object> body = (Map<String, Object>) resposta.getBody();
        assertEquals("DADOS INVALIDOS", body.get("codigo"));
        assertEquals("Status inválido: INVALIDO", body.get("mensagem"));

        // O service não deve ser chamado, porque a string é inválida
        verify(service, never()).alterarStatusBicicleta(anyLong(), any());
    }


    /* ----------- incluirBicicletaRedeTotens--------*/
    @Test
    void deveIncluirBicicletaNaRedeComSucesso() {
        BicicletaRedeDTO bicicletaRedeDTO = new BicicletaRedeDTO();
        bicicletaRedeDTO.setIdBicicleta(1L);
        bicicletaRedeDTO.setIdFuncionario(123L);
        bicicletaRedeDTO.setIdTranca(99l);

        String mensagem = "Bicicleta incluída com sucesso na rede de totens.";

        when(service.incluirBicicletaNaRede(1L, 99l, 123L)).thenReturn(mensagem);

        ResponseEntity<Object> resposta = controller.incluirBicicletaNaRede(bicicletaRedeDTO);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());

        Map<String, Object> body = (Map<String, Object>) resposta.getBody();
        assertEquals(mensagem, body.get("mensagem"));

        verify(service).incluirBicicletaNaRede(1L, 99l, 123L);
    }

    @Test
    void deveRetornarErro422QuandoFalharAoIncluirBicicletaNaRede() {
        BicicletaRedeDTO bicicletaRdDTO = new BicicletaRedeDTO();
        bicicletaRdDTO.setIdTranca(88L);
        bicicletaRdDTO.setIdBicicleta(2L);
        bicicletaRdDTO.setIdFuncionario(555L);

        when(service.incluirBicicletaNaRede(2L, 88L, 555L))
                .thenThrow(new IllegalArgumentException("Falha ao incluir bicicleta."));

        ResponseEntity<Object> resposta = controller.incluirBicicletaNaRede(bicicletaRdDTO);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, resposta.getStatusCode());

        Map<String, Object> body = (Map<String, Object>) resposta.getBody();
        assertEquals("Falha ao incluir bicicleta.", body.get("mensagem"));

        verify(service).incluirBicicletaNaRede(2L, 88L, 555L);
    }


    /*------------ retirarBicicletaRedeTotens -------*/
    @Test
    void deveRetirarBicicletaDaRedeComSucesso() {

        RetiradaBicicletaRequestDTO bicicleta = new RetiradaBicicletaRequestDTO();
        bicicleta.setIdTranca(10L);
        bicicleta.setIdFuncionario(1L);
        bicicleta.setStatusAcaoReparador("reparo");

        when(service.retirarBicicletaDaRede(10L, 1L, "reparo"))
                .thenReturn("Bicicleta retirada com sucesso.");

        ResponseEntity<Object> resposta = controller.retirarBicicletaDaRede(bicicleta);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals(
                "Bicicleta retirada com sucesso.",
                ((Map<?, ?>) resposta.getBody()).get("mensagem")
        );

        verify(service).retirarBicicletaDaRede(10L, 1L, "reparo");
    }


    @Test
    void deveRetornarErro422QuandoFalharAoRetirarBicicletaDaRede() {

        RetiradaBicicletaRequestDTO bicicleta = new RetiradaBicicletaRequestDTO();
        bicicleta.setIdTranca(20L);
        bicicleta.setIdFuncionario(2L);
        bicicleta.setStatusAcaoReparador("reparo");

        when(service.retirarBicicletaDaRede(20L, 2L, "reparo"))
                .thenThrow(new IllegalArgumentException("Dados inválidos."));

        ResponseEntity<Object> resposta = controller.retirarBicicletaDaRede(bicicleta);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, resposta.getStatusCode());
        assertEquals(
                "Dados inválidos.",
                ((Map<?, ?>) resposta.getBody()).get("mensagem")
        );

        verify(service).retirarBicicletaDaRede(20L, 2L, "reparo");
    }

    /*------------ */

}
