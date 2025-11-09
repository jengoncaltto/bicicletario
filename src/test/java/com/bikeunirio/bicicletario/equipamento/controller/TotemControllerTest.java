package com.bikeunirio.bicicletario.equipamento.controller;

import com.bikeunirio.bicicletario.equipamento.service.TotemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TotemController.class)
public class TotemControllerTest {

    @Autowired
    private MockMvc mockMvc; // simula as requisições HTTP

    @MockitoBean
    private TotemService service; // mock do service (não chama o banco)

}
