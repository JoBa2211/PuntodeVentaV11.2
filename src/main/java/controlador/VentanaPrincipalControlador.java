package controlador;

import vista.VentanaPrincipal;
import main.Main; // Asegúrate de importar la clase Main

import javax.swing.*;

public class VentanaPrincipalControlador {
    private final VentanaPrincipal ventanaPrincipal;

    public VentanaPrincipalControlador(VentanaPrincipal ventanaPrincipal) {
        this.ventanaPrincipal = ventanaPrincipal;
        agregarListeners();
        // Al inicializar, muestra el usuario actual si existe
        actualizarUsuarioActual();
    }

    private void agregarListeners() {
        ventanaPrincipal.menuLogin.addActionListener(e ->
            JOptionPane.showMessageDialog(ventanaPrincipal, "Login seleccionado")
        );

    }

    // NUEVO: Método para actualizar el usuario mostrado en la vista principal
    public void actualizarUsuarioActual() {
        String nombre = (Main.usuarioActual != null) ? Main.usuarioActual.getUsuario() : "---";
        ventanaPrincipal.setUsuarioActual(nombre);
    }

    // Si necesitas actualizar el usuario desde otro lugar:
    public void setUsuarioActual(String usuario) {
        ventanaPrincipal.setUsuarioActual(usuario);
    }
}
