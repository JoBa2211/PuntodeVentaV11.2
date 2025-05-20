package controlador;

import DAO.UsuarioDAO;
import clases.Usuario;
import vista.LoginUI;
import vista.VentanaPrincipal;
import main.Main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Toolkit;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioInputStream;
import java.io.InputStream;

public class LoginControlador {
    private final LoginUI loginUI;
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public LoginControlador(LoginUI loginUI) {
        this.loginUI = loginUI;
        agregarListeners();
    }

    private void agregarListeners() {
        loginUI.iniciarSesionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usuario = loginUI.usuarioTextField.getText();
                String contrasena = new String(loginUI.contrasenaPasswordField.getPassword());
                Usuario user = usuarioDAO.obtenerPorNombre(usuario);
                if (user != null && user.getContraseña() != null && contrasena.equals(user.getContraseña())) {
                    // Guardar usuario e id globalmente
                    Main.usuarioActual = user;
                    Main.usuarioIdActual = user.getId();

                    System.out.println("Inicio de sesión exitoso para usuario: " + usuario);
                    loginUI.mensajeLabel.setText("¡Inicio de sesión exitoso!");
                    loginUI.mensajeLabel.setForeground(new java.awt.Color(0, 128, 0));
                    // Abrir ventana principal y cerrar login
                    SwingUtilities.invokeLater(() -> {
                        VentanaPrincipal ventanaPrincipal = new VentanaPrincipal();
                        new VentanaPrincipalControlador(ventanaPrincipal);
                    });
                    loginUI.dispose();
                } else {
                    System.out.println("Usuario o contraseña incorrectos.");
                    loginUI.mensajeLabel.setText("Usuario o contraseña incorrectos.");
                    loginUI.mensajeLabel.setForeground(java.awt.Color.RED);
                    // Sonido de error fuerte (usa beep y además reproduce un sonido de error de Windows si está disponible)
                    try {
                        InputStream audioSrc = getClass().getResourceAsStream("/Windows_Notify.wav");
                        if (audioSrc != null) {
                            AudioInputStream ais = AudioSystem.getAudioInputStream(audioSrc);
                            Clip clip = AudioSystem.getClip();
                            clip.open(ais);
                            clip.start();
                        } else {
                            for (int i = 0; i < 3; i++) {
                                Toolkit.getDefaultToolkit().beep();
                                Thread.sleep(60);
                            }
                        }
                    } catch (Exception ex) {
                        for (int i = 0; i < 3; i++) {
                            Toolkit.getDefaultToolkit().beep();
                            try { Thread.sleep(60); } catch (InterruptedException ignored) {}
                        }
                    }
                }
            }
        });

        loginUI.cancelarButton.addActionListener(e -> System.exit(0));
    }

    private boolean verificarCredenciales(String usuario, String contrasena) {
        Usuario user = usuarioDAO.obtenerPorNombre(usuario);
        if (user != null && user.getContraseña() != null) {
            return contrasena.equals(user.getContraseña());
        }
        return false;
    }
}
