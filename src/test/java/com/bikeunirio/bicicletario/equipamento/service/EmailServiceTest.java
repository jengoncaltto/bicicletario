package com.bikeunirio.bicicletario.equipamento.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class EmailServiceTest {

    @Test
    void deveEnviarEmailComSucesso() {
        EmailService service = new EmailService();

        assertDoesNotThrow(() ->
                service.enviarEmail("teste@exemplo.com", "Assunto de Teste", "Corpo do email")
        );
    }
}