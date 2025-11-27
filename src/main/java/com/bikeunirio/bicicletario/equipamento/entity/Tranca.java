package com.bikeunirio.bicicletario.equipamento.entity;

import com.bikeunirio.bicicletario.equipamento.enums.StatusTranca;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "tranca")
@Data
public class Tranca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private Integer numero;

    private String modelo;

    @Column(name = "ano_de_fabricacao")
    private String anoDeFabricacao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusTranca status;

    // Uma tranca pertence a um totem
    @ManyToOne
    @JoinColumn(name = "totem_id")
    @JsonIgnore
    private Totem totem;


    // Uma tranca pode estar segurando uma bicicleta
    @OneToOne
    @JoinColumn(name = "bicicleta_id")
    private Bicicleta bicicleta;

    @Column(name = "data_insercao")
    private LocalDateTime dataInsercao;
    @Column(name = "matricula_reparador")
    private Long matriculaReparador;


    public Tranca() {
        /**
         * Construtor padrão obrigatório pelo JPA/Hibernate.
         * Não deve ser removido ou modificado.
         */
    }



}
