package com.bikeunirio.bicicletario.equipamento.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "totem")
public class Totem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String localizacao;

    @Column(nullable = true)
    private String descricao;

    // Um totem possui várias bicicletas
    @OneToMany(mappedBy = "totem", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Bicicleta> bicicletas;

    // Um totem possui várias trancas
    @OneToMany(mappedBy = "totem", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Tranca> trancas;

    public Totem() {
        /**
         * Construtor padrão obrigatório pelo JPA/Hibernate.
         * Não deve ser removido ou modificado.
         */
    }

}
