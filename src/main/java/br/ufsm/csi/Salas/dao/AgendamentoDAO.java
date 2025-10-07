package br.ufsm.csi.Salas.dao;

import br.ufsm.csi.Salas.model.Agendamento;
import br.ufsm.csi.Salas.model.Funcionario;
import br.ufsm.csi.Salas.model.Sala;
import br.ufsm.csi.Salas.model.Usuario;

import java.sql.*;
import java.util.ArrayList;

public class AgendamentoDAO {

    public String inserir(Agendamento agendamento) {
        try {
            Connection conn = ConectarBanco.conectarBancoPostgres();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO agendamento (idsala, idfunc, idusuario, status, turno, data) VALUES (?, ?, ?, ?, ?, ?)");

            stmt.setInt(1, agendamento.getSala().getId());
            stmt.setInt(2, agendamento.getFuncionario().getId());
            stmt.setInt(3, agendamento.getUsuario().getId());
            stmt.setInt(4, agendamento.getStatus());
            stmt.setInt(5, agendamento.getTurno());
            stmt.setDate(6, Date.valueOf(agendamento.getData()));
            stmt.execute();

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            System.out.println("Erro ao inserir agendamento");
            return "Erro ao inserir agendamento!";
        }
        return "Agendamento inserido com sucesso!";
    }

    public ArrayList<Agendamento> listar() {
        ArrayList<Agendamento> agendamentos = new ArrayList<>();
        try {
            Connection conn = ConectarBanco.conectarBancoPostgres();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM agendamento ORDER BY data");

            while (rs.next()) {
                SalaDAO salaDAO = new SalaDAO();
                Sala sala = salaDAO.buscar(rs.getInt("idsala"));

                FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
                Funcionario funcionario = funcionarioDAO.buscar(rs.getInt("idfunc"));

                UsuarioDAO usuarioDAO = new UsuarioDAO();
                Usuario usuario = usuarioDAO.buscar(rs.getInt("idusuario"));

                Agendamento agendamento = new Agendamento();

                agendamento.setSala(sala);
                agendamento.setFuncionario(funcionario);
                agendamento.setUsuario(usuario);
                agendamento.setStatus(rs.getInt("status"));
                agendamento.setTurno(rs.getInt("turno"));
                agendamento.setData(rs.getDate("data").toLocalDate());

                agendamentos.add(agendamento);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao conectar");
            e.printStackTrace();
        } catch (ClassNotFoundException ex) {
            System.out.println("Driver não carregou");
            ex.printStackTrace();
        }
        return agendamentos;
    }

    public String alterar(Agendamento agendamento) {
        try {
            Connection conn = ConectarBanco.conectarBancoPostgres();
            PreparedStatement stmt = conn.prepareStatement("UPDATE funcionario SET nome = ?, email = ?, cpf = ? WHERE id = ?");

            stmt.setString(1, funcionario.getNome());
            stmt.setString(2, funcionario.getEmail());
            stmt.setString(3, funcionario.getCpf());
            stmt.setInt(4, funcionario.getId());
            stmt.execute();

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            System.out.println("Erro ao alterar funcionário");
            return "Erro ao alterar funcionário!";
        }
        return "Funcionário alterado com sucesso!";
    }


}
