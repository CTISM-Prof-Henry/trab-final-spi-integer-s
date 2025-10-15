package br.ufsm.csi.salas.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sala {

    private Integer id;
    private String tipo;
    private int capacidade;
    private String bloco;

}