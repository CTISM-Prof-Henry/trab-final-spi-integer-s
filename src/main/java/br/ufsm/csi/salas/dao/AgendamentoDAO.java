package br.ufsm.csi.salas.dao;

import br.ufsm.csi.salas.model.Agendamento;
import br.ufsm.csi.salas.model.Funcionario;
import br.ufsm.csi.salas.model.Sala;
import br.ufsm.csi.salas.model.Usuario;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class AgendamentoDAO {

    public String inserir(Agendamento agendamento) {
        try
                (Connection conn = ConectarBanco.conectarBancoPostgres();
                 PreparedStatement stmt = conn.prepareStatement("INSERT INTO agendamento (idsala, idfunc, idusuario, status, turno, data, datacadastro) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
            dadosSQL(agendamento, stmt);

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            System.out.println("Erro ao inserir agendamento");
            return "Erro ao inserir agendamento!";
        }
        return "Agendamento inserido com sucesso!";
    }

    public ArrayList<Agendamento> listar() {
        ArrayList<Agendamento> agendamentos = new ArrayList<>();

        try (Connection conn = ConectarBanco.conectarBancoPostgres();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM agendamento ORDER BY data")) {

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
                agendamento.setDatacadastro(rs.getDate("datacadastro").toLocalDate());

                agendamentos.add(agendamento);
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            System.out.println("Erro ao listar agendamentos");
        }
        return agendamentos;
    }

    public String alterar(Agendamento agendamento) {
        try (Connection conn = ConectarBanco.conectarBancoPostgres();
             PreparedStatement stmt = conn.prepareStatement("UPDATE agendamento SET idsala = ?, idfunc = ?, " +
                     "idusuario = ?, status = ?, turno = ?, data = ?, datacadastro = ? WHERE id = ?")) {

            dadosSQL(agendamento, stmt);

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            System.out.println("Erro ao alterar Agendamento");
            return "Erro ao alterar Agendamento!";
        }
        return "Agendamento alterado com sucesso!";
    }

    private void dadosSQL(Agendamento agendamento, PreparedStatement stmt) throws SQLException {
        stmt.setInt(1, agendamento.getSala().getId());
        stmt.setInt(2, agendamento.getFuncionario().getId());
        stmt.setInt(3, agendamento.getUsuario().getId());
        stmt.setInt(4, agendamento.getStatus());
        stmt.setInt(5, agendamento.getTurno());
        stmt.setDate(6, Date.valueOf(agendamento.getData()));
        stmt.setDate(7, Date.valueOf(agendamento.getData()));
        stmt.execute();
    }

    public boolean excluir(int id) {
        try (Connection conn = ConectarBanco.conectarBancoPostgres();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM agendamento WHERE id = ?")) {

            stmt.setInt(1, id);
            stmt.execute();

            if (stmt.getUpdateCount() <= 0) {
                return false;
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            System.out.println("Erro ao excluir Agendamento");
            return false;
        }
        return true;
    }

    public ArrayList<Agendamento> buscarPorData(LocalDate data) {
        ArrayList<Agendamento> agendamentos = new ArrayList<>();

        try (Connection conn = ConectarBanco.conectarBancoPostgres();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM agendamento where data = ? order by data ")) {

            stmt.setDate(1, Date.valueOf(data));

            montaAgendamento(agendamentos, stmt);
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            System.out.println("Erro ao buscar agendamentos");
        }
        return agendamentos;
    }

    private void montaAgendamento(ArrayList<Agendamento> agendamentos, PreparedStatement stmt) throws SQLException {
        stmt.execute();

        while (stmt.getResultSet().next()) {
            SalaDAO salaDAO = new SalaDAO();
            Sala sala = salaDAO.buscar(stmt.getResultSet().getInt("idsala"));

            FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
            Funcionario funcionario = funcionarioDAO.buscar(stmt.getResultSet().getInt("idfunc"));

            UsuarioDAO usuarioDAO = new UsuarioDAO();
            Usuario usuario = usuarioDAO.buscar(stmt.getResultSet().getInt("idusuario"));

            Agendamento agendamento = new Agendamento();

            agendamento.setSala(sala);
            agendamento.setFuncionario(funcionario);
            agendamento.setUsuario(usuario);
            agendamento.setStatus(stmt.getResultSet().getInt("status"));
            agendamento.setTurno(stmt.getResultSet().getInt("turno"));
            agendamento.setData(stmt.getResultSet().getDate("data").toLocalDate());
            agendamento.setDatacadastro(stmt.getResultSet().getDate("datacadastro").toLocalDate());

            agendamentos.add(agendamento);
        }
    }


    public ArrayList<Agendamento> listarPorStatus(int i) {
        ArrayList<Agendamento> agendamentos = new ArrayList<>();

        try (Connection conn = ConectarBanco.conectarBancoPostgres();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM agendamento WHERE status = ? ORDER BY data")) {

            stmt.setInt(1, i);
            montaAgendamento(agendamentos, stmt);
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            System.out.println("Erro ao listar agendamentos");
        }
        return agendamentos;
    }
}
