package com.bikeunirio.bicicletario.equipamento.service;

import com.bikeunirio.bicicletario.equipamento.entity.Bicicleta;
import com.bikeunirio.bicicletario.equipamento.repository.BicicletaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BicicletaService {

    private BicicletaRepository repository;
    public void setRepository(BicicletaRepository repository) {
        this.repository = repository;
    }

    public List<Bicicleta> listar() {
        return repository.findAll();
    }


}
