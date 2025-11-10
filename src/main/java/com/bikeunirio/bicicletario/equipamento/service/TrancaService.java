package com.bikeunirio.bicicletario.equipamento.service;

import com.bikeunirio.bicicletario.equipamento.entity.Tranca;
import com.bikeunirio.bicicletario.equipamento.enums.StatusTranca;
import com.bikeunirio.bicicletario.equipamento.repository.TrancaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrancaService {

    @Autowired
    private TrancaRepository trancaRepository;

    /* ---------- LISTAR TODAS AS TRANÇAS ---------- */
    public List<Tranca> listarTrancas() {
        return trancaRepository.findAll();
    }

    /* ---------- CADASTRAR NOVA TRANCA ---------- */
    public Tranca cadastrarTranca(Tranca tranca) {
        if (tranca == null || tranca.getNumero() == null ||
                tranca.getModelo() == null || tranca.getAnoDeFabricacao() == null) {
            throw new IllegalArgumentException("Número, modelo e ano de fabricação são obrigatórios.");
        }
        return trancaRepository.save(tranca);
    }


    /* ---------- BUSCAR TRANCA POR ID ---------- */
    public Tranca buscarPorId(Long idTranca) {
        return trancaRepository.findById(idTranca)
                .orElseThrow(() -> new IllegalArgumentException("Tranca não encontrada: " + idTranca));
    }

    /* ---------- EDITAR TRANCA ---------- */
    public Tranca editarTranca(Long idTranca, Tranca novosDados) {
        Tranca existente = trancaRepository.findById(idTranca)
                .orElseThrow(() -> new IllegalArgumentException("Tranca não encontrada: " + idTranca));

        existente.setNumero(novosDados.getNumero());
        existente.setModelo(novosDados.getModelo());
        existente.setAnoDeFabricacao(novosDados.getAnoDeFabricacao());
        existente.setLocalizacao(novosDados.getLocalizacao());
        existente.setStatus(novosDados.getStatus());

        return trancaRepository.save(existente);
    }

    /* ---------- EXCLUIR TRANCA ---------- */
    public Tranca excluirTranca(Long idTranca) {
        Tranca existente = trancaRepository.findById(idTranca)
                .orElseThrow(() -> new IllegalArgumentException("Tranca não encontrada: " + idTranca));

        trancaRepository.delete(existente);
        return existente;
    }

    /* ---------- RETORNAR BICICLETA NA TRANCA ---------- */
    public Object retornarBicicletaNaTranca(Long idTranca) {
        Tranca tranca = trancaRepository.findById(idTranca)
                .orElseThrow(() -> new IllegalArgumentException("Tranca não encontrada: " + idTranca));

        if (tranca.getBicicleta().getId() == null) {
            throw new IllegalArgumentException("Nenhuma bicicleta está presa nesta tranca.");
        }

        // aqui você pode futuramente integrar com o serviço de bicicletas
        return tranca.getBicicleta().getId();
    }

    /* ---------- ALTERAR STATUS DA TRANCA ---------- */
    public Tranca alterarStatusDaTranca(Long idTranca, String acao) {
        Tranca tranca = trancaRepository.findById(idTranca)
                .orElseThrow(() -> new IllegalArgumentException("Tranca não encontrada: " + idTranca));

        try {
            StatusTranca novoStatus = StatusTranca.valueOf(acao.toUpperCase());
            tranca.setStatus(novoStatus);
            return trancaRepository.save(tranca);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Status inválido: " + acao);
        }
    }


}
