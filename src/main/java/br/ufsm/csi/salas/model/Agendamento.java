package br.ufsm.csi.salas.model;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Agendamento {

    private Integer id;
    private Sala sala;
    private Funcionario funcionario;
    private Usuario usuario;
    private int status;
    private int turno;
    private LocalDate data;
    private LocalDate datacadastro;

}