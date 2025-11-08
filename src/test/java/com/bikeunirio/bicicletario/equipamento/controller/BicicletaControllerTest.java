package com.bikeunirio.bicicletario.equipamento.controller;

import com.bikeunirio.bicicletario.equipamento.entity.Bicicleta;
import com.bikeunirio.bicicletario.equipamento.enums.StatusBicicleta;
import com.bikeunirio.bicicletario.equipamento.service.BicicletaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;


import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BicicletaController.class)
class BicicletaControllerTest {

    @Autowired
    private MockMvc mockMvc; // simula as requisições HTTP

    @MockitoBean
    private BicicletaService service; // mock do service (não chama o banco)

    /*Endpoint: listarBicicletas()*/
    @Test
    void deveRetornarListaDeBicicletasComStatus200() throws Exception {
        // Cria uma bicicleta falsa
        Bicicleta bike = new Bicicleta();
        bike.setMarca("Caloi");
        bike.setModelo("Elite");
        bike.setAno("2023");
        bike.setNumero(123);
        bike.setStatus(StatusBicicleta.DISPONIVEL);
        bike.setTrancaId(1L);
        bike.setTotemId(2L);

        // Simula o ID com reflexão (já que o campo é private e sem setId)
        Field idField = Bicicleta.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(bike, 1L);

        // Mocka o comportamento do service
        when(service.listarBicicletas()).thenReturn(List.of(bike));

        // Executa GET /bicicleta e verifica o JSON
        mockMvc.perform(get("/bicicleta"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].marca").value("Caloi"))
                .andExpect(jsonPath("$[0].modelo").value("Elite"))
                .andExpect(jsonPath("$[0].ano").value("2023"))
                .andExpect(jsonPath("$[0].numero").value(123))
                .andExpect(jsonPath("$[0].status").value("DISPONIVEL"));
    }

    @Test
    void deveRetornarStatus204QuandoNaoHaBicicletas() throws Exception {
        // Mocka o service para retornar uma lista vazia
        when(service.listarBicicletas()).thenReturn(Collections.emptyList());

        // Executa GET /bicicleta e verifica o status
        mockMvc.perform(get("/bicicleta"))
                .andExpect(status().isNoContent());
    }


    /*Enpoint: cadastrarBicicleta(@RequestBody Bicicleta bicicleta)*/
    @Test
    void deveCadastrarBicicletaComSucesso() throws Exception {
        Bicicleta salva = new Bicicleta();
        ReflectionTestUtils.setField(salva, "id", 1L);
        salva.setMarca("Caloi");
        salva.setModelo("Elite");
        salva.setAno("2023");
        salva.setNumero(123);
        salva.setStatus(StatusBicicleta.DISPONIVEL);

        when(service.cadastrarBicicleta(any(Bicicleta.class))).thenReturn(salva);

        mockMvc.perform(post("/bicicleta")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "marca": "Caloi",
                        "modelo": "Elite",
                        "ano": "2023",
                        "numero": 123,
                        "status": "DISPONIVEL"
                    }
                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.marca").value("Caloi"))
                .andExpect(jsonPath("$.modelo").value("Elite"));
    }

    @Test
    void deveRetornar422QuandoDadosInvalidos() throws Exception {
        when(service.cadastrarBicicleta(any(Bicicleta.class)))
                .thenThrow(new IllegalArgumentException("Marca e modelo são obrigatórios."));

        mockMvc.perform(post("/bicicleta")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "ano": "2023",
                        "numero": 123,
                        "status": "DISPONIVEL"
                    }
                """))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$[0].codigo").value("DADOS_INVALIDOS"))
                .andExpect(jsonPath("$[0].mensagem").value("Marca e modelo são obrigatórios."));
    }


    /*Endpoint: retornarBicicleta(@PathVariable long idBicicleta*/
    @Test
    void bicicletaRetornarStatus200() throws Exception {
        Bicicleta bike = new Bicicleta();
        ReflectionTestUtils.setField(bike, "id", 1L);
        bike.setMarca("Caloi");
        bike.setModelo("Elite");
        bike.setAno("2023");
        bike.setNumero(123);
        bike.setStatus(StatusBicicleta.DISPONIVEL);

        // Quando o service for chamado com id 1L, retorna a bike mockada
        when(service.retornarBicicleta(1L)).thenReturn(bike);

        mockMvc.perform(get("/bicicleta/{idBicicleta}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.marca").value("Caloi"))
                .andExpect(jsonPath("$.modelo").value("Elite"))
                .andExpect(jsonPath("$.ano").value("2023"))
                .andExpect(jsonPath("$.numero").value(123))
                .andExpect(jsonPath("$.status").value("DISPONIVEL"));
    }

    @Test
    void deveRetornar404QuandoBicicletaNaoEncontrada() throws Exception {
        // Simula o comportamento do service lançando exceção quando não encontra
        when(service.retornarBicicleta(99L))
                .thenThrow(new IllegalArgumentException("Bicicleta não encontrada."));

        mockMvc.perform(get("/bicicleta/{idBicicleta}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.codigo").value("NAO_ENCONTRADO"))
                .andExpect(jsonPath("$.mensagem").value("Bicicleta não encontrada."));
    }

}
