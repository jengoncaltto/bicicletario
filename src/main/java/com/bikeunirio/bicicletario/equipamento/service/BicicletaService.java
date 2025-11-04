package com.bikeunirio.bicicletario.equipamento.service;

import com.bikeunirio.bicicletario.equipamento.repository.BicicletaRepository;
import org.springframework.stereotype.Service;

@Service
public class BicicletaService {

    private BicicletaRepository repository;
    public void setRepository(BicicletaRepository repository) {
        this.repository = repository;
    }
}
