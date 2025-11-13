package com.bikeunirio.bicicletario.equipamento.dto;

public class TrancaDTO {

    private String modelo;
    private String anoDeFabricacao;
    private Long totemId;       // ID do Totem ao qual a tranca pertence
    private Long bicicletaId;   // ID da Bicicleta que a tranca pode segurar (opcional)

    public TrancaDTO(String s, String s1, Object o) {
    }


    // Getters e Setters

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getAnoDeFabricacao() { return anoDeFabricacao; }
    public void setAnoDeFabricacao(String anoDeFabricacao) { this.anoDeFabricacao = anoDeFabricacao; }

    public Long getTotemId() { return totemId; }
    public void setTotemId(Long totemId) { this.totemId = totemId; }

    public Long getBicicletaId() { return bicicletaId; }
    public void setBicicletaId(Long bicicletaId) { this.bicicletaId = bicicletaId; }


}
