package com.bikeunirio.bicicletario.equipamento.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RetiradaBicicletaRequestDTO {
    private Integer numeroTranca;
    private String statusAcaoReparador;     // "reparo" ou "aposentadoria"
    private Long idBicicleta;

    public RetiradaBicicletaRequestDTO() {
    }

}
