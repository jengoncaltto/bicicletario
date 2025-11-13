package com.bikeunirio.bicicletario.equipamento.service;

import com.bikeunirio.bicicletario.equipamento.entity.Bicicleta;
import com.bikeunirio.bicicletario.equipamento.entity.Tranca;
import com.bikeunirio.bicicletario.equipamento.enums.StatusBicicleta;
import com.bikeunirio.bicicletario.equipamento.enums.StatusTranca;
import com.bikeunirio.bicicletario.equipamento.repository.BicicletaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BicicletaService {
    private final BicicletaRepository bicicletaRepository;
    private final EmailService emailService;

    @Autowired
    public BicicletaService(BicicletaRepository bicicletaRepository, EmailService emailService) {
        this.bicicletaRepository = bicicletaRepository;
        this.emailService = emailService;
    }
    /* ---------- UC10: Manter Bicicleta ---------- */
    public List<Bicicleta> listarBicicletas() {
        return bicicletaRepository.findAll();
    }

    public Bicicleta cadastrarBicicleta(Bicicleta bicicleta) {

        // R2 - Todos os dados do formulário são obrigatórios.
        if (bicicleta.getMarca() == null || bicicleta.getMarca().isBlank())
            throw new IllegalArgumentException("Marca é obrigatória.");
        if (bicicleta.getModelo() == null || bicicleta.getModelo().isBlank())
            throw new IllegalArgumentException("Modelo é obrigatório.");
        if (bicicleta.getAno() == null || bicicleta.getAno().isBlank())
            throw new IllegalArgumentException("Ano é obrigatório.");

        // R5 – O número da bicicleta deve ser gerado pelo próprio sistema e não pode ser editado.
        Integer numeroGerado = gerarNumeroAutomatico();
        bicicleta.setNumero(numeroGerado);

        // R1 – Status inicial sempre NOVA
        bicicleta.setStatus(StatusBicicleta.NOVA);

        return bicicletaRepository.save(bicicleta);
    }

    public Bicicleta retornarBicicleta(Long idBicicleta) {
        if (idBicicleta == null) {
            throw new IllegalArgumentException("Um número é obrigatório.");
        } else if (idBicicleta < 0) {
            throw new IllegalArgumentException("Número negativo não é aceito.");
        }

        return bicicletaRepository.findById(idBicicleta)
                .orElseThrow(() -> new IllegalArgumentException("não encontrada."));
    }

    public Bicicleta editarBicicleta(Long idBicicleta, Bicicleta novosDados) {

        Bicicleta existente = retornarBicicleta(idBicicleta);
        // NÃO EDITA numero (R3)
        // NÃO EDITA status (R3)

        // Atualiza apenas campos permitidos
        existente.setMarca(novosDados.getMarca());
        existente.setModelo(novosDados.getModelo());
        existente.setAno(novosDados.getAno());

        return bicicletaRepository.save(existente);
    }

    public Bicicleta removerBicicleta(Long idBicicleta) {

        Bicicleta bike = bicicletaRepository.findById(idBicicleta)
                .orElseThrow(() -> new IllegalArgumentException("não encontrada: " + idBicicleta));

        // R4 — só excluir se aposentada e sem tranca
        if (bike.getStatus() != StatusBicicleta.APOSENTADA) {
            throw new IllegalArgumentException("Só é possível excluir bicicletas aposentadas.");
        }

        if (bike.getTranca() != null) {
            throw new IllegalArgumentException("Não é possível excluir bicicleta presa em tranca.");
        }

        bike.setStatus(StatusBicicleta.EXCLUIDA);
        return bicicletaRepository.save(bike);
    }

    /* ---------- UC08: Incluir Bicicleta na Rede de Totens ---------- */
    public String incluirBicicletaNaRede(Long idBicicleta) {
        Bicicleta bicicleta = retornarBicicleta(idBicicleta);

        if (bicicleta.getTranca() == null) {
            throw new IllegalArgumentException("Bicicleta não está associada a nenhuma tranca.");
        }

        Tranca tranca = bicicleta.getTranca();

        // [E3] Bicicleta em uso
        if (bicicleta.getStatus() == StatusBicicleta.EM_USO) {
            throw new IllegalArgumentException("A bicicleta está em uso e não pode ser incluída na rede.");
        }

        // [Pré-condição] status válido
        if (bicicleta.getStatus() != StatusBicicleta.NOVA &&
                bicicleta.getStatus() != StatusBicicleta.EM_REPARO) {
            throw new IllegalArgumentException("Bicicleta não está apta para inclusão (deve ser NOVA ou EM_REPARO).");
        }

        // [Pré-condição] tranca disponível
        if (tranca.getStatus() != StatusTranca.LIVRE) {
            throw new IllegalArgumentException("A tranca selecionada não está disponível.");
        }

        // [R1] Registrar inclusão
        bicicleta.setDataInsercao(LocalDateTime.now());
        bicicleta.setStatus(StatusBicicleta.DISPONIVEL);
        tranca.setStatus(StatusTranca.OCUPADA);
        tranca.setBicicleta(bicicleta);

        bicicletaRepository.save(bicicleta);

        // [R2] Notificação por email
        try {
            emailService.enviarEmail(
                    "reparador@empresa.com",
                    "Inclusão de Bicicleta na Rede",
                    String.format("Bicicleta %d incluída na tranca %d em %s.",
                            bicicleta.getNumero(),
                            tranca.getNumero(),
                            bicicleta.getDataInsercao())
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Falha ao enviar o e-mail de notificação.");
        }

        return "Bicicleta incluída com sucesso na rede de totens.";
    }

    public Bicicleta alterarStatusBicicleta(Long idBicicleta, StatusBicicleta acao) {
        Bicicleta atualizada = bicicletaRepository.findById(idBicicleta)
                .orElseThrow(() -> new IllegalArgumentException("não encontrada: " + idBicicleta));

        atualizada.setStatus(acao);
        return bicicletaRepository.save(atualizada);
    }

    /* funções usadas internamente na classe*/
    private Integer gerarNumeroAutomatico() {
        //função para adicionar um numero ao novo elemento cadastrado, se nao existir, será atribuido o numeor 1(primeiro),
        // se existir já bicicletas, pegará sua posição e adicionará mais um
        Integer maiorNumero = bicicletaRepository.findMaxNumero();
        return (maiorNumero == null) ? 1 : maiorNumero + 1;
    }
}
