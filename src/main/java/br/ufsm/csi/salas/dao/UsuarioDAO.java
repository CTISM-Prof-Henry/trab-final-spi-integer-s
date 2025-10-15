package br.ufsm.csi.salas.dao;

import br.ufsm.csi.salas.model.Usuario;

import java.sql.*;
import java.util.ArrayList;

public class UsuarioDAO {

    public String inserir(Usuario usuario) {
        try (Connection conn = ConectarBanco.conectarBancoPostgres();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO usuario (nome, email, matricula) VALUES (?, ?, ?)"))
        {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getMatricula());
            stmt.execute();

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            System.out.println("Erro ao inserir usuário");
            return "Erro ao inserir usuário!";
        }
        return "Usuário inserido com sucesso!";
    }

    public ArrayList<Usuario> listar() {
        ArrayList<Usuario> usuarios = new ArrayList<>();

        try (Connection conn = ConectarBanco.conectarBancoPostgres();
             Statement stmt = conn.createStatement())
        {

            ResultSet rs = stmt.executeQuery("SELECT * FROM usuario ORDER BY nome");

            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setEmail(rs.getString("email"));
                usuario.setMatricula(rs.getString("matricula"));

                usuarios.add(usuario);
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            System.out.println("Erro ao listar usuarios");
        }
        return usuarios;
    }

    public String alterar(Usuario usuario) {
        try (Connection conn = ConectarBanco.conectarBancoPostgres();
             PreparedStatement stmt = conn.prepareStatement("UPDATE usuario SET nome = ?, email = ?, matricula = ? WHERE id = ?"))
        {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getMatricula());
            stmt.setInt(4, usuario.getId());
            stmt.execute();

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            System.out.println("Erro ao alterar usuário");
            return "Erro ao alterar usuário!";
        }
        return "Usuário alterado com sucesso!";
    }

    public boolean excluir(int id) {
        try (Connection conn = ConectarBanco.conectarBancoPostgres();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM usuario WHERE id = ?"))
        {

            stmt.setInt(1, id);
            stmt.execute();

            if (stmt.getUpdateCount() <= 0) {
                return false;
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            System.out.println("Erro ao excluir usuário");
            return false;
        }
        return true;
    }

    public Usuario buscar(int id) {
        Usuario usuario = new Usuario();

        try (Connection conn = ConectarBanco.conectarBancoPostgres();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM usuario WHERE id = ?"))
        {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                usuario.setId(rs.getInt("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setEmail(rs.getString("email"));
                usuario.setMatricula(rs.getString("matricula"));
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            System.out.println("Erro ao buscar usuário");
        }
        return usuario;
    }
}