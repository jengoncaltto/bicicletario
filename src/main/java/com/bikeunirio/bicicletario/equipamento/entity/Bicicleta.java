package com.bikeunirio.bicicletario.equipamento.entity;

import com.bikeunirio.bicicletario.equipamento.enums.StatusBicicleta;
import jakarta.persistence.*;

@Entity
@Table(name = "bicicleta")
public class Bicicleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String marca;

    @Column(nullable = false)
    private String modelo;

    @Column(nullable = false)
    private String ano;

    @Column(unique = true, nullable = false)
    private Integer numero;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusBicicleta status;

    @Column(name = "tranca_id")
    private Long trancaId;

    @Column(name = "totem_id")
    private Long totemId;

    public Bicicleta() {}

    public Long getId() {
        return id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public StatusBicicleta getStatus() {
        return status;
    }

    public void setStatus(StatusBicicleta status) {
        this.status = status;
    }

    public Long getTrancaId() {
        return trancaId;
    }

    public void setTrancaId(Long trancaId) {
        this.trancaId = trancaId;
    }

    public Long getTotemId() {
        return totemId;
    }

    public void setTotemId(Long totemId) {
        this.totemId = totemId;
    }

}