package com.bikeunirio.bicicletario.equipamento.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RetiradaTrancaRequestDTO {

    private Integer numeroTranca;
    private String operacao; // "reparo" ou "aposentadoria"
    private Long matriculaReparador;

}

