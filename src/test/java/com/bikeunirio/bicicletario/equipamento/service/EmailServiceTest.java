package com.bikeunirio.bicicletario.equipamento.service;

import org.junit.jupiter.api.Test;

class EmailServiceTest {

    @Test
    void deveEnviarEmailComSucesso() {
        EmailService service = new EmailService();
        service.enviarEmail("teste@exemplo.com", "Assunto de Teste", "Corpo do email");
        // Nenhuma exceção lançada = teste OK
    }
}
