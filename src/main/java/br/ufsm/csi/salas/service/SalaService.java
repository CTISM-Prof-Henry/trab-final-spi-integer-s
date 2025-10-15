package br.ufsm.csi.salas.service;

import br.ufsm.csi.salas.dao.SalaDAO;
import br.ufsm.csi.salas.model.Sala;

import java.util.List;

public class SalaService {

    private static SalaDAO dao = new SalaDAO();

    public SalaService() {
        dao = new SalaDAO();
    }

    public SalaService(SalaDAO dao) {
        SalaService.dao = dao;
    }

    public String inserir(Sala sala) {
        return dao.inserir(sala);
    }

    public String alterar(Sala sala) {
        return dao.alterar(sala);
    }

    public String excluir(int id) {

        if (dao.excluir(id)) {
            return "Sucesso ao excluir sala";
        } else {
            return "Erro ao excluir sala";
        }

    }

    public List<Sala> listar() {
        return dao.listar();
    }

    public Sala buscar(int id) {
        return dao.buscar(id);
    }

}
