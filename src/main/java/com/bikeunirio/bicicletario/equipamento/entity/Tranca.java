package com.bikeunirio.bicicletario.equipamento.entity;

import com.bikeunirio.bicicletario.equipamento.enums.StatusTranca;
import jakarta.persistence.*;

@Entity
@Table(name = "tranca")
public class Tranca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private Integer numero;

    @Column(name = "bicicleta_id")
    private Long bicicletaId;

    private String localizacao;

    @Column(name = "ano_de_fabricacao", nullable = false)
    private String anoDeFabricacao;

    @Column(nullable = false)
    private String modelo;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusTranca status;

    public Long getId() {
        return id;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Long getBicicletaId() {
        return bicicletaId;
    }

    public void setBicicletaId(Long bicicletaId) {
        this.bicicletaId = bicicletaId;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public String getAnoDeFabricacao() {
        return anoDeFabricacao;
    }

    public void setAnoDeFabricacao(String anoDeFabricacao) {
        this.anoDeFabricacao = anoDeFabricacao;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public StatusTranca getStatus() {
        return status;
    }

    public void setStatus(StatusTranca status) {
        this.status = status;
    }
}
