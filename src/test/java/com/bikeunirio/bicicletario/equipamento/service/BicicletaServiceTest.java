package com.bikeunirio.bicicletario.equipamento.service;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


import com.bikeunirio.bicicletario.equipamento.entity.Bicicleta;
import com.bikeunirio.bicicletario.equipamento.entity.Tranca;
import com.bikeunirio.bicicletario.equipamento.enums.StatusBicicleta;
import com.bikeunirio.bicicletario.equipamento.enums.StatusTranca;
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

    @Mock
    private EmailService emailService;

    @InjectMocks
    private BicicletaService service;

    /* listarBicicletas */

    @Test
    void deveRetornarListaDeBicicletas() {
        // representa a bicicleta que já está cadastrada no sistema.
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
        // Arrange
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setMarca("Caloi");
        bicicleta.setModelo("Elite");
        bicicleta.setAno("2024");

        Bicicleta salva = new Bicicleta();
        salva.setMarca("Caloi");
        salva.setModelo("Elite");
        salva.setAno("2024");
        salva.setNumero(1);
        salva.setStatus(StatusBicicleta.NOVA);

        when(repository.findMaxNumero()).thenReturn(null);
        when(repository.save(any(Bicicleta.class))).thenReturn(salva);

        // Act
        Bicicleta resultado = service.cadastrarBicicleta(bicicleta);

        // Assert
        assertEquals(StatusBicicleta.NOVA, resultado.getStatus());
        assertNotNull(resultado.getNumero());
        verify(repository).save(any(Bicicleta.class));
    }

    @Test
    void deveLancarErroQuandoMarcaForNula() {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setModelo("Elite");
        bicicleta.setAno("2024");

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.cadastrarBicicleta(bicicleta)
        );

        assertEquals("Marca é obrigatória.", ex.getMessage());
    }

    @Test
    void deveLancarErroQuandoModeloForVazio() {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setMarca("Caloi");
        bicicleta.setModelo(" ");
        bicicleta.setAno("2024");

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.cadastrarBicicleta(bicicleta)
        );

        assertEquals("Modelo é obrigatório.", ex.getMessage());
    }

    @Test
    void deveLancarErroQuandoAnoForNulo() {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setMarca("Caloi");
        bicicleta.setModelo("Elite");

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.cadastrarBicicleta(bicicleta)
        );

        assertEquals("Ano é obrigatório.", ex.getMessage());
    }

    /* retornarBicicleta */

    @Test
    void deveRetornarBicicletaQuandoIdForValido() {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setMarca("Caloi");
        when(repository.findById(1L)).thenReturn(Optional.of(bicicleta));

        Bicicleta resultado = service.retornarBicicleta(1L);

        assertEquals("Caloi", resultado.getMarca());
        verify(repository).findById(1L);
    }

    @Test
    void deveLancarErroQuandoIdForNulo() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.retornarBicicleta(null)
        );

        assertEquals("Um número é obrigatório.", ex.getMessage());
        verify(repository, never()).findById(any());
    }

    @Test
    void deveLancarErroQuandoIdForNegativo() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.retornarBicicleta(-5L)
        );

        assertEquals("Número negativo não é aceito.", ex.getMessage());
        verify(repository, never()).findById(any());
    }

    @Test
    void deveLancarErroQuandoBicicletaNaoForEncontrada() {
        when(repository.findById(10L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.retornarBicicleta(10L)
        );

        assertEquals("não encontrada.", ex.getMessage());
        verify(repository).findById(10L);
    }

    /* editarBicicleta*/

    @Test
    void deveEditarBicicletaComSucesso() {
        // simula uma bicicleta existente no banco
        Bicicleta existente = new Bicicleta();
        existente.setMarca("Caloi");
        existente.setModelo("City");
        existente.setAno("2020");
        existente.setNumero(10);
        existente.setStatus(StatusBicicleta.DISPONIVEL);

        // novos dados enviados pelo usuário
        Bicicleta novos = new Bicicleta();
        novos.setMarca("Sense");
        novos.setModelo("Urban");
        novos.setAno("2024");

        // mockando findById
        when(repository.findById(1L)).thenReturn(Optional.of(existente));

        // mockando save — devolve o próprio objeto
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Bicicleta atualizado = service.editarBicicleta(1L, novos);

        // Assert
        // campos permitidos
        assertEquals("Sense", atualizado.getMarca());
        assertEquals("Urban", atualizado.getModelo());
        assertEquals("2024", atualizado.getAno());

        // campos proibidos
        assertEquals(10, atualizado.getNumero());
        assertEquals(StatusBicicleta.DISPONIVEL, atualizado.getStatus());

        // verifica se o repository foi usado
        verify(repository).findById(1L);
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
        // Arrange
        Bicicleta bike = new Bicicleta();
        bike.setStatus(StatusBicicleta.APOSENTADA); // regra R4
        bike.setTranca(null);                       // não pode estar presa

        when(repository.findById(1L)).thenReturn(Optional.of(bike));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Bicicleta removida = service.removerBicicleta(1L);

        // Assert
        assertEquals(bike, removida);
        assertEquals(StatusBicicleta.EXCLUIDA, removida.getStatus()); // status final correto

        verify(repository).findById(1L);
        verify(repository).save(bike); //  obrigatório no UC10
    }

    @Test
    void deveLancarErroQuandoBicicletaNaoExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.removerBicicleta(99L));

        verify(repository, never()).delete(any());
    }

    @Test
    void deveLancarExcecaoSeBicicletaNaoEncontrada() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.removerBicicleta(1L));
    }

    @Test
    void deveLancarExcecaoSeBicicletaNaoAposentada() {
        Bicicleta bike = new Bicicleta();
        bike.setStatus(StatusBicicleta.EM_USO);
        when(repository.findById(anyLong())).thenReturn(Optional.of(bike));
        assertThrows(IllegalArgumentException.class, () -> service.removerBicicleta(1L));
    }

    @Test
    void deveLancarExcecaoSeBicicletaPossuirTranca() {
        Bicicleta bike = new Bicicleta();
        bike.setStatus(StatusBicicleta.APOSENTADA);
        bike.setTranca(new Tranca());
        when(repository.findById(anyLong())).thenReturn(Optional.of(bike));
        assertThrows(IllegalArgumentException.class, () -> service.removerBicicleta(1L));
    }

    /* alterarStatusBicicleta*/
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
        Bicicleta resultado = service.alterarStatusBicicleta(1L, StatusBicicleta.EM_REPARO);

        // Verifica se o status mudou corretamente
        assertEquals(StatusBicicleta.EM_REPARO, resultado.getStatus());
    }

    @Test
    void deveDarErroQuandoBicicletaNaoForEncontrada() {
        // Quando o ID não existe, retorna vazio
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // Espera que o método lance erro
        assertThrows(IllegalArgumentException.class,
                () -> service.alterarStatusBicicleta(99L, StatusBicicleta.DISPONIVEL));
    }


    /* ---------- UC08 - Fluxo Principal ---------- */
    @Test
    void deveIncluirBicicletaNovaNaRedeComSucesso() {
        // bicicleta do banco
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setNumero(101);
        bicicleta.setStatus(StatusBicicleta.NOVA);

        Tranca tranca = new Tranca();
        tranca.setStatus(StatusTranca.LIVRE);
        bicicleta.setTranca(tranca);

        when(repository.findById(1L)).thenReturn(Optional.of(bicicleta));

        // ✅mock do envio de e-mail antes da execução
        doNothing().when(emailService).enviarEmail(anyString(), anyString(), anyString());

        // Act
        String resultado = service.incluirBicicletaNaRede(1L);

        // Assert
        assertEquals("Bicicleta incluída com sucesso na rede de totens.", resultado);
        assertEquals(StatusBicicleta.DISPONIVEL, bicicleta.getStatus());
        assertEquals(StatusTranca.OCUPADA, tranca.getStatus());
        assertNotNull(bicicleta.getDataInsercao());

        verify(repository).save(bicicleta);
    }

    @Test
    void deveLancarExcecaoSeBicicletaNaoTemTranca() {
        Bicicleta bike = new Bicicleta();
        bike.setTranca(null);

        when(repository.findById(1L)).thenReturn(Optional.of(bike));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.incluirBicicletaNaRede(1L));

        assertEquals("Bicicleta não está associada a nenhuma tranca.", ex.getMessage());
    }

    @Test
    void deveLancarExcecaoSeBicicletaEmUso() {
        Bicicleta bike = new Bicicleta();
        bike.setTranca(new Tranca());
        bike.setStatus(StatusBicicleta.EM_USO);

        when(repository.findById(1L)).thenReturn(Optional.of(bike));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.incluirBicicletaNaRede(1L));

        assertEquals("A bicicleta está em uso e não pode ser incluída na rede.", ex.getMessage());
    }

    @Test
    void deveLancarExcecaoSeStatusInvalido() {
        Bicicleta bike = new Bicicleta();
        bike.setTranca(new Tranca());
        bike.setStatus(StatusBicicleta.APOSENTADA);

        when(repository.findById(1L)).thenReturn(Optional.of(bike));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.incluirBicicletaNaRede(1L));

        assertEquals("Bicicleta não está apta para inclusão (deve ser NOVA ou EM_REPARO).", ex.getMessage());
    }

    @Test
    void deveLancarExcecaoSeTrancaOcupada() {
        Tranca tranca = new Tranca();
        tranca.setStatus(StatusTranca.OCUPADA);

        Bicicleta bike = new Bicicleta();
        bike.setTranca(tranca);
        bike.setStatus(StatusBicicleta.NOVA); // status válido para passar até a verificação da tranca

        when(repository.findById(1L)).thenReturn(Optional.of(bike));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.incluirBicicletaNaRede(1L));

        assertEquals("A tranca selecionada não está disponível.", ex.getMessage());
    }



    /* ---------- E1: Número da bicicleta inválido ---------- */
    @Test
    void deveLancarErroQuandoBicicletaNaoExistir() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.incluirBicicletaNaRede(99L));

        assertTrue(ex.getMessage().contains("não encontrada"));
    }

    /* ---------- E3: Bicicleta em uso ---------- */
    @Test
    void deveLancarErroQuandoBicicletaEstiverEmUso() {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setStatus(StatusBicicleta.EM_USO);

        Tranca tranca = new Tranca();
        tranca.setStatus(StatusTranca.LIVRE);
        bicicleta.setTranca(tranca); // Adiciona a tranca

        when(repository.findById(1L)).thenReturn(Optional.of(bicicleta));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.incluirBicicletaNaRede(1L));

        assertEquals("A bicicleta está em uso e não pode ser incluída na rede.", ex.getMessage());
    }

}
