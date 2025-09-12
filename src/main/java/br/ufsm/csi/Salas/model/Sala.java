package br.ufsm.csi.Salas.model;

public class Sala {

    private Integer id;
    private String tipo;
    private int capacidade;
    private String bloco;

    public Sala() {
    }

    public Sala(Integer id, String tipo, int capacidade, String bloco) {
        this.id = id;
        this.tipo = tipo;
        this.capacidade = capacidade;
        this.bloco = bloco;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    public String getBloco() {
        return bloco;
    }

    public void setBloco(String bloco) {
        this.bloco = bloco;
    }
}