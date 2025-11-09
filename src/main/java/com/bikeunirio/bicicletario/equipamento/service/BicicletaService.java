package com.bikeunirio.bicicletario.equipamento.service;

import com.bikeunirio.bicicletario.equipamento.entity.Bicicleta;
import com.bikeunirio.bicicletario.equipamento.repository.BicicletaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BicicletaService {

    @Autowired
    private BicicletaRepository repository;

    public List<Bicicleta> listarBicicletas() {
        return repository.findAll();
    }

    public Bicicleta cadastrarBicicleta(Bicicleta bicicleta) {
        if (bicicleta.getMarca() == null || bicicleta.getModelo() == null) {
            throw new IllegalArgumentException("Marca e modelo são obrigatórios.");
        }
        return repository.save(bicicleta);
    }

    public Bicicleta retornarBicicleta(Long idBicicleta) {
        if (idBicicleta == null) {
            throw new IllegalArgumentException("Um número é obrigatório.");
        } else if (idBicicleta < 0) {
            throw new IllegalArgumentException("Número negativo não é aceito.");
        }

        return repository.findById(idBicicleta)
                .orElseThrow(() -> new IllegalArgumentException("Bicicleta não encontrada."));
    }

}
