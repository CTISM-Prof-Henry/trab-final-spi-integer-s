package br.ufsm.csi.salas.service;

import br.ufsm.csi.salas.dao.AgendamentoDAO;
import br.ufsm.csi.salas.model.Agendamento;
import br.ufsm.csi.salas.model.Funcionario;
import br.ufsm.csi.salas.model.Sala;
import br.ufsm.csi.salas.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AgendamentoServiceTest {

    @Mock
    private AgendamentoDAO agendamentoDAO; // Objeto mockado

    @InjectMocks
    private AgendamentoService agendamentoService; // Injeta o objeto mockado, e não o DAO real que tem conexão com o banco

    private Agendamento agendamentoTeste;

    @BeforeEach
    void setUp() {
        Sala sala = new Sala(1, "Laboratório", 30, "A");
        Funcionario funcionario = new Funcionario(1, "Juliano Rocha", "julianoblw12@gmail.com", "037.882.470-88", 1, "senha123");
        Usuario usuario = new Usuario(1, "Neymar jr Santos", "njrthebest@email.com", "1234567");
        LocalDate data = LocalDate.of(2024, 1, 15);
        LocalDate dataCadastro = LocalDate.of(2024, 1, 10);

        agendamentoTeste = new Agendamento(1, sala, funcionario, usuario, 1, 2, data, dataCadastro);
    }

    @Test
    void testInserirAgendamento_Sucesso() {
        when(agendamentoDAO.inserir(agendamentoTeste)).thenReturn("Agendamento inserido com sucesso!");

        String resultado = agendamentoService.inserir(agendamentoTeste);

        assertEquals("Agendamento inserido com sucesso!", resultado);
        verify(agendamentoDAO, times(1)).inserir(agendamentoTeste);
    }

    @Test
    void testInserirAgendamento_Erro() {
        when(agendamentoDAO.inserir(agendamentoTeste)).thenReturn("Erro ao inserir agendamento");

        String resultado = agendamentoService.inserir(agendamentoTeste);

        assertEquals("Erro ao inserir agendamento", resultado);
        verify(agendamentoDAO, times(1)).inserir(agendamentoTeste);
    }

    @Test
    void testAlterarAgendamento_Sucesso() {
        when(agendamentoDAO.alterar(agendamentoTeste)).thenReturn("Agendamento alterado com sucesso!");

        String resultado = agendamentoService.alterar(agendamentoTeste);

        assertEquals("Agendamento alterado com sucesso!", resultado);
        verify(agendamentoDAO, times(1)).alterar(agendamentoTeste);
    }

    @Test
    void testAlterarAgendamento_Erro() {
        when(agendamentoDAO.alterar(agendamentoTeste)).thenReturn("Erro ao alterar agendamento");

        String resultado = agendamentoService.alterar(agendamentoTeste);

        assertEquals("Erro ao alterar agendamento", resultado);
        verify(agendamentoDAO, times(1)).alterar(agendamentoTeste);
    }

    @Test
    void testExcluirAgendamento_Sucesso() {
        when(agendamentoDAO.excluir(1)).thenReturn(true);

        String resultado = agendamentoService.excluir(1);

        assertEquals("Sucesso ao excluir agendamento", resultado);
        verify(agendamentoDAO, times(1)).excluir(1);
    }

    @Test
    void testExcluirAgendamento_Erro() {
        when(agendamentoDAO.excluir(1)).thenReturn(false);

        String resultado = agendamentoService.excluir(1);

        assertEquals("Erro ao excluir agendamento", resultado);
        verify(agendamentoDAO, times(1)).excluir(1);
    }

    @Test
    void testListarAgendamentos_Sucesso() {
        List<Agendamento> agendamentosMock = Collections.singletonList(agendamentoTeste);
        when(agendamentoDAO.listar()).thenReturn(new ArrayList<>(agendamentosMock));

        List<Agendamento> resultado = agendamentoService.listar();

        assertEquals(1, resultado.size());
        assertNotNull(resultado.getFirst().getSala());
        verify(agendamentoDAO, times(1)).listar();
    }

    @Test
    void testBuscarPorData_Sucesso() {
        LocalDate data = LocalDate.of(2024, 1, 15);
        List<Agendamento> agendamentosMock = Collections.singletonList(agendamentoTeste);
        when(agendamentoDAO.buscarPorData(data)).thenReturn(new ArrayList<>(agendamentosMock));

        List<Agendamento> resultado = agendamentoService.buscarPorData(data);

        assertEquals(1, resultado.size());
        verify(agendamentoDAO, times(1)).buscarPorData(data);
    }
}