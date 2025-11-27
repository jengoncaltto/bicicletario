package com.bikeunirio.bicicletario.equipamento.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class IntegrarTrancaRequestDTO {
    private Integer numeroTranca;
    private Long matriculaReparador;
    private Long idTotem;

    public IntegrarTrancaRequestDTO() {

    }

}
