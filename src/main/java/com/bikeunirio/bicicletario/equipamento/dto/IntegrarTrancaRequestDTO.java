package com.bikeunirio.bicicletario.equipamento.dto;

public class IntegrarTrancaRequestDTO {
    private Integer numeroTranca;
    private Long matriculaReparador;

    public IntegrarTrancaRequestDTO() {

    }

    public Integer getNumeroTranca() {
        return numeroTranca;
    }

    public void setNumeroTranca(Integer numeroTranca) {
        this.numeroTranca = numeroTranca;
    }

    public Long getMatriculaReparador() { return matriculaReparador; }
    public void setMatriculaReparador(Long matriculaReparador) { this.matriculaReparador = matriculaReparador; }
}
