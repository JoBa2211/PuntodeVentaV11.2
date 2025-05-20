package test;

import lib.ConnectionFactory;
import lib.SQLQueries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TestUsuarios {
    public static void main(String[] args) {
        // Inicializa la conexión antes de usarla
        ConnectionFactory.inicializar(
            ConnectionFactory.TipoBD.SQLSERVER,
            "jdbc:sqlserver://localhost:1433;databaseName=PuntodeVentaTienda;integratedSecurity=false;encrypt=true;trustServerCertificate=true",
            "",
            ""
        );

        String query = SQLQueries.SELECT_ALL_USERS;
        try (Connection conn = ConnectionFactory.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            System.out.println("Usuarios en la base de datos:");
            while (rs.next()) {
                System.out.println(rs.getString(1));
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar usuarios: " + e.getMessage());
        }
    }
}
