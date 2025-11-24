package com.bikeunirio.bicicletario.equipamento.dto;

public class RetiradaBicicletaRequestDTO {
    private Integer numeroTranca;
    private String statusAcaoReparador;     // "reparo" ou "aposentadoria"
    private Long idBicicleta;

    public RetiradaBicicletaRequestDTO() {
    }

    public Integer getNumeroTranca() {
        return numeroTranca;
    }

    public void setNumeroTranca(Integer numeroTranca) {
        this.numeroTranca = numeroTranca;
    }

    public String getStatusAcaoReparador() {
        return statusAcaoReparador;
    }

    public void setStatusAcaoReparador(String statusAcaoReparador) {
        this.statusAcaoReparador = statusAcaoReparador;
    }

    public Long getIdBicicleta() {
        return idBicicleta;
    }

    public void setIdBicicleta(Long idBicicleta) {
        this.idBicicleta = idBicicleta;
    }

}
