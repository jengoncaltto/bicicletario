package com.bikeunirio.bicicletario.equipamento.service;

import com.bikeunirio.bicicletario.equipamento.entity.Totem;
import com.bikeunirio.bicicletario.equipamento.repository.TotemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TotemServiceTest {

    @Mock
    private TotemRepository repository;

    @InjectMocks
    private TotemService service;

    /* ---------- cadastrarTotem ---------- */
    @Test
    void deveCadastrarTotemComSucesso() {
        Totem totem = new Totem();
        totem.setLocalizacao("Central");
        totem.setDescricao("Totem próximo ao terminal");

        when(repository.save(totem)).thenReturn(totem);

        Totem resultado = service.cadastrarTotem(totem);

        assertNotNull(resultado);
        verify(repository).save(totem);
    }

    @Test
    void deveLancarErroQuandoTotemInvalido() {
        Totem totem = new Totem(); // sem localização e descrição

        IllegalArgumentException erro = assertThrows(IllegalArgumentException.class,
                () -> service.cadastrarTotem(totem));

        assertEquals("Localização e descrição são obrigatórias.", erro.getMessage());
        verify(repository, never()).save(any());
    }

    /* ---------- listarTotens ---------- */
    @Test
    void deveListarTotens() {
        Totem t1 = new Totem();
        Totem t2 = new Totem();
        when(repository.findAll()).thenReturn(List.of(t1, t2));

        List<Totem> resultado = service.listarTotens();

        assertEquals(2, resultado.size());
        verify(repository).findAll();
    }

    /* ---------- editarTotem ---------- */
    @Test
    void deveEditarTotemComSucesso() {
        Totem existente = new Totem();
        existente.setLocalizacao("lapa");
        existente.setDescricao("próximo aos arcos");

        Totem novos = new Totem();
        novos.setLocalizacao("lapa");
        novos.setDescricao("próximo ao circo");

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(existente)).thenReturn(existente);

        Totem atualizado = service.editarTotem(1L, novos);

        assertEquals("lapa", atualizado.getLocalizacao());
        assertEquals("próximo ao circo", atualizado.getDescricao());
        verify(repository).save(existente);
    }

    @Test
    void deveLancarErroAoEditarTotemNaoEncontrado() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        Totem totem = new Totem(); // criar fora da lambda

        assertThrows(IllegalArgumentException.class,
                () -> service.editarTotem(99L, totem));

        verify(repository, never()).save(any());
    }


    /* ---------- excluirTotem ---------- */
    @Test
    void deveExcluirTotemComSucesso() {
        Totem totem = new Totem();
        totem.setTrancas(new ArrayList<>()); // evita NullPointer

        when(repository.findById(1L)).thenReturn(Optional.of(totem));

        Totem removido = service.excluirTotem(1L);

        assertEquals(totem, removido);
        verify(repository).delete(totem);
    }

    @Test
    void deveLancarErroAoExcluirTotemNaoEncontrado() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> service.excluirTotem(99L));

        verify(repository, never()).delete(any());
    }
}
