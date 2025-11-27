package com.bikeunirio.bicicletario.equipamento.service;

import com.bikeunirio.bicicletario.equipamento.entity.Bicicleta;
import com.bikeunirio.bicicletario.equipamento.entity.Totem;
import com.bikeunirio.bicicletario.equipamento.entity.Tranca;
import com.bikeunirio.bicicletario.equipamento.repository.TotemRepository;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
public class TotemService {


    private final TotemRepository repository;

    public TotemService(TotemRepository repository) {
        this.repository = repository;
    }

    public Totem cadastrarTotem(Totem totem) {
        if (totem.getLocalizacao() == null || totem.getDescricao() == null) {
            throw new IllegalArgumentException("Localização e descrição são obrigatórias.");
        }

        return repository.save(totem);
    }

    public List<Totem> listarTotens() {
        return repository.findAll();
    }

    public Totem excluirTotem(Long idTotem) {
        Totem existente = acharTotem(idTotem);

        //Apenas os totens que não possuem nenhuma tranca podem ser excluídos.
        if(!existente.getTrancas().isEmpty()){
            throw new IllegalArgumentException("Só é possível excluir totem sem tranca.");
        }
        repository.delete(existente);
        return existente;
    }

    public List<Tranca> listarTrancasDeUmTotem(Long idTotem) {
        Totem existente = acharTotem(idTotem);

        // Retorna a lista (vazia ou não)
        return existente.getTrancas();
    }

    public List<Bicicleta> listarBicicletasDeUmTotem(Long idTotem) {
        Totem existente = acharTotem(idTotem);

        return existente.getBicicletas();
    }

    public Totem editarTotem(Long idTotem, Totem novosDados) {
        Totem existente = repository.findById(idTotem)
                .orElseThrow(() -> new IllegalArgumentException("Totem não encontrado: " + idTotem));

        existente.setLocalizacao(novosDados.getLocalizacao());
        existente.setDescricao(novosDados.getDescricao());

        return repository.save(existente);
    }

    private Totem acharTotem(Long idTotem) {
        return repository.findById(idTotem)
                .orElseThrow(() -> new IllegalArgumentException("não encontrado: " + idTotem));
    }

}
