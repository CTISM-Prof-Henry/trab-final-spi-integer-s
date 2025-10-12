package br.ufsm.csi.Salas.Service;

import br.ufsm.csi.Salas.dao.FuncionarioDAO;
import br.ufsm.csi.Salas.model.Funcionario;

import java.util.ArrayList;

public class FuncionarioService {

    private static FuncionarioDAO dao = new FuncionarioDAO();

    public String inserir(Funcionario funcionario) {
        return dao.inserir(funcionario);
    }

    public String alterar(Funcionario funcionario) {
        return dao.alterar(funcionario);
    }

    public String excluir(int id) {

        if (dao.excluir(id)) {
            return "Sucesso ao excluir funcionario";
        } else {
            return "Erro ao excluir funcionario";
        }

    }

    public ArrayList<Funcionario> listar() {
        return dao.listar();
    }

    public Funcionario buscar(int id) {
        return dao.buscar(id);
    }

    public Funcionario buscar(String email) {
        return dao.buscar(email);
    }

}
