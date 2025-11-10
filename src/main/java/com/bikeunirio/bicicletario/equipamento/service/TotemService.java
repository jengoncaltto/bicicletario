package com.bikeunirio.bicicletario.equipamento.service;

import com.bikeunirio.bicicletario.equipamento.entity.Totem;
import com.bikeunirio.bicicletario.equipamento.repository.TotemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TotemService {

    @Autowired
    private TotemRepository repository;

    // CREATE
    public Totem cadastrarTotem(Totem totem) {
        if (totem.getLocalizacao() == null || totem.getDescricao() == null) {
            throw new IllegalArgumentException("Localização e descrição são obrigatórias.");
        }

        return repository.save(totem);
    }

    public List<Totem> listarTotens() {
        return repository.findAll();
    }

    public Totem editarTotem(Long idTotem, Totem novosDados) {
        Totem existente = repository.findById(idTotem)
                .orElseThrow(() -> new IllegalArgumentException("Totem não encontrado: " + idTotem));

        existente.setLocalizacao(novosDados.getLocalizacao());
        existente.setDescricao(novosDados.getDescricao());

        return repository.save(existente);
    }

    public Totem excluirTotem(Long idTotem) {
        Totem existente = repository.findById(idTotem)
                .orElseThrow(() -> new IllegalArgumentException("Totem não encontrado: " + idTotem));

        repository.delete(existente);
        return existente;
    }
}
