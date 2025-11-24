package com.bikeunirio.bicicletario.equipamento.service;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


import com.bikeunirio.bicicletario.equipamento.entity.Bicicleta;
import com.bikeunirio.bicicletario.equipamento.entity.Tranca;
import com.bikeunirio.bicicletario.equipamento.enums.StatusBicicleta;
import com.bikeunirio.bicicletario.equipamento.enums.StatusTranca;
import com.bikeunirio.bicicletario.equipamento.repository.BicicletaRepository;
import com.bikeunirio.bicicletario.equipamento.repository.TrancaRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BicicletaServiceTest {


    @Mock
    private BicicletaRepository bicicletaRepository;

    @Mock
    private TrancaRepository trancaRepository;

    @Mock
    private TrancaService trancaService;

    @Mock
    private EmailService emailService;

    @Spy
    @InjectMocks
    private BicicletaService bicicletaService;

    /* listarBicicletas */

    @Test
    void deveRetornarListaDeBicicletas() {
        // representa a bicicleta que já está cadastrada no sistema.
        Bicicleta bike = new Bicicleta();
        bike.setMarca("Caloi");
        bike.setModelo("Elite");

        // Quando o método findAll for chamado, retorna uma lista com essa bicicleta
        when(bicicletaRepository.findAll()).thenReturn(List.of(bike));

        // Act — chama o método que queremos testar
        List<Bicicleta> resultado = bicicletaService.listarBicicletas();

        // Assert — verifica se o resultado é o esperado
        assertEquals(1, resultado.size());
        assertEquals("Caloi", resultado.get(0).getMarca());
        assertEquals("Elite", resultado.get(0).getModelo());

        // Verifica se o repositório foi realmente chamado uma vez
        verify(bicicletaRepository, times(1)).findAll();
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoExistiremBicicletas() {
        // Arrange — simula repositório retornando lista vazia
        when(bicicletaRepository.findAll()).thenReturn(List.of());

        // Act
        List<Bicicleta> resultado = bicicletaService.listarBicicletas();

        // Assert
        assertTrue(resultado.isEmpty());
        verify(bicicletaRepository, times(1)).findAll();
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

        when(bicicletaRepository.findMaxNumero()).thenReturn(null);
        when(bicicletaRepository.save(any(Bicicleta.class))).thenReturn(salva);

        // Act
        Bicicleta resultado = bicicletaService.cadastrarBicicleta(bicicleta);

        // Assert
        assertEquals(StatusBicicleta.NOVA, resultado.getStatus());
        assertNotNull(resultado.getNumero());
        verify(bicicletaRepository).save(any(Bicicleta.class));
    }

    @Test
    void deveLancarErroQuandoMarcaForNula() {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setModelo("Elite");
        bicicleta.setAno("2024");

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> bicicletaService.cadastrarBicicleta(bicicleta)
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
                () -> bicicletaService.cadastrarBicicleta(bicicleta)
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
                () -> bicicletaService.cadastrarBicicleta(bicicleta)
        );

        assertEquals("Ano é obrigatório.", ex.getMessage());
    }

    /* retornarBicicleta */

    @Test
    void deveRetornarBicicletaQuandoIdForValido() {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setMarca("Caloi");
        when(bicicletaRepository.findById(1L)).thenReturn(Optional.of(bicicleta));

        Bicicleta resultado = bicicletaService.retornarBicicleta(1L);

        assertEquals("Caloi", resultado.getMarca());
        verify(bicicletaRepository).findById(1L);
    }

    @Test
    void deveLancarErroQuandoIdForNulo() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> bicicletaService.retornarBicicleta(null)
        );

        assertEquals("Um número é obrigatório.", ex.getMessage());
        verify(bicicletaRepository, never()).findById(any());
    }

    @Test
    void deveLancarErroQuandoIdForNegativo() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> bicicletaService.retornarBicicleta(-5L)
        );

        assertEquals("Número negativo não é aceito.", ex.getMessage());
        verify(bicicletaRepository, never()).findById(any());
    }

    @Test
    void deveLancarErroQuandoBicicletaNaoForEncontrada() {
        when(bicicletaRepository.findById(10L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> bicicletaService.retornarBicicleta(10L)
        );

        assertEquals("não encontrada.", ex.getMessage());
        verify(bicicletaRepository).findById(10L);
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
        when(bicicletaRepository.findById(1L)).thenReturn(Optional.of(existente));

        // mockando save — devolve o próprio objeto
        when(bicicletaRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Bicicleta atualizado = bicicletaService.editarBicicleta(1L, novos);

        // Assert
        // campos permitidos
        assertEquals("Sense", atualizado.getMarca());
        assertEquals("Urban", atualizado.getModelo());
        assertEquals("2024", atualizado.getAno());

        // campos proibidos
        assertEquals(10, atualizado.getNumero());
        assertEquals(StatusBicicleta.DISPONIVEL, atualizado.getStatus());

        // verifica se o repository foi usado
        verify(bicicletaRepository).findById(1L);
        verify(bicicletaRepository).save(existente);
    }

    @Test
    void lancarExcecaoQuandoBicicletaNaoEncontrada() {
        when(bicicletaRepository.findById(99L)).thenReturn(Optional.empty());

        Bicicleta bicicleta = new Bicicleta(); // criar fora do lambda

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> bicicletaService.editarBicicleta(99L, bicicleta));

        assertTrue(e.getMessage().contains("não encontrada"));
        verify(bicicletaRepository, never()).save(any());
    }


    /* removerBicicleta*/
    @Test
    void deveRemoverBicicletaComSucesso() {
        // Arrange
        Bicicleta bike = new Bicicleta();
        bike.setStatus(StatusBicicleta.APOSENTADA); // regra R4
        bike.setTranca(null);                       // não pode estar presa

        when(bicicletaRepository.findById(1L)).thenReturn(Optional.of(bike));
        when(bicicletaRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Bicicleta removida = bicicletaService.removerBicicleta(1L);

        // Assert
        assertEquals(bike, removida);
        assertEquals(StatusBicicleta.EXCLUIDA, removida.getStatus()); // status final correto

        verify(bicicletaRepository).findById(1L);
        verify(bicicletaRepository).save(bike); //  obrigatório no UC10
    }

    @Test
    void deveLancarErroQuandoBicicletaNaoExiste() {
        when(bicicletaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> bicicletaService.removerBicicleta(99L));

        verify(bicicletaRepository, never()).delete(any());
    }

    @Test
    void deveLancarExcecaoSeBicicletaNaoEncontrada() {
        when(bicicletaRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> bicicletaService.removerBicicleta(1L));
    }

    @Test
    void deveLancarExcecaoSeBicicletaNaoAposentada() {
        Bicicleta bike = new Bicicleta();
        bike.setStatus(StatusBicicleta.EM_USO);
        when(bicicletaRepository.findById(anyLong())).thenReturn(Optional.of(bike));
        assertThrows(IllegalArgumentException.class, () -> bicicletaService.removerBicicleta(1L));
    }

    @Test
    void deveLancarExcecaoSeBicicletaPossuirTranca() {
        Bicicleta bike = new Bicicleta();
        bike.setStatus(StatusBicicleta.APOSENTADA);
        bike.setTranca(new Tranca());
        when(bicicletaRepository.findById(anyLong())).thenReturn(Optional.of(bike));
        assertThrows(IllegalArgumentException.class, () -> bicicletaService.removerBicicleta(1L));
    }

    /* alterarStatusBicicleta*/
    @Test
    void deveMudarStatusDaBicicletaComSucesso() {
        // Simula uma bicicleta que já existe no "banco"
        Bicicleta bike = new Bicicleta();
        bike.setStatus(StatusBicicleta.DISPONIVEL);

        // Quando buscar por ID 1, retorna a bicicleta
        when(bicicletaRepository.findById(1L)).thenReturn(Optional.of(bike));

        // Quando salvar, retorna a mesma bicicleta
        when(bicicletaRepository.save(bike)).thenReturn(bike);

        // Chama o método do service
        Bicicleta resultado = bicicletaService.alterarStatusBicicleta(1L, StatusBicicleta.EM_REPARO);

        // Verifica se o status mudou corretamente
        assertEquals(StatusBicicleta.EM_REPARO, resultado.getStatus());
    }

    @Test
    void deveDarErroQuandoBicicletaNaoForEncontrada() {
        // Quando o ID não existe, retorna vazio
        when(bicicletaRepository.findById(99L)).thenReturn(Optional.empty());

        // Espera que o método lance erro
        assertThrows(IllegalArgumentException.class,
                () -> bicicletaService.alterarStatusBicicleta(99L, StatusBicicleta.DISPONIVEL));
    }


    /* ---------- UC08 - Fluxo Principal ---------- */
    @Test
    void deveIncluirBicicletaNovaNaRedeComSucesso() {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setNumero(101);
        bicicleta.setStatus(StatusBicicleta.NOVA);

        Tranca tranca = new Tranca();
        tranca.setStatus(StatusTranca.LIVRE);
        bicicleta.setTranca(tranca);

        when(bicicletaRepository.findByNumero(101)).thenReturn(Optional.of(bicicleta));

        doNothing().when(emailService).enviarEmail(any(), any(), any());

        String resultado = bicicletaService.incluirBicicletaNaRede(101, 87L);

        assertEquals("Bicicleta incluída com sucesso na rede de totens.", resultado);
        assertEquals(StatusBicicleta.DISPONIVEL, bicicleta.getStatus());
        assertEquals(StatusTranca.OCUPADA, tranca.getStatus());
        assertNotNull(bicicleta.getDataInsercao());

        verify(bicicletaRepository).save(bicicleta);
    }

    @Test
    void deveLancarExcecaoSeBicicletaNaoTemTranca() {
        Bicicleta bike = new Bicicleta();
        bike.setTranca(null);

        when(bicicletaRepository.findByNumero(1)).thenReturn(Optional.of(bike));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> bicicletaService.incluirBicicletaNaRede(1, 10L)
        );

        assertEquals("Bicicleta não está associada a nenhuma tranca.", ex.getMessage());
    }

    @Test
    void deveLancarExcecaoSeBicicletaEmUso() {
        Bicicleta bike = new Bicicleta();
        bike.setStatus(StatusBicicleta.EM_USO);
        bike.setTranca(new Tranca());

        when(bicicletaRepository.findByNumero(1)).thenReturn(Optional.of(bike));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> bicicletaService.incluirBicicletaNaRede(1, 10L)
        );

        assertEquals("A bicicleta está em uso e não pode ser incluída na rede.", ex.getMessage());
    }

    @Test
    void deveLancarExcecaoSeStatusInvalido() {
        Bicicleta bike = new Bicicleta();
        bike.setStatus(StatusBicicleta.APOSENTADA);
        bike.setTranca(new Tranca());

        when(bicicletaRepository.findByNumero(1)).thenReturn(Optional.of(bike));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> bicicletaService.incluirBicicletaNaRede(1, 10L)
        );

        assertEquals("Bicicleta não está apta para inclusão (deve ser NOVA ou EM_REPARO).", ex.getMessage());
    }

    @Test
    void deveLancarExcecaoSeTrancaOcupada() {
        Tranca tranca = new Tranca();
        tranca.setStatus(StatusTranca.OCUPADA);

        Bicicleta bike = new Bicicleta();
        bike.setStatus(StatusBicicleta.NOVA);
        bike.setTranca(tranca);

        when(bicicletaRepository.findByNumero(1)).thenReturn(Optional.of(bike));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> bicicletaService.incluirBicicletaNaRede(1, 10L)
        );

        assertEquals("A tranca selecionada não está disponível.", ex.getMessage());
    }

    // Número da bicicleta inválido
    @Test
    void deveLancarErroQuandoBicicletaNaoExistir() {
        when(bicicletaRepository.findByNumero(99)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> bicicletaService.incluirBicicletaNaRede(99, 10L)
        );

        assertTrue(ex.getMessage().contains("não encontrada"));
    }

    // E3: Bicicleta em uso
    @Test
    void deveLancarErroQuandoBicicletaEstiverEmUso() {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setStatus(StatusBicicleta.EM_USO);

        Tranca tranca = new Tranca();
        tranca.setStatus(StatusTranca.LIVRE);
        bicicleta.setTranca(tranca);

        when(bicicletaRepository.findByNumero(1)).thenReturn(Optional.of(bicicleta));
        // quando chamar service.incluirBicicletaNaRede, aconteça um erro do tipo IllegalArgumentException
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> bicicletaService.incluirBicicletaNaRede(1, 10L)
                // mensagem de erro capturada e guardada na variável ex
        );

        assertEquals(
                "A bicicleta está em uso e não pode ser incluída na rede.",
                ex.getMessage() //verifica se os dois batem
        );
    }


    /*retirarBicicletaDaRede*/
    @Test
    void deveRetirarBicicletaComSucessoParaReparo() {

        Bicicleta bike = new Bicicleta();
        bike.setId(1L);
        bike.setStatus(StatusBicicleta.EM_USO);

        // Tranca
        Tranca tranca = new Tranca();
        tranca.setId(10L);
        tranca.setNumero(123);
        tranca.setStatus(StatusTranca.OCUPADA);
        bike.setTranca(tranca);

        // Mock: bicicleta encontrada
        doReturn(bike).when(bicicletaService).retornarBicicleta(1L);

        // Mock: destrancar retorna a própria tranca
        Mockito.doReturn(tranca).when(trancaService).destrancar(10L);

        // Mock salvar
        when(trancaRepository.save(any())).thenReturn(tranca);
        when(bicicletaRepository.save(any())).thenReturn(bike);

        String msg = bicicletaService.retirarBicicletaDaRede(123, "reparo", 1L);

        assertEquals("Bicicleta 1 retirada com sucesso.", msg);
        assertEquals(StatusBicicleta.EM_REPARO, bike.getStatus());
    }

    @Test
    void deveRetirarBicicletaParaAposentadoria() {
        Bicicleta bike = new Bicicleta();
        bike.setId(2L);
        bike.setStatus(StatusBicicleta.EM_USO);

        Tranca tr = new Tranca();
        tr.setId(20L);
        tr.setNumero(8);
        tr.setStatus(StatusTranca.OCUPADA);
        bike.setTranca(tr);

        doReturn(bike).when(bicicletaService).retornarBicicleta(2L);
        when(trancaService.destrancar(20L)).thenReturn(tr);
        when(trancaRepository.save(any())).thenReturn(tr);
        when(bicicletaRepository.save(any())).thenReturn(bike);

        String msg = bicicletaService.retirarBicicletaDaRede(8, "aposentadoria", 2L);

        assertEquals("Bicicleta 2 retirada com sucesso.", msg);
        assertEquals(StatusBicicleta.APOSENTADA, bike.getStatus());
    }

    @Test
    void deveLancarErroSeBicicletaNaoTemTranca() {

        Bicicleta bike = new Bicicleta(); // sem tranca

        doReturn(bike).when(bicicletaService).retornarBicicleta(1L);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> bicicletaService.retirarBicicletaDaRede(123, "reparo", 1L)
        );

        assertEquals("A bicicleta não está presa a nenhuma tranca.", ex.getMessage());
    }

    @Test
    void deveLancarErroParaNumeroDeTrancaInvalido() {

        Bicicleta bike = new Bicicleta();
        Tranca tr = new Tranca();
        tr.setNumero(10);
        bike.setTranca(tr);

        doReturn(bike).when(bicicletaService).retornarBicicleta(1L);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> bicicletaService.retirarBicicletaDaRede(99, "reparo", 1L)
        );

        assertEquals("Número da tranca inválido.", ex.getMessage());
    }

    @Test
    void deveLancarErroParaOperacaoInvalida() {

        Bicicleta bike = new Bicicleta();
        bike.setStatus(StatusBicicleta.EM_USO);

        Tranca tr = new Tranca();
        tr.setNumero(10);
        tr.setStatus(StatusTranca.OCUPADA);

        bike.setTranca(tr);

        doReturn(bike).when(bicicletaService).retornarBicicleta(1L);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> bicicletaService.retirarBicicletaDaRede(10, "INVALIDO", 1L)
        );

        assertEquals("Operação inválida. Use 'reparo' ou 'aposentadoria'.", ex.getMessage());
    }


}
