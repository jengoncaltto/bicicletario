package com.bikeunirio.bicicletario.equipamento.service;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


import com.bikeunirio.bicicletario.equipamento.entity.Bicicleta;
import com.bikeunirio.bicicletario.equipamento.enums.StatusBicicleta;
import com.bikeunirio.bicicletario.equipamento.repository.BicicletaRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BicicletaServiceTest {

    @Mock
    private BicicletaRepository repository;

    @InjectMocks
    private BicicletaService service;

    /* listarBicicletas */

    @Test
    void deveRetornarListaDeBicicletas() {
        // Arrange — cria uma bicicleta simulada
        Bicicleta bike = new Bicicleta();
        bike.setMarca("Caloi");
        bike.setModelo("Elite");

        // Quando o método findAll for chamado, retorna uma lista com essa bicicleta
        when(repository.findAll()).thenReturn(List.of(bike));

        // Act — chama o método que queremos testar
        List<Bicicleta> resultado = service.listarBicicletas();

        // Assert — verifica se o resultado é o esperado
        assertEquals(1, resultado.size());
        assertEquals("Caloi", resultado.get(0).getMarca());
        assertEquals("Elite", resultado.get(0).getModelo());

        // Verifica se o repositório foi realmente chamado uma vez
        verify(repository, times(1)).findAll();
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoExistiremBicicletas() {
        // Arrange — simula repositório retornando lista vazia
        when(repository.findAll()).thenReturn(List.of());

        // Act
        List<Bicicleta> resultado = service.listarBicicletas();

        // Assert
        assertTrue(resultado.isEmpty());
        verify(repository, times(1)).findAll();
    }

    /* cadastrarBicicleta */

    @Test
    void deveCadastrarBicicletaComSucesso() {
        // cria uma bicicleta válida
        Bicicleta bike = new Bicicleta();
        bike.setMarca("Caloi");
        bike.setModelo("Elite");

        // simula o comportamento do repository.save()
        when(repository.save(bike)).thenReturn(bike);

        // executa o método
        Bicicleta resultado = service.cadastrarBicicleta(bike);

        // verifica o retorno
        assertNotNull(resultado);
        assertEquals("Caloi", resultado.getMarca());
        verify(repository, times(1)).save(bike);
    }

    @Test
    void deveLancarErroQuandoMarcaOuModeloForemNulos() {
        // bicicleta sem marca e modelo
        Bicicleta bike = new Bicicleta();

        // espera que lance exceção
        IllegalArgumentException erro = assertThrows(IllegalArgumentException.class,
                () -> service.cadastrarBicicleta(bike));

        assertEquals("Marca e modelo são obrigatórios.", erro.getMessage());
        verify(repository, never()).save(any());
    }

    /* editarBicicleta*/

    @Test
    void deveEditarBicicletaComSucesso() {
        // prepara uma bicicleta que já existe no "banco"
        Bicicleta existente = new Bicicleta();
        existente.setMarca("Caloi");
        existente.setModelo("City Tour");
        existente.setAno("2020");
        existente.setNumero(123);
        existente.setStatus(StatusBicicleta.DISPONIVEL);

        // prepara os novos dados que serão enviados
        Bicicleta nova = new Bicicleta();
        nova.setMarca("Sense");
        nova.setModelo("Urban 2024");
        nova.setAno("2024");
        nova.setNumero(123);
        nova.setStatus(StatusBicicleta.EM_REPARO);

        // diz ao mock que vai encontrar a bicicleta
        when(repository.findById(1L)).thenReturn(Optional.of(existente));

        // simula o comportamento do save (salva e retorna o mesmo objeto)
        when(repository.save(any(Bicicleta.class))).thenReturn(existente);

        // executa o método que queremos testar
        Bicicleta atualizada = service.editarBicicleta(1L, nova);

        // verifica se os dados foram realmente atualizados
        assertEquals("Sense", atualizada.getMarca());
        assertEquals("Urban 2024", atualizada.getModelo());
        assertEquals("2024", atualizada.getAno());
        assertEquals(StatusBicicleta.EM_REPARO, atualizada.getStatus());

        // garante que o método save foi chamado
        verify(repository).save(existente);
    }

    @Test
    void lancarExcecaoQuandoBicicletaNaoEncontrada() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> service.editarBicicleta(99L, new Bicicleta()));

        assertTrue(e.getMessage().contains("não encontrada"));
        verify(repository, never()).save(any());
    }

    /* removerBicicleta*/
    @Test
    void deveRemoverBicicletaComSucesso() {
        Bicicleta bike = new Bicicleta();
        when(repository.findById(1L)).thenReturn(Optional.of(bike));

        Bicicleta removida = service.removerBicicleta(1L);

        assertEquals(bike, removida);
        verify(repository).findById(1L);
        verify(repository).delete(bike);
    }

    @Test
    void deveLancarErroQuandoBicicletaNaoExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.removerBicicleta(99L));

        verify(repository, never()).delete(any());
    }

    /* alterarStatusBicicleta */
    @Test
    void deveMudarStatusDaBicicletaComSucesso() {
        // Simula uma bicicleta que já existe no "banco"
        Bicicleta bike = new Bicicleta();
        bike.setStatus(StatusBicicleta.DISPONIVEL);

        // Quando buscar por ID 1, retorna a bicicleta
        when(repository.findById(1L)).thenReturn(Optional.of(bike));

        // Quando salvar, retorna a mesma bicicleta
        when(repository.save(bike)).thenReturn(bike);

        // Chama o método do service
        Bicicleta resultado = service.alterarStatusBicicleta(1L, "EM_REPARO");

        // Verifica se o status mudou corretamente
        assertEquals(StatusBicicleta.EM_REPARO, resultado.getStatus());
    }

    @Test
    void deveDarErroQuandoBicicletaNaoForEncontrada() {
        // Quando o ID não existe, retorna vazio
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // Espera que o método lance erro
        assertThrows(IllegalArgumentException.class,
                () -> service.alterarStatusBicicleta(99L, "DISPONIVEL"));
    }

    @Test
    void deveDarErroQuandoStatusForInvalido() {
        // Simula uma bicicleta encontrada
        Bicicleta bike = new Bicicleta();
        when(repository.findById(1L)).thenReturn(Optional.of(bike));

        // Chama o método com um status que não existe no enum
        assertThrows(IllegalArgumentException.class,
                () -> service.alterarStatusBicicleta(1L, "QUEBRADA"));
    }

}
