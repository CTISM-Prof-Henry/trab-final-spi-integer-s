package br.ufsm.csi.Salas.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Funcionario {

    private Integer id;
    private String nome;
    private String email;
    private String cpf;
    private String senha;

}