package br.ufsm.csi.Salas.model;

import java.util.Date;

public class Agendamento {

    private Integer id;
    private Sala sala;
    private Funcionario funcionario;
    private Usuario usuario;
    private int status;
    private int turno;
    private Date data;

    public Agendamento() {
    }

    public Agendamento(Integer id, Sala sala, Funcionario funcionario, Usuario usuario, int status, int turno, Date data) {
        this.id = id;
        this.sala = sala;
        this.funcionario = funcionario;
        this.usuario = usuario;
        this.status = status;
        this.turno = turno;
        this.data = data;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTurno() {
        return turno;
    }

    public void setTurno(int turno) {
        this.turno = turno;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
}