package com.bikeunirio.bicicletario.equipamento.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TrancaDTO {

    private String modelo;
    private String anoDeFabricacao;
    private Long totemId;       // ID do Totem ao qual a tranca pertence
    private Long bicicletaId;   // ID da Bicicleta que a tranca pode segurar (opcional)

    public TrancaDTO(String s, String s1, Object o) {
    }


    // Getters e Setters


}
