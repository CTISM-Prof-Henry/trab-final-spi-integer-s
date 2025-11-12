package br.ufsm.csi.salas.dao;

import br.ufsm.csi.salas.model.*;

import java.sql.*;
import java.util.ArrayList;

public class SalaDAO {

    public String inserir(Sala sala) {
        try (Connection conn = ConectarBanco.conectarBancoPostgres();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO sala (tipo, capacidade, bloco) VALUES (?, ?, ?)"))
        {

            stmt.setString(1, sala.getTipo());
            stmt.setInt(2, sala.getCapacidade());
            stmt.setString(3, sala.getBloco());
            stmt.execute();

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            System.out.println("Erro ao inserir sala");
            return "Erro ao inserir sala!";
        }
        return "Sala inserida com sucesso!";
    }

    public ArrayList<Sala> listar() {
        ArrayList<Sala> salas = new ArrayList<>();

        try (Connection conn = ConectarBanco.conectarBancoPostgres();
             Statement stmt = conn.createStatement())
        {

            ResultSet rs = stmt.executeQuery("SELECT * FROM sala ORDER BY tipo");

            while (rs.next()) {
                Sala sala = new Sala();
                sala.setId(rs.getInt("id"));
                sala.setTipo(rs.getString("tipo"));
                sala.setCapacidade(rs.getInt("capacidade"));
                sala.setBloco(rs.getString("bloco"));

                salas.add(sala);
            }

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            System.out.println("Erro ao listar salas");
        }
        return salas;
    }

    public String alterar(Sala sala) {
        try (Connection conn = ConectarBanco.conectarBancoPostgres();
             PreparedStatement stmt = conn.prepareStatement("UPDATE sala SET tipo = ?, capacidade = ?, bloco = ? WHERE id = ?"))
        {

            stmt.setString(1, sala.getTipo());
            stmt.setInt(2, sala.getCapacidade());
            stmt.setString(3, sala.getBloco());
            stmt.setInt(4, sala.getId());
            stmt.execute();

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            System.out.println("Erro ao alterar sala");
            return "Erro ao alterar sala!";
        }
        return "Sala alterada com sucesso!";
    }

    public boolean excluir(int id) {
        try (Connection conn = ConectarBanco.conectarBancoPostgres();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM sala WHERE id = ?"))
        {

            stmt.setInt(1, id);
            stmt.execute();

            if (stmt.getUpdateCount() <= 0) {
                return false;
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            System.out.println("Erro ao excluir sala");
            return false;
        }
        return true;
    }

    public Sala buscar(int id) {
        Sala sala = new Sala();

        try (Connection conn = ConectarBanco.conectarBancoPostgres();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM sala WHERE id = ?"))
        {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                sala.setId(rs.getInt("id"));
                sala.setTipo(rs.getString("tipo"));
                sala.setCapacidade(rs.getInt("capacidade"));
                sala.setBloco(rs.getString("bloco"));
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            System.out.println("Erro ao buscar sala");
        }
        return sala;
    }
}
