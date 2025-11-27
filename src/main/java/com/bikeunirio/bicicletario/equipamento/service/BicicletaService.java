package com.bikeunirio.bicicletario.equipamento.service;

import com.bikeunirio.bicicletario.equipamento.entity.Bicicleta;
import com.bikeunirio.bicicletario.equipamento.entity.Tranca;
import com.bikeunirio.bicicletario.equipamento.enums.StatusBicicleta;
import com.bikeunirio.bicicletario.equipamento.enums.StatusTranca;
import com.bikeunirio.bicicletario.equipamento.repository.BicicletaRepository;
import com.bikeunirio.bicicletario.equipamento.repository.TrancaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class BicicletaService {
    private final BicicletaRepository bicicletaRepository;
    private final EmailService emailService;
    private final TrancaService trancaService;
    private final TrancaRepository trancaRepository;

    @Autowired
    public BicicletaService(BicicletaRepository bicicletaRepository, EmailService emailService, TrancaService trancaService, TrancaRepository trancaRepository) {
        this.bicicletaRepository = bicicletaRepository;
        this.emailService = emailService;
        this.trancaService = trancaService;
        this.trancaRepository = trancaRepository;
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

        Bicicleta bike = retornarBicicleta(idBicicleta);

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
    public String incluirBicicletaNaRede(Long idBicicleta, Long idTranca, Long matriculaReparador) {
        // 1. Buscar Bicicleta e Tranca (Correção do erro lógico)
        Bicicleta bicicleta = bicicletaRepository.findById(idBicicleta)
                .orElseThrow(() -> new IllegalArgumentException("Bicicleta não encontrada."));

        Tranca tranca = trancaRepository.findById(idTranca)
                .orElseThrow(() -> new IllegalArgumentException("Tranca não encontrada."));

        // [R3] Validação do reparador
        if (bicicleta.getStatus() == StatusBicicleta.EM_REPARO &&
                !Objects.equals(bicicleta.getMatriculaReparador(), matriculaReparador)) {
            throw new IllegalArgumentException("Apenas o reparador que retirou a bicicleta pode devolvê-la.");
        }

        // [E3] Validação Em Uso
        if (bicicleta.getStatus() == StatusBicicleta.EM_USO) {
            throw new IllegalArgumentException("A bicicleta está em uso e não pode ser incluída na rede.");
        }

        // [Pré-condição] Status bicicleta válido
        if (bicicleta.getStatus() != StatusBicicleta.NOVA && bicicleta.getStatus() != StatusBicicleta.EM_REPARO) {
            throw new IllegalArgumentException("Bicicleta não está apta para inclusão (deve ser NOVA ou EM_REPARO).");
        }

        // [Pré-condição] Tranca disponível (Verifica a tranca buscada pelo ID, não a da bicicleta)
        if (tranca.getStatus() != StatusTranca.LIVRE) {
            throw new IllegalArgumentException("A tranca selecionada não está disponível.");
        }

         trancaService.trancar(matriculaReparador);

        // [R1] Atualizar dados e fazer a associação
        bicicleta.setDataInsercao(LocalDateTime.now());
        bicicleta.setStatus(StatusBicicleta.DISPONIVEL);

        tranca.setStatus(StatusTranca.OCUPADA);
        tranca.setBicicleta(bicicleta); // Associa a bike na tranca
        bicicleta.setTranca(tranca);    // Associa a tranca na bike (importante manter consistência)

        bicicletaRepository.save(bicicleta);
        trancaRepository.save(tranca); // Salvar a tranca também é importante

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
        Bicicleta atualizada = retornarBicicleta(idBicicleta);
        atualizada.setStatus(acao);
        return bicicletaRepository.save(atualizada);
    }

    /*-----------------UC09*---------------*/
    public String retirarBicicletaDaRede(Integer numeroTranca, String operacao, Long idBicicleta) {

        // Recuperar bicicleta
        Bicicleta bicicleta = retornarBicicleta(idBicicleta);
        Tranca tranca = bicicleta.getTranca();

        if (tranca == null) {
            throw new IllegalArgumentException("A bicicleta não está presa a nenhuma tranca.");
        }
        // ------------- [E1] Número da tranca inválido -----------------------
        if (!numeroTranca.equals(tranca.getNumero())) {
            throw new IllegalArgumentException("Número da tranca inválido.");
        }
        // ------------- [A2] Verificações de falha do UC -----------------------
        if (bicicleta.getStatus() == StatusBicicleta.DISPONIVEL) {
            throw new IllegalArgumentException("A bicicleta está disponível — não pode ser retirada.");
        }
        if (tranca.getStatus() == StatusTranca.LIVRE) {
            throw new IllegalArgumentException("A tranca está livre — não há bicicleta para retirar.");
        }


        // A2.4 Abrir tranca
        trancaService.destrancar(tranca.getId());

        // A2.5 Retirar bicicleta
        tranca.setBicicleta(null);

        // A2.6 Ajustar status da bicicleta conforme operação
        if (operacao.equalsIgnoreCase("aposentadoria")) {
            bicicleta.setStatus(StatusBicicleta.APOSENTADA); // A1
        } else if (operacao.equalsIgnoreCase("reparo")) {
            bicicleta.setStatus(StatusBicicleta.EM_REPARO); // fluxo principal
        } else {
            throw new IllegalArgumentException("Operação inválida. Use 'reparo' ou 'aposentadoria'.");
        }

        // Atualizar banco
        trancaRepository.save(tranca);
        bicicletaRepository.save(bicicleta);

        // A2.7 Registrar retirada
        bicicleta.setDataInsercao(LocalDateTime.now());

        // A2.8 Enviar e-mail
        emailService.enviarEmail(
                "reparador@empresa.com",
                "Inclusão de Bicicleta na Rede",
                String.format("Bicicleta %d incluída na tranca %d em %s.",
                        bicicleta.getNumero(),
                        tranca.getNumero(),
                        bicicleta.getDataInsercao())
        );

        // A2.9 Mensagem final
        return "Bicicleta " + bicicleta.getId() + " retirada com sucesso.";
    }

    /* funções usadas internamente na classe*/
    private Integer gerarNumeroAutomatico() {
        //função para adicionar um numero ao novo elemento cadastrado, se nao existir, será atribuido o numeor 1(primeiro),
        // se existir já bicicletas, pegará sua posição e adicionará mais um
        Integer maiorNumero = bicicletaRepository.findMaxNumero();
        return (maiorNumero == null) ? 1 : maiorNumero + 1;
    }

    protected Bicicleta buscarPorNumero(Integer numero) {
        if (numero == null) {
            throw new IllegalArgumentException("Número da bicicleta é obrigatório.");
        }

        return bicicletaRepository.findByNumero(numero)
                .orElseThrow(() -> new IllegalArgumentException("Bicicleta não encontrada."));
    }
}
