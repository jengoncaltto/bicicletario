package com.bikeunirio.bicicletario.equipamento.service;

import com.bikeunirio.bicicletario.equipamento.entity.Tranca;
import com.bikeunirio.bicicletario.equipamento.enums.StatusTranca;
import com.bikeunirio.bicicletario.equipamento.repository.TrancaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrancaService {
    private static final String TRANCA_NAO_ENCONTRADO = "Tranca não encontrada: ";


    private final TrancaRepository trancaRepository;

    public TrancaService(TrancaRepository trancaRepository) {
        this.trancaRepository = trancaRepository;
    }

    /* ---------- LISTAR TODAS AS TRANÇAS ---------- */
    public List<Tranca> listarTrancas() {
        return trancaRepository.findAll();
    }

    /* ---------- CADASTRAR NOVA TRANCA ---------- */
    public Tranca cadastrarTranca(Tranca tranca) {
        // Dados obrigatórios: anoDeFabricacao, modelo
        if (tranca.getAnoDeFabricacao() == null || tranca.getAnoDeFabricacao().isBlank())
            throw new IllegalArgumentException("Ano de Fabricação é obrigatória.");
        if (tranca.getModelo() == null || tranca.getModelo().isBlank())
            throw new IllegalArgumentException("Modelo é obrigatória.");
        // R1 – Status inicial sempre NOVA
        tranca.setStatus(StatusTranca.NOVA);

        //extra, no caso de uso não menciona nada de como definir o numero da tranca
        // decidi fazer automaticamente(sistema fará)
        Integer numeroGerado = gerarNumeroAutomatico();
        tranca.setNumero(numeroGerado);

        return trancaRepository.save(tranca);
    }

    /* ---------- BUSCAR TRANCA POR ID ---------- */
    public Tranca buscarPorId(Long idTranca) {
        return trancaRepository.findById(idTranca)
                .orElseThrow(() -> new IllegalArgumentException(TRANCA_NAO_ENCONTRADO + idTranca));
    }

    /* ---------- EDITAR TRANCA ---------- */
    public Tranca editarTranca(Long idTranca, Tranca novosDados) {
        Tranca existente = trancaRepository.findById(idTranca)
                .orElseThrow(() -> new IllegalArgumentException(TRANCA_NAO_ENCONTRADO + idTranca));

        // NÃO EDITA numero (R3)
        // NÃO EDITA status (R3)

        existente.setModelo(novosDados.getModelo());
        existente.setAnoDeFabricacao(novosDados.getAnoDeFabricacao());

        return trancaRepository.save(existente);
    }

    /* ---------- EXCLUIR TRANCA ---------- */
    public Tranca removerTranca(Long idTranca) {
        //R4: Apenas trancas que não estiverem com nenhuma bicicleta podem ser excluídas.

        Tranca trancaExistente = trancaRepository.findById(idTranca)
                .orElseThrow(() -> new IllegalArgumentException(TRANCA_NAO_ENCONTRADO + idTranca));

        if(trancaExistente.getBicicleta() != null){
            throw new IllegalArgumentException("Só é possível excluir tranca sem bicicleta.");

        }
        trancaExistente.setStatus(StatusTranca.EXCLUIDA);
        return trancaRepository.save(trancaExistente);

    }

    /* ---------- RETORNAR BICICLETA NA TRANCA ---------- */
    public Object retornarBicicletaNaTranca(Long idTranca) {
        Tranca tranca = trancaRepository.findById(idTranca)
                .orElseThrow(() -> new IllegalArgumentException(TRANCA_NAO_ENCONTRADO + idTranca));

        if (tranca.getBicicleta().getId() == null) {
            throw new IllegalArgumentException("Nenhuma bicicleta está presa nesta tranca.");
        }

        return tranca.getBicicleta().getId();
    }

    /* ---------- ALTERAR STATUS DA TRANCA ---------- */
    public Tranca alterarStatusDaTranca(Long idTranca, String acao) {
        Tranca tranca = trancaRepository.findById(idTranca)
                .orElseThrow(() -> new IllegalArgumentException(TRANCA_NAO_ENCONTRADO + idTranca));

        try {
            StatusTranca novoStatus = StatusTranca.valueOf(acao.toUpperCase());
            tranca.setStatus(novoStatus);
            return trancaRepository.save(tranca);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Status inválido: " + acao);
        }
    }

    /* funções usadas internamente na classe*/
    private Integer gerarNumeroAutomatico() {
        //função para adicionar um numero ao novo elemento cadastrado, se nao existir, será atribuido o numeor 1(primeiro),
        // se existir já bicicletas, pegará sua posição e adicionará mais um
        Integer maiorNumero = trancaRepository.findMaxNumero();
        return (maiorNumero == null) ? 1 : maiorNumero + 1;
    }


}
