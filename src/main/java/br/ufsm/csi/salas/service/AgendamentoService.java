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

    public boolean verificarDisponibilidade(Integer salaId, Integer turno, LocalDate data) {
        List<Agendamento> agendamentos = listar();
        return agendamentos.stream()
                .noneMatch(a ->
                        a.getSala() != null &&
                                a.getSala().getId().equals(salaId) &&
                                a.getTurno() == turno &&
                                a.getData() != null &&
                                a.getData().equals(data) &&
                                a.getStatus() != 3 // não considerar finalizados
                );
    }

    public boolean verificarDisponibilidadeEdicao(Integer salaId, Integer turno, LocalDate data, Integer agendamentoId) {
        List<Agendamento> agendamentos = listar();
        return agendamentos.stream()
                .noneMatch(a ->
                        a.getSala() != null &&
                                a.getSala().getId().equals(salaId) &&
                                a.getTurno() == turno &&
                                a.getData() != null &&
                                a.getData().equals(data) &&
                                !a.getId().equals(agendamentoId) && // ignora o próprio
                                a.getStatus() != 3 // ignora finalizados
                );
    }
}
