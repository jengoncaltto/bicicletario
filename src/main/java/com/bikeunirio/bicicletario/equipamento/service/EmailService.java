package com.bikeunirio.bicicletario.equipamento.service;

import org.springframework.stereotype.Service;

@Service
public class EmailService {
    public void enviarEmail(String destinatario, String assunto, String corpo) {
        System.out.printf("📩 Email enviado para %s\nAssunto: %s\nCorpo:\n%s\n",
                destinatario, assunto, corpo);
    }
}
