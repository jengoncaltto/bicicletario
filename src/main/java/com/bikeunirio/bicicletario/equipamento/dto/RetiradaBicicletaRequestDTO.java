package com.bikeunirio.bicicletario.equipamento.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor // <--- Cria o construtor vazio automaticamente
@AllArgsConstructor
@Setter
@Getter
public class RetiradaBicicletaRequestDTO {
    private Long idTotem;
    private Long idTranca;
    private Long idFuncionario;       // matricula do reparador
    private String statusAcaoReparador; // "reparo" ou "aposentadoria"



}
