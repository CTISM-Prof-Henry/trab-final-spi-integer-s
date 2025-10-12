package br.ufsm.csi.Salas.Service;


import br.ufsm.csi.Salas.model.Funcionario;

public class LoginService {

    public Funcionario autenticar(String email, String senha) {

        Funcionario funcionario = new FuncionarioService().buscar(email);

        if(funcionario != null && funcionario.getSenha().equals(senha)) {
            return funcionario;
        } else {
            return null;
        }

    }

}