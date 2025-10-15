package br.ufsm.csi.salas.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SalaTest {

    @Test
    public void testSalaCadastro() {
        Sala sala = new Sala(1, "Laboratório", 30, "A");

        assertAll("Sala properties",
                () -> assertEquals(1, sala.getId()),
                () -> assertEquals("Laboratório", sala.getTipo()),
                () -> assertEquals(30, sala.getCapacidade()),
                () -> assertEquals("A", sala.getBloco())
        );
    }

    @Test
    public void testSalaSetters() {
        Sala sala = new Sala();

        sala.setId(2);
        sala.setTipo("Sala de Aula");
        sala.setCapacidade(40);
        sala.setBloco("B");

        assertAll("Sala setters",
                () -> assertEquals(2, sala.getId()),
                () -> assertEquals("Sala de Aula", sala.getTipo()),
                () -> assertEquals(40, sala.getCapacidade()),
                () -> assertEquals("B", sala.getBloco())
        );
    }

    @Test
    public void testSalaConstrutor() {
        Sala sala = new Sala();

        assertNull(sala.getId());
        assertNull(sala.getTipo());
        assertEquals(0, sala.getCapacidade());
        assertNull(sala.getBloco());
    }
}
