package br.ufsm.csi.salas.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class AgendamentoTest {

    @Test
    public void testAgendamentoCadastro() {
        Sala sala = new Sala(1, "Laboratório", 30, "A");
        Funcionario funcionario = new Funcionario(1, "Juliano Rocha", "julianoblw12@gmail.com", "037.882.470-88", 1, "senha123");
        Usuario usuario = new Usuario(1, "Carlos Souza", "carlos@email.com", "2023001");
        LocalDate data = LocalDate.of(2024, 1, 15);
        LocalDate dataCadastro = LocalDate.of(2024, 1, 10);

        Agendamento agendamento = new Agendamento(1, sala, funcionario, usuario, 1, 2, data, dataCadastro);

        assertAll("Agendamento properties",
                () -> assertEquals(1, agendamento.getId()),
                () -> assertEquals(sala, agendamento.getSala()),
                () -> assertEquals(funcionario, agendamento.getFuncionario()),
                () -> assertEquals(usuario, agendamento.getUsuario()),
                () -> assertEquals(1, agendamento.getStatus()),
                () -> assertEquals(2, agendamento.getTurno()),
                () -> assertEquals(data, agendamento.getData()),
                () -> assertEquals(dataCadastro, agendamento.getDatacadastro())
        );
    }

    @Test
    public void testAgendamentoSetters() {
        Sala sala = new Sala(1, "Laboratório", 30, "A");
        Funcionario funcionario = new Funcionario(1, "Juliano Rocha", "julianoblw12@gmail.com", "037.882.470-88", 1, "senha123");
        Usuario usuario = new Usuario(1, "Carlos Souza", "carlos@email.com", "2023001");
        LocalDate data = LocalDate.of(2024, 1, 15);
        LocalDate dataCadastro = LocalDate.of(2024, 1, 10);

        Agendamento agendamento = new Agendamento();
        agendamento.setId(1);
        agendamento.setSala(sala);
        agendamento.setFuncionario(funcionario);
        agendamento.setUsuario(usuario);
        agendamento.setStatus(1);
        agendamento.setTurno(2);
        agendamento.setData(data);
        agendamento.setDatacadastro(dataCadastro);

        assertAll("Agendamento setters",
                () -> assertEquals(1, agendamento.getId()),
                () -> assertEquals(sala, agendamento.getSala()),
                () -> assertEquals(funcionario, agendamento.getFuncionario()),
                () -> assertEquals(usuario, agendamento.getUsuario()),
                () -> assertEquals(1, agendamento.getStatus()),
                () -> assertEquals(2, agendamento.getTurno()),
                () -> assertEquals(data, agendamento.getData()),
                () -> assertEquals(dataCadastro, agendamento.getDatacadastro())
        );
    }

    @Test
    public void testAgendamentoConstrutor() {
        Agendamento agendamento = new Agendamento();

        assertAll("Agendamento default construtor",
                () -> assertNull(agendamento.getId()),
                () -> assertNull(agendamento.getSala()),
                () -> assertNull(agendamento.getFuncionario()),
                () -> assertNull(agendamento.getUsuario()),
                () -> assertEquals(0, agendamento.getStatus()),
                () -> assertEquals(0, agendamento.getTurno()),
                () -> assertNull(agendamento.getData()),
                () -> assertNull(agendamento.getDatacadastro())
        );
    }
}
