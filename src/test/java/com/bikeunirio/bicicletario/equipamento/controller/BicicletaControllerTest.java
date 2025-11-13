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
import java.util.Map;

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
    @Test
    void deveRetornarErro422QuandoNumeroOuStatusForEnviadoNaCriacao() {
        // Arrange
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setNumero(100); // número não deve ser enviado
        bicicleta.setStatus(StatusBicicleta.NOVA); // status também não deve ser enviado

        // Act
        ResponseEntity<Object> resposta = controller.cadastrarBicicleta(bicicleta);

        // Assert
        assertEquals(422, resposta.getStatusCodeValue());
        String corpo = resposta.getBody().toString();
        assertTrue(corpo.contains("DADOS INVALIDOS"));
        assertTrue(corpo.contains("Número e status não podem ser enviados na criação."));
    }
    @Test
    void deveRetornarErro422QuandoServiceLancarExcecaoDuranteCadastro() {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setMarca("Caloi");
        bicicleta.setModelo("Elite");
        // não define número nem status, para não cair no if

        when(service.cadastrarBicicleta(bicicleta))
                .thenThrow(new IllegalArgumentException("Dados inválidos ao cadastrar."));

        ResponseEntity<Object> resposta = controller.cadastrarBicicleta(bicicleta);

        assertEquals(422, resposta.getStatusCodeValue());
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
    @Test
    void deveRetornarErro422QuandoDadosForemInvalidos() {
        // Arrange
        Long idInvalido = -1L;
        when(service.retornarBicicleta(idInvalido))
                .thenThrow(new IllegalArgumentException("Número negativo não é aceito."));

        // Act
        ResponseEntity<Object> resposta = controller.retornarBicicleta(idInvalido);

        // Assert
        assertEquals(422, resposta.getStatusCodeValue());
        assertTrue(resposta.getBody().toString().contains("DADOS INVALIDOS"));
        assertTrue(resposta.getBody().toString().contains("Número negativo não é aceito"));
    }


    /* ---------- editarBicicleta ---------- */

    @Test
    void deveEditarBicicletaComSucesso() {
        // representa a bicicleta que já está cadastrada no sistema.
        Bicicleta atual = new Bicicleta();
        atual.setNumero(10);
        atual.setStatus(StatusBicicleta.DISPONIVEL);
        atual.setMarca("Caloi");
        atual.setModelo("City Tour");
        atual.setAno("2020");

        // simula o JSON que o usuário envia para o endpoint PUT.
        Bicicleta novosDados = new Bicicleta();
        novosDados.setMarca("Sense");
        novosDados.setModelo("Urban");
        novosDados.setAno("2024");

        // Quando o controller chamar service.retornarBicicleta(1L), devolva essa bicicleta atual;
        // é ele que o controller usa para comparar número e status.
        when(service.retornarBicicleta(1L)).thenReturn(atual);

        // Essa é a bicicleta que será retornada após a edição — já com os dados atualizados.
        Bicicleta atualizada = new Bicicleta();
        atualizada.setNumero(10);
        atualizada.setStatus(StatusBicicleta.DISPONIVEL);
        atualizada.setMarca("Sense");
        atualizada.setModelo("Urban");
        atualizada.setAno("2024");

        // Quando o controller pedir para editar a bicicleta 1 com esses dados, devolva a bicicleta atualizada.
        when(service.editarBicicleta(1L, novosDados)).thenReturn(atualizada);

        // Act
        ResponseEntity<Object> resposta = controller.editarDadosBicicleta(1L, novosDados);

        // Assert
        assertEquals(200, resposta.getStatusCodeValue());  //Edição válida deve retornar OK.
        assertEquals(atualizada, resposta.getBody());              // Corpo da resposta deve ser a bicicleta atualizada

        // Confirma que o controller chamou o método certo
        verify(service).retornarBicicleta(1L);
        verify(service).editarBicicleta(1L, novosDados);
    }
    @Test
    void deveRetornarErro422QuandoTentarEditarNumeroDaBicicleta() {
        // Arrange
        Bicicleta atual = new Bicicleta();
        atual.setNumero(10);
        atual.setStatus(com.bikeunirio.bicicletario.equipamento.enums.StatusBicicleta.DISPONIVEL);

        when(service.retornarBicicleta(1L)).thenReturn(atual);

        // bicicleta enviada com número diferente
        Bicicleta nova = new Bicicleta();
        nova.setNumero(99);

        // Act
        ResponseEntity<Object> resposta = controller.editarDadosBicicleta(1L, nova);

        // Assert
        assertEquals(422, resposta.getStatusCodeValue());
        String body = resposta.getBody().toString();
        assertTrue(body.contains("O número da bicicleta não pode ser editado."));
    }
    @Test
    void deveRetornarErro422QuandoServiceLancarIllegalArgumentExceptionGenerica() {
        // Arrange
        Bicicleta nova = new Bicicleta();
        when(service.retornarBicicleta(1L)).thenThrow(new IllegalArgumentException("Dados inválidos."));

        // Act
        ResponseEntity<Object> resposta = controller.editarDadosBicicleta(1L, nova);

        // Assert
        assertEquals(422, resposta.getStatusCodeValue());
        assertTrue(resposta.getBody().toString().contains("Dados inválidos."));
    }
    @Test
    void deveRetornarErro422QuandoTentarEditarStatusDaBicicleta() {
        // Arrange
        Bicicleta atual = new Bicicleta();
        atual.setNumero(10);
        atual.setStatus(com.bikeunirio.bicicletario.equipamento.enums.StatusBicicleta.DISPONIVEL);

        when(service.retornarBicicleta(1L)).thenReturn(atual);

        // bicicleta enviada com status diferente
        Bicicleta nova = new Bicicleta();
        nova.setStatus(com.bikeunirio.bicicletario.equipamento.enums.StatusBicicleta.EM_REPARO);

        // Act
        ResponseEntity<Object> resposta = controller.editarDadosBicicleta(1L, nova);

        // Assert
        assertEquals(422, resposta.getStatusCodeValue());
        String body = resposta.getBody().toString();
        assertTrue(body.contains("O status da bicicleta não pode ser editado pelo UC10."));
    }


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
                .thenThrow(new IllegalArgumentException("NAO ENCONTRADO"));

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
        when(service.alterarStatusBicicleta(1L, StatusBicicleta.EM_REPARO)).thenReturn(bike);

        // Chama o método do controller
        ResponseEntity<Object> resposta = controller.alterarStatusBicicleta(1L, StatusBicicleta.EM_REPARO);

        // Verifica o resultado
        assertEquals(200, resposta.getStatusCodeValue());
        assertEquals(bike, resposta.getBody());
    }
    @Test
    void deveRetornarErro404QuandoBicicletaNaoForEncontrada() {
        // Arrange
        when(service.alterarStatusBicicleta(1L, StatusBicicleta.EM_REPARO))
                .thenThrow(new IllegalArgumentException("não encontrada"));

        // Act
        ResponseEntity<Object> resposta = controller.alterarStatusBicicleta(1L, StatusBicicleta.EM_REPARO);

        // Assert
        assertEquals(404, resposta.getStatusCodeValue());
        Map<String, Object> body = (Map<String, Object>) resposta.getBody();
        assertEquals("NAO ENCONTRADO", body.get("codigo"));
        assertTrue(((String) body.get("mensagem")).contains("não encontrada"));

        verify(service).alterarStatusBicicleta(1L, StatusBicicleta.EM_REPARO);
    }

    /* ---------- Teste 422 ---------- */
    @Test
    void deveRetornarErro422QuandoStatusForInvalido() {
        // Arrange
        when(service.alterarStatusBicicleta(1L, StatusBicicleta.DISPONIVEL))
                .thenThrow(new IllegalArgumentException("Status inválido para alteração."));

        // Act
        ResponseEntity<Object> resposta = controller.alterarStatusBicicleta(1L, StatusBicicleta.DISPONIVEL);

        // Assert
        assertEquals(422, resposta.getStatusCodeValue());
        Map<String, Object> body = (Map<String, Object>) resposta.getBody();
        assertEquals("DADOS INVALIDOS", body.get("codigo"));
        assertEquals("Status inválido para alteração.", body.get("mensagem"));

        verify(service).alterarStatusBicicleta(1L, StatusBicicleta.DISPONIVEL);
    }

    /* -----------incluirBicicletaRedeTotens--------*/

    @Test
    void deveIncluirBicicletaNaRedeComSucesso() {
        // Arrange
        BicicletaController.BicicletaRedeDTO dto = new BicicletaController.BicicletaRedeDTO();
        dto.setIdBicicleta(1L);

        when(service.incluirBicicletaNaRede(1L))
                .thenReturn("Bicicleta incluída com sucesso na rede de totens.");

        // Act
        ResponseEntity<Object> resposta = controller.incluirBicicletaNaRede(dto);

        // Assert
        assertEquals(200, resposta.getStatusCodeValue());
        Map<String, Object> body = (Map<String, Object>) resposta.getBody();
        assertEquals("Bicicleta incluída com sucesso na rede de totens.", body.get("mensagem"));

        verify(service).incluirBicicletaNaRede(1L);
    }

    @Test
    void deveRetornarErro422QuandoFalharAoIncluirBicicletaNaRede() {
        // Arrange
        BicicletaController.BicicletaRedeDTO dto = new BicicletaController.BicicletaRedeDTO();
        dto.setIdBicicleta(2L);

        when(service.incluirBicicletaNaRede(2L))
                .thenThrow(new IllegalArgumentException("Falha ao incluir bicicleta."));

        // Act
        ResponseEntity<Object> resposta = controller.incluirBicicletaNaRede(dto);

        // Assert
        assertEquals(422, resposta.getStatusCodeValue());
        Map<String, Object> body = (Map<String, Object>) resposta.getBody();
        assertEquals("Falha ao incluir bicicleta.", body.get("erro"));

        verify(service).incluirBicicletaNaRede(2L);
    }


}
