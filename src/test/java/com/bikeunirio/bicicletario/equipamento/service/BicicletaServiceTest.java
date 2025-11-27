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
        // ARRANGE
        Long idBicicleta = 101L;
        Long idTranca = 87L;
        Long idReparador = 50L;

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setNumero(101);
        bicicleta.setStatus(StatusBicicleta.NOVA);

        Tranca tranca = new Tranca();
        tranca.setId(idTranca);
        tranca.setNumero(200);
        tranca.setStatus(StatusTranca.LIVRE);

        // --- Mock para retornar a bicicleta buscada ---
        when(bicicletaRepository.findById(idBicicleta))
                .thenReturn(Optional.of(bicicleta));

        // --- Mock para retornar a tranca buscada ---
        when(trancaRepository.findById(idTranca))
                .thenReturn(Optional.of(tranca));

        // --- Mock do método trancar(idReparador) ---
        Tranca trancaTrancada = new Tranca();
        trancaTrancada.setId(idTranca);
        trancaTrancada.setStatus(StatusTranca.OCUPADA);

        when(trancaService.trancar(idReparador))
                .thenReturn(trancaTrancada);

        // --- Mock do envio de email ---
        doNothing().when(emailService).enviarEmail(any(), any(), any());

        // ACT
        String resultado = bicicletaService.incluirBicicletaNaRede(idBicicleta, idTranca, idReparador);

        // ASSERT
        assertEquals("Bicicleta incluída com sucesso na rede de totens.", resultado);

        // Verifica status finais
        assertEquals(StatusBicicleta.DISPONIVEL, bicicleta.getStatus());
        assertEquals(StatusTranca.OCUPADA, tranca.getStatus());

        // Verifica associação entre bicicleta e tranca
        assertEquals(tranca, bicicleta.getTranca());
        assertEquals(bicicleta, tranca.getBicicleta());

        // Verifica persistência
        verify(bicicletaRepository).save(bicicleta);
        verify(trancaRepository).save(tranca);

        // Verifica se trancar foi chamado
        verify(trancaService).trancar(idReparador);
    }


    @Test
    void deveLancarErroQuandoBicicletaEstiverEmUso() {
        // ARRANGE
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setStatus(StatusBicicleta.EM_USO);

        // Precisamos de uma tranca válida para passar pela validação inicial de IDs
        Tranca tranca = new Tranca();
        tranca.setStatus(StatusTranca.LIVRE);

        // MOCKS
        when(bicicletaRepository.findById(1L)).thenReturn(Optional.of(bicicleta));
        // Se não mockar a tranca, ele lança erro de "Tranca não encontrada" antes de checar o status da bike
        when(trancaRepository.findById(10L)).thenReturn(Optional.of(tranca));

        // ACT & ASSERT
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> bicicletaService.incluirBicicletaNaRede(1L, 10L, 54L)
        );

        assertEquals("A bicicleta está em uso e não pode ser incluída na rede.", ex.getMessage());
    }


    @Test
    void deveLancarErroQuandoTrancaNaoExistir() {
        // ARRANGE
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setStatus(StatusBicicleta.NOVA);

        // MOCKS
        when(bicicletaRepository.findById(1L)).thenReturn(Optional.of(bicicleta));
        when(trancaRepository.findById(999L)).thenReturn(Optional.empty()); // Tranca não existe

        // ACT & ASSERT
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> bicicletaService.incluirBicicletaNaRede(1L, 999L, 34L)
        );

        assertEquals("Tranca não encontrada.", ex.getMessage());
    }

    @Test
    void deveLancarErroQuandoReparadorForDiferenteNoRetorno() {
        // Cenário: Bike em reparo devolvida por outro funcionário
        Long idReparadorOriginal = 123L;
        Long idReparadorImpostor = 999L;

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setStatus(StatusBicicleta.EM_REPARO);
        bicicleta.setMatriculaReparador(idReparadorOriginal); // Quem pegou

        Tranca tranca = new Tranca();

        when(bicicletaRepository.findById(1L)).thenReturn(Optional.of(bicicleta));
        when(trancaRepository.findById(10L)).thenReturn(Optional.of(tranca));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> bicicletaService.incluirBicicletaNaRede(1L, 10L, idReparadorImpostor)
        );

        assertEquals("Apenas o reparador que retirou a bicicleta pode devolvê-la.", ex.getMessage());
    }

    /*retirarBicicletaDaRede*/
    /*----------------- UC09: retirarBicicletaDaRede -----------------*/

    @Test
    void deveRetirarBicicletaComSucessoParaReparo() {

        Long idTranca = 10L;
        Long matriculaReparador = 123L;

        // Bicicleta
        Bicicleta bike = new Bicicleta();
        bike.setId(1L);
        bike.setNumero(55);
        bike.setStatus(StatusBicicleta.REPARO_SOLICITADO);

        // Tranca
        Tranca tranca = new Tranca();
        tranca.setId(idTranca);
        tranca.setNumero(10);
        tranca.setStatus(StatusTranca.OCUPADA);
        tranca.setBicicleta(bike);

        // Mocks
        when(trancaRepository.findById(idTranca)).thenReturn(Optional.of(tranca));
        when(trancaService.destrancar(idTranca)).thenReturn(tranca);

        when(trancaRepository.save(any())).thenReturn(tranca);
        when(bicicletaRepository.save(any())).thenReturn(bike);

        // Mock do email
        doNothing().when(emailService).enviarEmail(any(), any(), any());

        // Execução
        String msg = bicicletaService.retirarBicicletaDaRede(idTranca, matriculaReparador, "reparo");

        // Asserts
        assertEquals("Bicicleta 1 retirada com sucesso.", msg);
        assertEquals(StatusBicicleta.EM_REPARO, bike.getStatus());
    }

    @Test
    void deveRetirarBicicletaParaAposentadoria() {

        Long idTranca = 20L;
        Long matriculaReparador = 222L;

        Bicicleta bike = new Bicicleta();
        bike.setId(2L);
        bike.setNumero(77);
        bike.setStatus(StatusBicicleta.REPARO_SOLICITADO);

        Tranca tr = new Tranca();
        tr.setId(idTranca);
        tr.setNumero(20);
        tr.setStatus(StatusTranca.OCUPADA);
        tr.setBicicleta(bike);

        when(trancaRepository.findById(idTranca)).thenReturn(Optional.of(tr));
        when(trancaService.destrancar(idTranca)).thenReturn(tr);

        when(trancaRepository.save(any())).thenReturn(tr);
        when(bicicletaRepository.save(any())).thenReturn(bike);

        // Mock do email
        doNothing().when(emailService).enviarEmail(any(), any(), any());

        String msg = bicicletaService.retirarBicicletaDaRede(idTranca, matriculaReparador, "aposentadoria");

        assertEquals("Bicicleta 2 retirada com sucesso.", msg);
        assertEquals(StatusBicicleta.APOSENTADA, bike.getStatus());
    }

    @Test
    void deveLancarErroSeTrancaNaoTemBicicleta() {

        Long idTranca = 10L;

        Tranca tranca = new Tranca();
        tranca.setId(idTranca);
        tranca.setBicicleta(null); // sem bicicleta -> erro esperado

        when(trancaRepository.findById(idTranca))
                .thenReturn(Optional.of(tranca));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> bicicletaService.retirarBicicletaDaRede(idTranca, 999L, "reparo")
        );

        assertEquals("A tranca está livre — não há bicicleta para retirar.", ex.getMessage());
    }





}
