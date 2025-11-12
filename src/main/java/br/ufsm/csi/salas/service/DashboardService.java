package br.ufsm.csi.salas.service;

import br.ufsm.csi.salas.dao.AgendamentoDAO;
import br.ufsm.csi.salas.dao.SalaDAO;
import org.springframework.stereotype.Service;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import br.ufsm.csi.salas.dao.ConectarBanco;

@Service
public class DashboardService {

    private final SalaDAO salaDAO;
    private final AgendamentoDAO agendamentoDAO;

    public DashboardService() {
        this.salaDAO = new SalaDAO();
        this.agendamentoDAO = new AgendamentoDAO();
    }

    public int getTurnoAtual() {
        LocalTime agora = LocalTime.now();
        if (agora.isBefore(LocalTime.of(12, 0))) {
            return 1; // Manhã
        } else if (agora.isBefore(LocalTime.of(18, 0))) {
            return 2; // Tarde
        } else {
            return 3; // Noite
        }
    }

    public String getNomeTurnoAtual() {
        int turno = getTurnoAtual();
        switch (turno) {
            case 1: return "Manhã";
            case 2: return "Tarde";
            case 3: return "Noite";
            default: return "Indefinido";
        }
    }

    public int getSalasEmUso() {
        LocalDate hoje = LocalDate.now();
        int turnoAtual = getTurnoAtual();

        try (Connection conn = ConectarBanco.conectarBancoPostgres();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT COUNT(DISTINCT idsala) FROM agendamento " +
                             "WHERE data = ? AND turno = ? AND status = 1")) {

            stmt.setDate(1, Date.valueOf(hoje));
            stmt.setInt(2, turnoAtual);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Erro ao contar salas em uso: " + e.getMessage());
        }
        return 0;
    }

    public int getSalasLivres() {
        int totalSalas = salaDAO.listar().size();
        int salasEmUso = getSalasEmUso();
        return totalSalas - salasEmUso;
    }

    public List<Integer> getSalasEmUsoIds() {
        List<Integer> salasEmUsoIds = new ArrayList<>();
        LocalDate hoje = LocalDate.now();
        int turnoAtual = getTurnoAtual();

        try (Connection conn = ConectarBanco.conectarBancoPostgres();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT DISTINCT idsala FROM agendamento " +
                             "WHERE data = ? AND turno = ? AND status = 1")) {

            stmt.setDate(1, Date.valueOf(hoje));
            stmt.setInt(2, turnoAtual);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                salasEmUsoIds.add(rs.getInt("idsala"));
            }

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Erro ao buscar IDs de salas em uso: " + e.getMessage());
        }
        return salasEmUsoIds;
    }

    public int getAgendamentosHoje() {
        LocalDate hoje = LocalDate.now();

        try (Connection conn = ConectarBanco.conectarBancoPostgres();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT COUNT(*) FROM agendamento WHERE data = ?")) {

            stmt.setDate(1, Date.valueOf(hoje));
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Erro ao contar agendamentos de hoje: " + e.getMessage());
        }
        return 0;
    }

    public int getAgendamentosConcluidos() {
        LocalDate hoje = LocalDate.now();

        try (Connection conn = ConectarBanco.conectarBancoPostgres();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT COUNT(*) FROM agendamento WHERE data = ? AND status = 1")) {

            stmt.setDate(1, Date.valueOf(hoje));
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Erro ao contar agendamentos concluídos: " + e.getMessage());
        }
        return 0;
    }

    public int getAgendamentosAndamento() {
        LocalDate hoje = LocalDate.now();

        try (Connection conn = ConectarBanco.conectarBancoPostgres();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT COUNT(*) FROM agendamento WHERE data = ? AND status = 2")) {

            stmt.setDate(1, Date.valueOf(hoje));
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Erro ao contar agendamentos em andamento: " + e.getMessage());
        }
        return 0;
    }
}