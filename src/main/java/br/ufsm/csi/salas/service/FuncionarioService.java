package br.ufsm.csi.salas.service;

import br.ufsm.csi.salas.dao.FuncionarioDAO;
import br.ufsm.csi.salas.model.Funcionario;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class FuncionarioService {

    private static FuncionarioDAO dao = new FuncionarioDAO();

    public FuncionarioService() {
        dao = new FuncionarioDAO();
    }

    public FuncionarioService(FuncionarioDAO dao) {
        FuncionarioService.dao = dao;
    }

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
