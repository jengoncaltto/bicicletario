package com.bikeunirio.bicicletario.aluguel.webservice;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ExampleCpfValidationClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public boolean validarCpf(String cpf) {
        if (cpf == null)
            return false;
        return cpf.matches("\\d{11}");

        // Em um caso real, poderia ser algo como: Boolean resultado = restTemplate.getForObject("https://api.externa.com/validar?cpf=" + cpf, Boolean.class);
        // retornando: return resultado != null && resultado;
    }
}
