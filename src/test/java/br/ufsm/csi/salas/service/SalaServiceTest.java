package br.ufsm.csi.salas.service;

import br.ufsm.csi.salas.dao.SalaDAO;
import br.ufsm.csi.salas.model.Sala;
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
public class SalaServiceTest {

    @Mock
    private SalaDAO salaDAO; // Objeto mockado

    @InjectMocks
    private SalaService salaService; // Injeta o objeto mockado, e não o DAO real que tem conexão com o banco

    private Sala salaTeste;

    @BeforeEach
    public void setUp() {
        salaTeste = new Sala(1, "Laboratório", 30, "A");
    }

    @Test
    public void testInserirSala_Sucesso() {
        when(salaDAO.inserir(salaTeste)).thenReturn("Sala inserida com sucesso!");

        String resultado = salaService.inserir(salaTeste);

        assertEquals("Sala inserida com sucesso!", resultado);
        verify(salaDAO, times(1)).inserir(salaTeste);
    }

    @Test
    public void testInserirSala_Erro() {
        when(salaDAO.inserir(salaTeste)).thenReturn("Erro ao inserir sala!");

        String resultado = salaService.inserir(salaTeste);

        assertEquals("Erro ao inserir sala!", resultado);
        verify(salaDAO, times(1)).inserir(salaTeste);
    }

    @Test
    public void testAlterarSala_Sucesso() {
        when(salaDAO.alterar(salaTeste)).thenReturn("Sala alterada com sucesso!");

        String resultado = salaService.alterar(salaTeste);

        assertEquals("Sala alterada com sucesso!", resultado);
        verify(salaDAO, times(1)).alterar(salaTeste);
    }

    @Test
    public void testAlterarSala_Erro() {
        when(salaDAO.alterar(salaTeste)).thenReturn("Erro ao alterar sala!");

        String resultado = salaService.alterar(salaTeste);

        assertEquals("Erro ao alterar sala!", resultado);
        verify(salaDAO, times(1)).alterar(salaTeste);
    }

    @Test
    public void testExcluirSala_Sucesso() {
        int idSala = 1;
        when(salaDAO.excluir(idSala)).thenReturn(true);

        String resultado = salaService.excluir(idSala);

        assertEquals("Sucesso ao excluir sala", resultado);
        verify(salaDAO, times(1)).excluir(idSala);
    }

    @Test
    public void testExcluirSala_Erro() {
        int idSala = 1;

        when(salaDAO.excluir(idSala)).thenReturn(false);

        String resultado = salaService.excluir(idSala);

        assertEquals("Erro ao excluir sala", resultado);
        verify(salaDAO, times(1)).excluir(idSala);
    }

    @Test
    public void testListarSalas() {
        List<Sala> salasEsperadas = Arrays.asList(
                new Sala(1, "Laboratório", 30, "A"),
                new Sala(2, "Sala de Aula", 40, "B")
        );

        when(salaDAO.listar()).thenReturn(new ArrayList<>(salasEsperadas));

        List<Sala> resultado = salaService.listar();

        assertEquals(2, resultado.size());
        verify(salaDAO, times(1)).listar();
    }

    @Test
    public void testBuscarSala() {
        int idSala = 1;

        when(salaDAO.buscar(idSala)).thenReturn(salaTeste);

        Sala resultado = salaService.buscar(idSala);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        verify(salaDAO, times(1)).buscar(idSala);
    }
}
