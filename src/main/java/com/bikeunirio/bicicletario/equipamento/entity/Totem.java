package com.bikeunirio.bicicletario.equipamento.entity;

import com.bikeunirio.bicicletario.equipamento.enums.StatusTotem;
import jakarta.persistence.*;

@Entity
@Table(name = "totem")
public class Totem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Mudança para Long

    @Column(nullable = false)
    private String localizacao;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusTotem status; // Novo campo de status



    public Long getId() {
        return id;
    }

    public StatusTotem getStatus() {
        return status;
    }

    // Construtor padrão (obrigatório para JPA)
    public Totem() {
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public void setStatus(StatusTotem status) {
        this.status = status;
    }
}
