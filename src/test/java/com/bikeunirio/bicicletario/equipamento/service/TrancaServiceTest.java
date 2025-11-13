package com.bikeunirio.bicicletario.equipamento.service;

import com.bikeunirio.bicicletario.equipamento.entity.Bicicleta;
import com.bikeunirio.bicicletario.equipamento.entity.Tranca;
import com.bikeunirio.bicicletario.equipamento.enums.StatusTranca;
import com.bikeunirio.bicicletario.equipamento.repository.TrancaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrancaServiceTest {

    @Mock
    private TrancaRepository trancaRepository;

    @InjectMocks
    private TrancaService trancaService;

    /* ---------- listarTrancas ---------- */
    @Test
    void deveListarTodasAsTrancas() {
        Tranca t1 = new Tranca();
        t1.setModelo("Tranca A");
        when(trancaRepository.findAll()).thenReturn(List.of(t1));

        List<Tranca> resultado = trancaService.listarTrancas();

        assertEquals(1, resultado.size());
        assertEquals("Tranca A", resultado.get(0).getModelo());
        verify(trancaRepository).findAll();
    }

    /* ---------- cadastrarTranca ---------- */
    @Test
    void deveCadastrarTrancaComSucesso() {
        Tranca nova = new Tranca();
        nova.setAnoDeFabricacao("2024");
        nova.setModelo("Nova Tranca");

        when(trancaRepository.save(nova)).thenReturn(nova);

        Tranca resultado = trancaService.cadastrarTranca(nova);

        assertEquals("Nova Tranca", resultado.getModelo());
        verify(trancaRepository).save(nova);
    }

    /* ---------- buscarPorId ---------- */
    @Test
    void deveBuscarTrancaPorIdComSucesso() {
        Tranca tranca = new Tranca();
        tranca.setModelo("Tranca X");
        when(trancaRepository.findById(1L)).thenReturn(Optional.of(tranca));

        Tranca resultado = trancaService.buscarPorId(1L);

        assertEquals("Tranca X", resultado.getModelo());
        verify(trancaRepository).findById(1L);
    }

    @Test
    void deveLancarErroQuandoTrancaNaoExistir() {
        when(trancaRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> trancaService.buscarPorId(99L));

        assertTrue(ex.getMessage().contains("não encontrada"));
    }

    /* ---------- editarTranca ---------- */
    @Test
    void deveEditarTrancaComSucesso() {
        Tranca existente = new Tranca();
        existente.setModelo("Antigo");

        Tranca novosDados = new Tranca();
        novosDados.setModelo("Novo Modelo");

        when(trancaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(trancaRepository.save(existente)).thenReturn(existente);

        Tranca resultado = trancaService.editarTranca(1L, novosDados);

        assertEquals("Novo Modelo", resultado.getModelo());
        verify(trancaRepository).save(existente);
    }

    @Test
    void deveLancarErroAoEditarTrancaInexistente() {
        Tranca novosDados = new Tranca();
        when(trancaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> trancaService.editarTranca(99L, novosDados));
    }

    /* ---------- excluirTranca ---------- */
    @Test
    void deveRemoverTrancaComSucesso() {
        Tranca existente = new Tranca();
        existente.setStatus(StatusTranca.APOSENTADA);
        existente.setModelo("Novo Modelo");
        existente.setAnoDeFabricacao("2024");

        when(trancaRepository.findById(1L)).thenReturn(Optional.of(existente));

        // mocka o save — devolve o próprio objeto, evitando null
        when(trancaRepository.save(any(Tranca.class))).thenAnswer(inv -> inv.getArgument(0));

        Tranca resultado = trancaService.removerTranca(1L);

        assertEquals(StatusTranca.EXCLUIDA, resultado.getStatus());
        verify(trancaRepository).save(existente);
    }


    @Test
    void deveLancarErroAoRemoverTrancaInexistente() {
        when(trancaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> trancaService.removerTranca(99L));
    }

    /* ---------- retornarBicicletaNaTranca ---------- */
    @Test
    void deveRetornarBicicletaAssociadaComSucesso() {
        Tranca tranca = new Tranca();
        Bicicleta bicicleta = new Bicicleta();
        ReflectionTestUtils.setField(bicicleta, "id", 5L); // define o id via reflexão
        tranca.setBicicleta(bicicleta);

        when(trancaRepository.findById(1L)).thenReturn(Optional.of(tranca));

        Object resultado = trancaService.retornarBicicletaNaTranca(1L);

        assertEquals(5L, resultado);
        verify(trancaRepository).findById(1L);
    }

    @Test
    void deveLancarErroQuandoTrancaNaoForEncontradaAoBuscarBicicleta() {
        when(trancaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> trancaService.retornarBicicletaNaTranca(1L));

        verify(trancaRepository).findById(1L);
    }

    /* ---------- alterarStatusDaTranca ---------- */
    @Test
    void deveAlterarStatusParaTrancadaComSucesso() {
        Tranca tranca = new Tranca();
        tranca.setStatus(StatusTranca.OCUPADA);
        when(trancaRepository.findById(1L)).thenReturn(Optional.of(tranca));
        when(trancaRepository.save(tranca)).thenReturn(tranca);

        Tranca resultado = trancaService.alterarStatusDaTranca(1L, "OCUPADA");

        assertEquals(StatusTranca.OCUPADA, resultado.getStatus());
        verify(trancaRepository).save(tranca);
    }

    @Test
    void deveLancarErroQuandoStatusForInvalido() {
        Tranca tranca = new Tranca();
        when(trancaRepository.findById(1L)).thenReturn(Optional.of(tranca));

        assertThrows(IllegalArgumentException.class,
                () -> trancaService.alterarStatusDaTranca(1L, "QUEBRADA"));
    }

}
