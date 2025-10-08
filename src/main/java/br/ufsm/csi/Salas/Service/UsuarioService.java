package br.ufsm.csi.Salas.Service;

import br.ufsm.csi.Salas.dao.UsuarioDAO;
import br.ufsm.csi.Salas.model.Usuario;

import java.util.ArrayList;

public class UsuarioService {

    private static UsuarioDAO dao = new UsuarioDAO();

    public String inserir(Usuario usuario) {
        return dao.inserir(usuario);
    }

    public String alterar(Usuario usuario) {
        return dao.alterar(usuario);
    }

    public String excluir(int id) {

        if (dao.excluir(id)) {
            return "Sucesso ao excluir usuario";
        } else {
            return "Erro ao excluir usuario";
        }

    }

    public ArrayList<Usuario> listar() {
        return dao.listar();
    }

    public Usuario buscar(int id) {
        return dao.buscar(id);
    }

}
