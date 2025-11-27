package com.bikeunirio.bicicletario.equipamento.entity;

import com.bikeunirio.bicicletario.equipamento.enums.StatusBicicleta;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "bicicleta")
@Data
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
    @Column(name = "matricula_reparador")
    private Long matriculaReparador;


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

}
