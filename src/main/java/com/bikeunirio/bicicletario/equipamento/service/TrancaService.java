package com.bikeunirio.bicicletario.equipamento.service;

import com.bikeunirio.bicicletario.equipamento.entity.Totem;
import com.bikeunirio.bicicletario.equipamento.entity.Tranca;
import com.bikeunirio.bicicletario.equipamento.enums.StatusTranca;
import com.bikeunirio.bicicletario.equipamento.repository.TrancaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class TrancaService {
    private static final String TRANCA_NAO_ENCONTRADO = "Tranca não encontrada: ";


    private final TrancaRepository trancaRepository;
    private final EmailService emailService;

    public TrancaService(TrancaRepository trancaRepository,  EmailService emailService) {
        this.trancaRepository = trancaRepository;
        this.emailService = emailService;
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
        Tranca existente = buscarPorId(idTranca);
        // NÃO EDITA numero (R3)
        // NÃO EDITA status (R3)

        existente.setModelo(novosDados.getModelo());
        existente.setAnoDeFabricacao(novosDados.getAnoDeFabricacao());

        return trancaRepository.save(existente);
    }

    /* ---------- EXCLUIR TRANCA ---------- */
    public Tranca removerTranca(Long idTranca) {
        //R4: Apenas trancas que não estiverem com nenhuma bicicleta podem ser excluídas.

        Tranca trancaExistente = buscarPorId(idTranca);
        if(trancaExistente.getBicicleta() != null){
            throw new IllegalArgumentException("Só é possível excluir tranca sem bicicleta.");

        }
        trancaExistente.setStatus(StatusTranca.EXCLUIDA);
        return trancaRepository.save(trancaExistente);

    }

    /* ---------- RETORNAR BICICLETA NA TRANCA ---------- */
    public Object retornarBicicletaNaTranca(Long idTranca) {
        Tranca tranca = buscarPorId(idTranca);
        if (tranca.getBicicleta() == null) {
            throw new IllegalArgumentException("Nenhuma bicicleta está presa nesta tranca.");
        }

        return tranca.getBicicleta().getId();
    }

    /* ---------- ALTERAR STATUS DA TRANCA ---------- */

    public Tranca alterarStatusDaTranca(Long idTranca, String acao) {
        Tranca tranca = buscarPorId(idTranca);
        try {
            StatusTranca novoStatus = StatusTranca.valueOf(acao.toUpperCase());
            tranca.setStatus(novoStatus);
            return trancaRepository.save(tranca);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Status inválido: " + acao);
        }
    }

    /*----------- DESTRANCA TRANCA -----------*/
    public Tranca trancar(Long idTranca) {
        Tranca tranca =  buscarPorId(idTranca);
        if (tranca.getStatus() == StatusTranca.OCUPADA) {
            throw new IllegalArgumentException("A tranca já está ocupada.");
        }

        tranca.setStatus(StatusTranca.OCUPADA);

        System.out.println("Tranca atualizada com sucesso!");
        return trancaRepository.save(tranca);
    }

    /*----------- TRANCAMENTO DA TRANCA --------*/
    public Tranca destrancar(Long idTranca) {
        Tranca tranca = buscarPorId(idTranca);

        if (tranca.getStatus() == StatusTranca.LIVRE) {
            throw new IllegalArgumentException("A tranca já está livre.");
        }

        tranca.setStatus(StatusTranca.LIVRE);

        return trancaRepository.save(tranca);
    }

    public Tranca integrarNaRede(Integer numeroTranca, Long matriculaReparador) {

        Tranca tranca = buscarPorNumero(numeroTranca);

        // Pré-condições
        if (tranca.getStatus() != StatusTranca.NOVA && tranca.getStatus() != StatusTranca.EM_REPARO) {
            throw new IllegalArgumentException("A tranca deve estar 'nova' ou 'em reparo' para ser integrada.");
        }

        // R3 – Se estava em reparo, verificar se é o mesmo reparador
        if (tranca.getStatus() == StatusTranca.EM_REPARO &&
                !Objects.equals(tranca.getMatriculaReparador(), matriculaReparador)) {

            throw new IllegalArgumentException("Apenas o reparador que retirou a tranca pode devolvê-la.");
        }

        // R1 – Registrar dados da inclusão
        tranca.setDataInsercao(LocalDateTime.now());
        tranca.setMatriculaReparador(matriculaReparador);

        // Altera status
        tranca.setStatus(StatusTranca.LIVRE);

        try {
            // R2 – Email
            emailService.enviarEmail(
                    "reparador@empresa.com",
                    "Inclusão de Tranca na Rede",
                    String.format(
                            "Tranca %d incluída na rede em %s pelo reparador %d.",
                            tranca.getNumero(),
                            tranca.getDataInsercao(),
                            matriculaReparador
                    )
            );
        } catch (Exception e) {
            // E2
            throw new IllegalArgumentException("Não foi possível enviar o email.");
        }

        return trancaRepository.save(tranca);
    }

    public String retirarTrancaDaRede(Integer numeroTranca, String operacao, Long matriculaReparador) {

        // 1 — Buscar tranca pelo número
        Tranca tranca = buscarPorNumero(numeroTranca);
        // 2 — Pré-condição: tranca não pode ter bicicleta presa
        if (tranca.getBicicleta() != null) {
            throw new IllegalArgumentException("A tranca possui bicicleta presa e não pode ser retirada.");
        }

        // 3 — A2: validar status reparo solicitado
        if (!tranca.getStatus().equals(StatusTranca.REPARO_SOLICITADO)) {
            throw new IllegalArgumentException("A tranca não está com reparo solicitado.");
        }

        // 4 — Liberação da tranca (passo 6)
        destrancar(tranca.getId());

        // 5 — Remover tranca do totem
        Totem totem = tranca.getTotem();
        if (totem != null) {
            totem.getTrancas().remove(tranca);
            tranca.setTotem(null);
        }

        // 6 — Ajustar status conforme operação
        if (operacao.equalsIgnoreCase("reparo")) {
            tranca.setStatus(StatusTranca.EM_REPARO);
        } else if (operacao.equalsIgnoreCase("aposentadoria")) {
            tranca.setStatus(StatusTranca.APOSENTADA);
        } else {
            throw new IllegalArgumentException("Operação inválida: use 'reparo' ou 'aposentadoria'.");
        }

        // 7 — Registrar retirada (R1)
        tranca.setDataInsercao(LocalDateTime.now());
        tranca.setMatriculaReparador(matriculaReparador);

        trancaRepository.save(tranca);

        // 8 — Enviar email (R2)
        try {
            emailService.enviarEmail(
                    "reparador@empresa.com",
                    "Retirada de Tranca da Rede",
                    String.format(
                            "A tranca %d foi retirada do totem às %s pelo reparador %d.",
                            tranca.getNumero(),
                            tranca.getDataInsercao(),
                            matriculaReparador
                    )
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Não foi possível enviar o email.");
        }

        // 9 — Mensagem final
        return "Tranca " + tranca.getNumero() + " retirada com sucesso.";
    }


    /* funções usadas internamente na classe*/
    protected Tranca buscarPorNumero(Integer numero) {
        if (numero == null) {
            throw new IllegalArgumentException("Número da tranca é obrigatório.");
        }

        return trancaRepository.findByNumero(numero)
                .orElseThrow(() -> new IllegalArgumentException("Tranca não encontrada."));
    }

    private Integer gerarNumeroAutomatico() {
        //função para adicionar um numero ao novo elemento cadastrado, se nao existir, será atribuido o numeor 1(primeiro),
        // se existir já bicicletas, pegará sua posição e adicionará mais um
        Integer maiorNumero = trancaRepository.findMaxNumero();
        return (maiorNumero == null) ? 1 : maiorNumero + 1;
    }
}
