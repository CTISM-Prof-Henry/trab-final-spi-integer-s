package br.ufsm.csi.salas.service;

import br.ufsm.csi.salas.dao.FuncionarioDAO;
import br.ufsm.csi.salas.model.Funcionario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FuncionarioServiceTest {

    @Mock
    private FuncionarioDAO funcionarioDAO; // Objeto mockado

    @InjectMocks
    private FuncionarioService funcionarioService; // Injeta o objeto mockado, e não o DAO real que tem conexão com o banco

    private Funcionario funcionarioTeste;

    @BeforeEach
    public void preencheFuncionario() {
        funcionarioTeste = new Funcionario(1, "Juliano Rocha", "julianoblw12@gmail.com", "037.882.470-88", 1, "senha123");
    }

    @Test
    public void testInserirFuncionario_Sucesso() {
        when(funcionarioDAO.inserir(funcionarioTeste)).thenReturn("Funcionário inserido com sucesso!");

        String resultado = funcionarioService.inserir(funcionarioTeste);

        assertEquals("Funcionário inserido com sucesso!", resultado);
        verify(funcionarioDAO, times(1)).inserir(funcionarioTeste);
    }

    @Test
    public void testInserirFuncionario_Erro() {
        when(funcionarioDAO.inserir(funcionarioTeste)).thenReturn("Erro ao inserir funcionário!");

        String resultado = funcionarioService.inserir(funcionarioTeste);

        assertEquals("Erro ao inserir funcionário!", resultado);
        verify(funcionarioDAO, times(1)).inserir(funcionarioTeste);
    }

    @Test
    public void testAlterarFuncionario_Sucesso() {
        when(funcionarioDAO.alterar(funcionarioTeste)).thenReturn("Funcionário alterado com sucesso!");

        String resultado = funcionarioService.alterar(funcionarioTeste);

        assertEquals("Funcionário alterado com sucesso!", resultado);
        verify(funcionarioDAO, times(1)).alterar(funcionarioTeste);
    }

    @Test
    public void testAlterarFuncionario_Erro() {
        when(funcionarioDAO.alterar(funcionarioTeste)).thenReturn("Erro ao alterar funcionário!");

        String resultado = funcionarioService.alterar(funcionarioTeste);

        assertEquals("Erro ao alterar funcionário!", resultado);
        verify(funcionarioDAO, times(1)).alterar(funcionarioTeste);
    }

    @Test
    public void testExcluirFuncionario_Sucesso() {
        int idFuncionario = 1;

        when(funcionarioDAO.excluir(idFuncionario)).thenReturn(true);

        String resultado = funcionarioService.excluir(idFuncionario);

        assertEquals("Sucesso ao excluir funcionario", resultado);
        verify(funcionarioDAO, times(1)).excluir(idFuncionario);
    }

    @Test
    public void testExcluirFuncionario_Erro() {
        int idFuncionario = 1;

        when(funcionarioDAO.excluir(idFuncionario)).thenReturn(false);

        String resultado = funcionarioService.excluir(idFuncionario);

        assertEquals("Erro ao excluir funcionario", resultado);
        verify(funcionarioDAO, times(1)).excluir(idFuncionario);
    }

    @Test
    public void testListarFuncionarios() {
        List<Funcionario> funcionariosEsperados = Arrays.asList(
                new Funcionario(1, "Juliano Rocha", "julianoblw12@gmail.com", "037.882.470-88", 1, "senha123"),
                new Funcionario(2, "Neymar jr dos Santos", "njrthebest@email.com", "890.809.270-66", 1, "senha456")
        );
        when(funcionarioDAO.listar()).thenReturn(new ArrayList<>(funcionariosEsperados));

        List<Funcionario> resultado = funcionarioService.listar();

        assertEquals(2, resultado.size());
        verify(funcionarioDAO, times(1)).listar();
    }

    @Test
    public void testBuscarFuncionario() {
        int idFuncionario = 1;

        when(funcionarioDAO.buscar(idFuncionario)).thenReturn(funcionarioTeste);

        Funcionario resultado = funcionarioService.buscar(idFuncionario);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        verify(funcionarioDAO, times(1)).buscar(idFuncionario);
    }
}
