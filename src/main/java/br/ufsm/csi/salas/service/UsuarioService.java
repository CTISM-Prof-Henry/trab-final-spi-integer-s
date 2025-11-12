package br.ufsm.csi.salas.service;

import br.ufsm.csi.salas.dao.UsuarioDAO;
import br.ufsm.csi.salas.model.Usuario;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private static final UsuarioDAO dao = new UsuarioDAO();

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

    public List<Usuario> listar() {
        return dao.listar();
    }

    public Usuario buscar(int id) {
        return dao.buscar(id);
    }

}
