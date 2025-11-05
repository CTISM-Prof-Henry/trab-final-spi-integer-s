package br.ufsm.csi.salas.service;

import br.ufsm.csi.salas.dao.AgendamentoDAO;
import br.ufsm.csi.salas.model.Agendamento;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AgendamentoService {

    private final AgendamentoDAO dao;

    public AgendamentoService() {
        this.dao = new AgendamentoDAO();
    }

    public String inserir(Agendamento agendamento) {
        return dao.inserir(agendamento);
    }

    public String alterar(Agendamento agendamento) {
        return dao.alterar(agendamento);
    }

    public String excluir(int id) {
        if (dao.excluir(id)) {
            return "Agendamento excluído com sucesso";
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

    public List<Agendamento> listarPorStatus(int status) {
        return dao.listarPorStatus(status);
    }

    public Agendamento buscarPorId(int id) {
        return dao.buscar(id);
    }
}
