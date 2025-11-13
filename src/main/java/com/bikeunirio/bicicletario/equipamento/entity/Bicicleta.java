package com.bikeunirio.bicicletario.equipamento.entity;

import com.bikeunirio.bicicletario.equipamento.enums.StatusBicicleta;
import jakarta.persistence.*;

import java.time.LocalDateTime;

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusBicicleta status;

    @Column(name = "data_insercao")
    private LocalDateTime dataInsercao;

    // Uma bicicleta está em uma tranca (opcional)
    @ManyToOne
    @JoinColumn(name = "tranca_id")
    private Tranca tranca;
    // Uma bicicleta pertence a um totem (opcional)

    @ManyToOne
    @JoinColumn(name = "totem_id")
    private Totem totem;


    public Bicicleta() {
        /**
         * Construtor padrão obrigatório pelo JPA/Hibernate.
         * Não deve ser removido ou modificado.
         */
    }

    public Long getId() { return id; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public String getAno() { return ano; }
    public void setAno(String ano) { this.ano = ano; }
    public Integer getNumero() { return numero; }
    public void setNumero(Integer numero) {this.numero = numero; }          //deve ser usado apenas internamente
    public void setStatus(StatusBicicleta status) {
        this.status = status;
    } //deve ser usado apenas internamente
    public StatusBicicleta getStatus() { return status; }
    public Tranca getTranca() { return tranca; }
    public void setTranca(Tranca tranca) { this.tranca = tranca; }
    public Totem getTotem() { return totem; }
    public void setTotem(Totem totem) { this.totem = totem; }
    public LocalDateTime getDataInsercao() {
        return dataInsercao;
    }
    public void setDataInsercao(LocalDateTime dataInsercao) {
        this.dataInsercao = dataInsercao;
    }
}
