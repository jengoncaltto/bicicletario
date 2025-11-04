package com.bikeunirio.bicicletario.equipamento.service;

import com.bikeunirio.bicicletario.equipamento.repository.TrancaRepository;
import org.springframework.stereotype.Service;

@Service
public class TrancaService {
    private TrancaRepository trancaRepository;

    public TrancaService(TrancaRepository trancaRepository) {
        this.trancaRepository = trancaRepository;
    }
}
