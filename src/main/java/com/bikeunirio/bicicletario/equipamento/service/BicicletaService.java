package com.bikeunirio.bicicletario.equipamento.service;

import com.bikeunirio.bicicletario.equipamento.entity.Bicicleta;
import com.bikeunirio.bicicletario.equipamento.enums.StatusBicicleta;
import com.bikeunirio.bicicletario.equipamento.repository.BicicletaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

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
                .orElseThrow(() -> new IllegalArgumentException("não encontrada."));
    }

    public Bicicleta editarBicicleta(Long idBicicleta, Bicicleta bicicleta) {
        Bicicleta existente = repository.findById(idBicicleta)
                .orElseThrow(() -> new IllegalArgumentException("não encontrada: " + idBicicleta));

        existente.setMarca(bicicleta.getMarca());
        existente.setModelo(bicicleta.getModelo());
        existente.setAno(bicicleta.getAno());
        existente.setNumero(bicicleta.getNumero());
        existente.setStatus(bicicleta.getStatus());

        return repository.save(existente);
    }

    public Bicicleta removerBicicleta(Long idBicicleta) {
        Bicicleta bike = repository.findById(idBicicleta)
                .orElseThrow(() -> new IllegalArgumentException("não encontrada: " + idBicicleta));

        repository.delete(bike);
        return bike;
    }

    public Bicicleta alterarStatusBicicleta(Long idBicicleta, String acao) {
        Bicicleta atualizada = repository.findById(idBicicleta)
                .orElseThrow(() -> new IllegalArgumentException("não encontrada: " + idBicicleta));

        // converte String para Enum (ex: "DISPONIVEL" → StatusBicicleta.DISPONIVEL)
        StatusBicicleta novoStatus = StatusBicicleta.valueOf(acao.toUpperCase());

        // altera o status
        atualizada.setStatus(novoStatus);

        // salva e retorna a bicicleta atualizada
        return repository.save(atualizada);
    }



}
