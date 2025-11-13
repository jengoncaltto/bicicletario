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

    @Column(nullable = false)
    private String modelo;

    @Column(name = "ano_de_fabricacao", nullable = false)
    private String anoDeFabricacao;

    private String localizacao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusTranca status;

    // Uma tranca pertence a um totem
    @ManyToOne
    @JoinColumn(name = "totem_id")
    private Totem totem;

    // Uma tranca pode estar segurando uma bicicleta
    @OneToOne
    @JoinColumn(name = "bicicleta_id")
    private Bicicleta bicicleta;

    public Tranca(Long id) {
        this.id = id;
    }

    // --- Getters e Setters ---
    public Long getId() { return id; }
    public Integer getNumero() { return numero; }
    public void setNumero(Integer numero) { this.numero = numero; }
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public String getAnoDeFabricacao() { return anoDeFabricacao; }
    public void setAnoDeFabricacao(String anoDeFabricacao) { this.anoDeFabricacao = anoDeFabricacao; }
    public String getLocalizacao() { return localizacao; }
    public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }
    public StatusTranca getStatus() { return status; }
    public void setStatus(StatusTranca status) { this.status = status; }
    public Totem getTotem() { return totem; }
    public void setTotem(Totem totem) { this.totem = totem; }
    public Bicicleta getBicicleta() { return bicicleta; }
    public void setBicicleta(Bicicleta bicicleta) { this.bicicleta = bicicleta; }
}
