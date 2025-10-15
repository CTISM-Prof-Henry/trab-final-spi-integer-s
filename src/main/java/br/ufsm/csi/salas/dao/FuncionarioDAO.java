package br.ufsm.csi.salas.dao;

import br.ufsm.csi.salas.model.Funcionario;

import java.sql.*;
import java.util.ArrayList;

public class FuncionarioDAO {

    public String inserir(Funcionario funcionario) {
        try (Connection conn = ConectarBanco.conectarBancoPostgres();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO funcionario (nome, email, cpf, permissao, senha) VALUES (?, ?, ?, ?, ?)"))
        {

            stmt.setString(1, funcionario.getNome());
            stmt.setString(2, funcionario.getEmail());
            stmt.setString(3, funcionario.getCpf());
            stmt.setInt(4, funcionario.getPermissao());
            stmt.setString(5, funcionario.getSenha());
            stmt.execute();

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            System.out.println("Erro ao inserir funcionário");
            return "Erro ao inserir funcionário!";
        }
        return "Funcionário inserido com sucesso!";
    }

    public ArrayList<Funcionario> listar() {
        ArrayList<Funcionario> funcionarios = new ArrayList<>();
        try (Connection conn = ConectarBanco.conectarBancoPostgres();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM funcionario ORDER BY nome"))
        {

            while (rs.next()) {
                Funcionario funcionario = new Funcionario();
                funcionario.setId(rs.getInt("id"));
                funcionario.setNome(rs.getString("nome"));
                funcionario.setEmail(rs.getString("email"));
                funcionario.setCpf(rs.getString("cpf"));
                funcionario.setSenha(rs.getString("senha"));

                funcionarios.add(funcionario);
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            System.out.println("Erro ao listar funcionarios");
        }
        return funcionarios;
    }

    public String alterar(Funcionario funcionario) {
        try (Connection conn = ConectarBanco.conectarBancoPostgres();
             PreparedStatement stmt = conn.prepareStatement("UPDATE funcionario SET nome = ?, email = ?, cpf = ?, senha = ? WHERE id = ?"))
        {

            stmt.setString(1, funcionario.getNome());
            stmt.setString(2, funcionario.getEmail());
            stmt.setString(3, funcionario.getCpf());
            stmt.setInt(4, funcionario.getId());
            stmt.setString(5, funcionario.getSenha());
            stmt.execute();

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            System.out.println("Erro ao alterar funcionário");
            return "Erro ao alterar funcionário!";
        }
        return "Funcionário alterado com sucesso!";
    }

    public boolean excluir(int id) {
        try (Connection conn = ConectarBanco.conectarBancoPostgres();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM funcionario WHERE id = ?"))
        {

            stmt.setInt(1, id);
            stmt.execute();

            if (stmt.getUpdateCount() <= 0) {
                return false;
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            System.out.println("Erro ao excluir funcionário");
            return false;
        }
        return true;
    }

    public Funcionario buscar(int id) {
        Funcionario funcionario = new Funcionario();
        try (Connection conn = ConectarBanco.conectarBancoPostgres();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM funcionario WHERE id = ?"))
        {

            stmt.setInt(1, id);
            resultSet(funcionario, stmt);
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            System.out.println("Erro ao buscar funcionário");
        }
        return funcionario;
    }

    private void resultSet(Funcionario funcionario, PreparedStatement stmt) throws SQLException {
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            funcionario.setId(rs.getInt("id"));
            funcionario.setNome(rs.getString("nome"));
            funcionario.setEmail(rs.getString("email"));
            funcionario.setCpf(rs.getString("cpf"));
            funcionario.setSenha(rs.getString("senha"));
        }
    }

    public Funcionario buscar(String email) {
        Funcionario funcionario = new Funcionario();
        try (Connection conn = ConectarBanco.conectarBancoPostgres();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM funcionario WHERE email = ?"))
        {

            stmt.setString(1, email);
            resultSet(funcionario, stmt);
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            System.out.println("Erro ao buscar funcionário");
        }
        return funcionario;
    }
}
