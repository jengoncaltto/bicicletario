package com.bikeunirio.bicicletario.equipamento.service;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


import com.bikeunirio.bicicletario.equipamento.entity.Bicicleta;
import com.bikeunirio.bicicletario.equipamento.repository.BicicletaRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BicicletaServiceTest {

    @Mock
    private BicicletaRepository repository;

    @InjectMocks
    private BicicletaService service;

    @Test
    void deveListarBicicletasComSucesso() {
        Bicicleta bike = new Bicicleta();
        bike.setModelo("Caloi");

        when(repository.findAll()).thenReturn(List.of(bike));

        List<Bicicleta> resultado = service.listarBicicletas();

        assertEquals(1, resultado.size());
        assertEquals("Caloi", resultado.get(0).getModelo());
        verify(repository, times(1)).findAll();
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHaBicicletas() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        List<Bicicleta> resultado = service.listarBicicletas();

        assertTrue(resultado.isEmpty());
        verify(repository, times(1)).findAll();
    }
}
