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

    /* listarBicicletas */

    @Test
    void RetornarListaBicicletas() {
        Bicicleta bike = new Bicicleta();
        bike.setMarca("Caloi");

        // Quando o metodo findAll() do repositorio for chamado dentro do service, retorne uma lista contendo a bike
        when(repository.findAll()).thenReturn(List.of(bike));

        List<Bicicleta> resultado = service.listarBicicletas();

        // Como o metodo so retorna o resultado do repositorio, nao e necessario verificar todos os campos
        // basta checar alguns valores e se o repositorio foi chamado corretamente.

        assertEquals(1, resultado.size());
        assertEquals("Caloi", resultado.get(0).getMarca());

        // verificando interacoes, se o metodo do mock foi realmente chamado
        verify(repository, times(1)).findAll();
    }

    @Test
    void RetornarListaBicicletasVazia() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        List<Bicicleta> resultado = service.listarBicicletas();

        assertTrue(resultado.isEmpty());
        verify(repository, times(1)).findAll();
    }

    /*  */

}
