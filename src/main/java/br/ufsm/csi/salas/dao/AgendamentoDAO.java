package br.ufsm.csi.salas.dao;

import br.ufsm.csi.salas.model.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AgendamentoDAO {

    public String inserir(Agendamento agendamento) {
        String sql = """
            INSERT INTO agendamento (idsala, idfunc, idusuario, status, turno, data, datacadastro)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = ConectarBanco.conectarBancoPostgres();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            preencherStatement(agendamento, stmt, false);
            stmt.executeUpdate();
            return "Agendamento inserido com sucesso!";

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return "Erro ao inserir agendamento!";
        }
    }

    public String alterar(Agendamento agendamento) {
        String sql = """
            UPDATE agendamento
            SET idsala = ?, idfunc = ?, idusuario = ?, status = ?, turno = ?, data = ?, datacadastro = ?
            WHERE id = ?
        """;

        try (Connection conn = ConectarBanco.conectarBancoPostgres();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            preencherStatement(agendamento, stmt, true);
            stmt.executeUpdate();
            return "Agendamento alterado com sucesso!";

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return "Erro ao alterar agendamento!";
        }
    }

    public boolean excluir(int id) {
        String sql = "DELETE FROM agendamento WHERE id = ?";

        try (Connection conn = ConectarBanco.conectarBancoPostgres();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Agendamento> listar() {
        List<Agendamento> agendamentos = new ArrayList<>();
        String sql = """
            SELECT a.*, 
                   s.id AS sala_id, s.tipo AS sala_tipo, s.capacidade AS sala_capacidade, s.bloco AS sala_bloco,
                   f.id AS func_id, f.nome AS func_nome,
                   u.id AS usuario_id, u.nome AS usuario_nome
            FROM agendamento a
            INNER JOIN sala s ON a.idsala = s.id
            INNER JOIN funcionario f ON a.idfunc = f.id
            INNER JOIN usuario u ON a.idusuario = u.id
            ORDER BY a.data
        """;

        try (Connection conn = ConectarBanco.conectarBancoPostgres();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                agendamentos.add(montarAgendamento(rs));
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return agendamentos;
    }

    public Agendamento buscar(int id) {
        String sql = """
            SELECT a.*, 
                   s.id AS sala_id, s.tipo AS sala_tipo, s.capacidade AS sala_capacidade, s.bloco AS sala_bloco,
                   f.id AS func_id, f.nome AS func_nome,
                   u.id AS usuario_id, u.nome AS usuario_nome
            FROM agendamento a
            INNER JOIN sala s ON a.idsala = s.id
            INNER JOIN funcionario f ON a.idfunc = f.id
            INNER JOIN usuario u ON a.idusuario = u.id
            WHERE a.id = ?
        """;

        try (Connection conn = ConectarBanco.conectarBancoPostgres();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return montarAgendamento(rs);
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Agendamento> buscarPorData(LocalDate data) {
        List<Agendamento> agendamentos = new ArrayList<>();
        String sql = """
            SELECT a.*, 
                   s.id AS sala_id, s.tipo AS sala_tipo, s.capacidade AS sala_capacidade, s.bloco AS sala_bloco,
                   f.id AS func_id, f.nome AS func_nome,
                   u.id AS usuario_id, u.nome AS usuario_nome
            FROM agendamento a
            INNER JOIN sala s ON a.idsala = s.id
            INNER JOIN funcionario f ON a.idfunc = f.id
            INNER JOIN usuario u ON a.idusuario = u.id
            WHERE a.data = ?
        """;

        try (Connection conn = ConectarBanco.conectarBancoPostgres();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(data));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                agendamentos.add(montarAgendamento(rs));
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return agendamentos;
    }

    public List<Agendamento> listarPorStatus(int status) {
        List<Agendamento> agendamentos = new ArrayList<>();
        String sql = """
            SELECT a.*, 
                   s.id AS sala_id, s.tipo AS sala_tipo, s.capacidade AS sala_capacidade, s.bloco AS sala_bloco,
                   f.id AS func_id, f.nome AS func_nome,
                   u.id AS usuario_id, u.nome AS usuario_nome
            FROM agendamento a
            INNER JOIN sala s ON a.idsala = s.id
            INNER JOIN funcionario f ON a.idfunc = f.id
            INNER JOIN usuario u ON a.idusuario = u.id
            WHERE a.status = ?
            ORDER BY a.data
        """;

        try (Connection conn = ConectarBanco.conectarBancoPostgres();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, status);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                agendamentos.add(montarAgendamento(rs));
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return agendamentos;
    }

    private void preencherStatement(Agendamento agendamento, PreparedStatement stmt, boolean isUpdate) throws SQLException {
        stmt.setInt(1, agendamento.getSala().getId());
        stmt.setInt(2, agendamento.getFuncionario().getId());
        stmt.setInt(3, agendamento.getUsuario().getId());
        stmt.setInt(4, agendamento.getStatus());
        stmt.setInt(5, agendamento.getTurno());
        stmt.setDate(6, Date.valueOf(agendamento.getData()));
        stmt.setDate(7, Date.valueOf(
                agendamento.getDatacadastro() != null ? agendamento.getDatacadastro() : LocalDate.now()
        ));
        if (isUpdate) {
            stmt.setInt(8, agendamento.getId());
        }
    }

    private Agendamento montarAgendamento(ResultSet rs) throws SQLException {
        Agendamento a = new Agendamento();

        a.setId(rs.getInt("id"));
        a.setStatus(rs.getInt("status"));
        a.setTurno(rs.getInt("turno"));
        a.setData(rs.getDate("data").toLocalDate());
        a.setDatacadastro(rs.getDate("datacadastro").toLocalDate());

        Sala s = new Sala();
        s.setId(rs.getInt("sala_id"));
        s.setTipo(rs.getString("sala_tipo"));
        s.setCapacidade(rs.getInt("sala_capacidade"));
        s.setBloco(rs.getString("sala_bloco"));
        a.setSala(s);

        Funcionario f = new Funcionario();
        f.setId(rs.getInt("func_id"));
        f.setNome(rs.getString("func_nome"));
        a.setFuncionario(f);

        Usuario u = new Usuario();
        u.setId(rs.getInt("usuario_id"));
        u.setNome(rs.getString("usuario_nome"));
        a.setUsuario(u);

        return a;
    }
}
