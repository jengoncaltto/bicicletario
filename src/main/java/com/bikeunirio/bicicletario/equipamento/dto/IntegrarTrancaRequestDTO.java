package com.bikeunirio.bicicletario.equipamento.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor // <--- Cria o construtor vazio automaticamente
@AllArgsConstructor
@Setter
@Getter
public class IntegrarTrancaRequestDTO {
    private Integer numeroTranca;
    private Long matriculaReparador;
    private Long idTotem;



}
