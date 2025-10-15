package br.ufsm.csi.salas.service;

import br.ufsm.csi.salas.dao.AgendamentoDAO;
import br.ufsm.csi.salas.model.Agendamento;

import java.time.LocalDate;
import java.util.List;

public class AgendamentoService {
    private static AgendamentoDAO dao = new AgendamentoDAO();

    public AgendamentoService() {
        dao = new  AgendamentoDAO();
    }

    public AgendamentoService(AgendamentoDAO dao) {
        AgendamentoService.dao = dao;
    }

    public String inserir(Agendamento agendamento) {
        return dao.inserir(agendamento);
    }

    public String alterar(Agendamento agendamento) {
        return dao.alterar(agendamento);
    }

    public String excluir(int id) {
        if (dao.excluir(id)) {
            return "Sucesso ao excluir agendamento";
        } else {
            return "Erro ao excluir agendamento";
        }
    }

    public List<Agendamento> listar() {
        return dao.listar();
    }

    public List<Agendamento> buscarPorData(LocalDate data) {
        return dao.buscarPorData(data);
    }

    public List<Agendamento> listarPorStatus(int i) {
        return dao.listarPorStatus(i);
    }
}
