package br.ufsm.csi.salas.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FuncionarioTest {

    @Test
    public void testFuncionarioCadastro() {
        Funcionario funcionario = new Funcionario(1, "Juliano Rocha", "julianoblw12@gmail.com", "037.882.470-88", 1, "senha123");

        assertAll("Funcionario properties",
                () -> assertEquals(1, funcionario.getId()),
                () -> assertEquals("Juliano Rocha", funcionario.getNome()),
                () -> assertEquals("julianoblw12@gmail.com", funcionario.getEmail()),
                () -> assertEquals("037.882.470-88", funcionario.getCpf()),
                () -> assertEquals(1, funcionario.getPermissao()),
                () -> assertEquals("senha123", funcionario.getSenha())
        );
    }

    @Test
    public void testFuncionarioSetters() {
        Funcionario funcionario = new Funcionario();

        funcionario.setId(2);
        funcionario.setNome("Neymar jr dos Santos");
        funcionario.setEmail("njrthebest@email.com");
        funcionario.setCpf("890.809.270-66");
        funcionario.setPermissao(1);
        funcionario.setSenha("novaSenha456");

        assertAll("Funcionario setters",
                () -> assertEquals(2, funcionario.getId()),
                () -> assertEquals("Neymar jr dos Santos", funcionario.getNome()),
                () -> assertEquals("njrthebest@email.com", funcionario.getEmail()),
                () -> assertEquals("890.809.270-66", funcionario.getCpf()),
                () -> assertEquals(1, funcionario.getPermissao()),
                () -> assertEquals("novaSenha456", funcionario.getSenha())
        );
    }

    @Test
    public void testFuncionarioConstrutor() {
        Funcionario funcionario = new Funcionario();

        assertNull(funcionario.getId());
        assertNull(funcionario.getNome());
        assertNull(funcionario.getEmail());
        assertNull(funcionario.getCpf());
        assertNull(funcionario.getSenha());
    }
}