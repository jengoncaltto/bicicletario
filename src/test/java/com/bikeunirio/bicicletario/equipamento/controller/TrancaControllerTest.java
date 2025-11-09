package com.bikeunirio.bicicletario.equipamento.controller;

import com.bikeunirio.bicicletario.equipamento.service.TotemService;
import com.bikeunirio.bicicletario.equipamento.service.TrancaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TrancaController.class)
public class TrancaControllerTest {

    @Autowired
    private MockMvc mockMvc; // simula as requisições HTTP

    @MockitoBean
    private TrancaService service; // mock do service (não chama o banco)
}
