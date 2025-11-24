package com.bikeunirio.bicicletario.equipamento.dto;

public class RetiradaTrancaRequestDTO {

    private Integer numeroTranca;
    private String operacao; // "reparo" ou "aposentadoria"
    private Long matriculaReparador;

    public Integer getNumeroTranca() {
        return numeroTranca;
    }

    public void setNumeroTranca(Integer numeroTranca) {
        this.numeroTranca = numeroTranca;
    }

    public String getOperacao() {
        return operacao;
    }

    public void setOperacao(String operacao) {
        this.operacao = operacao;
    }

    public Long getMatriculaReparador() {
        return matriculaReparador;
    }

    public void setMatriculaReparador(Long matriculaReparador) {
        this.matriculaReparador = matriculaReparador;
    }
}

