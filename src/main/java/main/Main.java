package main;

import lib.ConnectionFactory;
import vista.LoginUI;
import controlador.LoginControlador;
import clases.Inventario;
import clases.Usuario;

public class Main {
    // Haz la instancia de Inventario accesible globalmente
    public static Inventario inventario;

    // Nuevo: Usuario actual y su id
    public static Usuario usuarioActual;
    public static int usuarioIdActual = -1;

    public static void main(String[] args) {
        // Inicializa la conexión a SQL Server
        ConnectionFactory.inicializar(
            ConnectionFactory.TipoBD.SQLSERVER,
            "jdbc:sqlserver://localhost:1433;databaseName=PuntodeVentaTienda;integratedSecurity=false;encrypt=true;trustServerCertificate=true",
            "sa",
            "1234"
        );

        // Instancia la lista de inventario al inicio del programa
        inventario = new Inventario();

        // Muestra la interfaz de inicio de sesión en el hilo de eventos de Swing
        javax.swing.SwingUtilities.invokeLater(() -> {
            LoginUI loginUI = new LoginUI();
            new LoginControlador(loginUI);
        });
    }
}
