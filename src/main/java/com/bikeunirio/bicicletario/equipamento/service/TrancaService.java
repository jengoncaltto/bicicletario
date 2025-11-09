package com.bikeunirio.bicicletario.equipamento.service;

import com.bikeunirio.bicicletario.equipamento.repository.TrancaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrancaService {

    @Autowired
    private TrancaRepository trancaRepository;

}
