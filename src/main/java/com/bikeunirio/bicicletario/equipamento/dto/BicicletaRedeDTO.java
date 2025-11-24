package com.bikeunirio.bicicletario.equipamento.dto;

public  class BicicletaRedeDTO {
    private Integer numeroBicicleta;

    private Long matriculaReparador;

    public Integer getNumeroBicicleta() {
        return numeroBicicleta;
    }

    public void setNumeroBicicleta(Integer numeroBicicleta) {
        this.numeroBicicleta = numeroBicicleta;
    }

    public Long getMatriculaReparador() {
        return matriculaReparador;
    }

    public void setMatriculaReparador(Long matriculaReparador) {
        this.matriculaReparador = matriculaReparador;
    }
}