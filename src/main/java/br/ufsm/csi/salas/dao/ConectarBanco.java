package br.ufsm.csi.salas.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConectarBanco {

    public static Connection conectarBancoPostgres()
            throws ClassNotFoundException, SQLException {

        Class.forName("org.postgresql.Driver");

        System.out.println("Driver carregado");

        String url = "jdbc:postgresql://localhost:5432/salas";
        String user = "postgres";
        String senha = "1234";
        return DriverManager.getConnection(url, user, senha);
    }

}
