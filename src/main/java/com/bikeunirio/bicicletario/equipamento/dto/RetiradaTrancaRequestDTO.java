package com.bikeunirio.bicicletario.equipamento.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor // <--- Cria o construtor vazio automaticamente
@AllArgsConstructor
@Setter
@Getter
public class RetiradaTrancaRequestDTO {

    private Integer numeroTranca;
    private String operacao; // "reparo" ou "aposentadoria"
    private Long matriculaReparador;

}

