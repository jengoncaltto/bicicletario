package com.bikeunirio.bicicletario.equipamento.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private EmailService() {
        // impede instância fora do Spring
    }

    public void enviarEmail(String destinatario, String assunto, String corpo) {
        logger.info("📩 Email enviado para {}\nAssunto: {}\nCorpo:\n{}", destinatario, assunto, corpo);
    }
}
