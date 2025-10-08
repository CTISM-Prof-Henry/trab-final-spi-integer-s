package br.ufsm.csi.Salas.Service;

import br.ufsm.csi.Salas.dao.SalaDAO;
import br.ufsm.csi.Salas.model.Sala;

import java.util.ArrayList;

public class SalaService {

    private static SalaDAO dao = new SalaDAO();

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

    public ArrayList<Sala> listar() {
        return dao.listar();
    }

    public Sala buscar(int id) {
        return dao.buscar(id);
    }

}
