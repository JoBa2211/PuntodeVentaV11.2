package vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Esta clase es solo para la vista (UI).
 * No debe contener ningún proceso lógico ni validación, solo la presentación.
 */
public class LoginUI extends JFrame {
    public JTextField usuarioTextField;
    public JPasswordField contrasenaPasswordField;
    public JButton iniciarSesionButton;
    public JButton cancelarButton;
    public JLabel mensajeLabel;

    public LoginUI() {
        super("Inicio de Sesión");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 320);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel principal con color de fondo y borde
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(235, 240, 250));
        panel.setBorder(new EmptyBorder(20, 30, 20, 30));

        // Panel superior con imagen y título
        JPanel panelSuperior = new JPanel();
        panelSuperior.setOpaque(false);
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.Y_AXIS));
        panelSuperior.setBorder(new EmptyBorder(0, 0, 10, 0));

        // Imagen de usuario (ajusta la ruta si es necesario)
        JLabel iconLabel;
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/user_icon.png"));
            Image img = icon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
            iconLabel = new JLabel(new ImageIcon(img));
        } catch (Exception e) {
            iconLabel = new JLabel();
        }
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelSuperior.add(iconLabel);

        JLabel tituloLabel = new JLabel("Bienvenido");
        tituloLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        tituloLabel.setForeground(new Color(60, 80, 180));
        tituloLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelSuperior.add(tituloLabel);

        panel.add(panelSuperior, BorderLayout.NORTH);

        // Panel central con formulario
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 8, 10, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel usuarioLabel = new JLabel("Usuario:");
        usuarioLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panelCentral.add(usuarioLabel, gbc);

        usuarioTextField = new JTextField();
        usuarioTextField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        usuarioTextField.setPreferredSize(new Dimension(300, 32)); // Más ancho
        usuarioTextField.setMinimumSize(new Dimension(300, 32));
        usuarioTextField.setMaximumSize(new Dimension(300, 32));
        usuarioTextField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 220), 1),
                new EmptyBorder(5, 8, 5, 8)
        ));
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panelCentral.add(usuarioTextField, gbc);

        JLabel contrasenaLabel = new JLabel("Contraseña:");
        contrasenaLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panelCentral.add(contrasenaLabel, gbc);

        contrasenaPasswordField = new JPasswordField();
        contrasenaPasswordField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        contrasenaPasswordField.setPreferredSize(new Dimension(300, 32)); // Más ancho
        contrasenaPasswordField.setMinimumSize(new Dimension(300, 32));
        contrasenaPasswordField.setMaximumSize(new Dimension(300, 32));
        contrasenaPasswordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 220), 1),
                new EmptyBorder(5, 8, 5, 8)
        ));
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panelCentral.add(contrasenaPasswordField, gbc);

        mensajeLabel = new JLabel(" ");
        mensajeLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        mensajeLabel.setForeground(Color.RED);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        panelCentral.add(mensajeLabel, gbc);

        panel.add(panelCentral, BorderLayout.CENTER);

        // Panel inferior con botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setOpaque(false);

        iniciarSesionButton = new JButton("Iniciar Sesión");
        personalizarBoton(iniciarSesionButton, new Color(70, 130, 180), Color.WHITE);

        cancelarButton = new JButton("Cancelar");
        personalizarBoton(cancelarButton, new Color(220, 50, 50), Color.WHITE);

        panelBotones.add(iniciarSesionButton);
        panelBotones.add(cancelarButton);

        panel.add(panelBotones, BorderLayout.SOUTH);

        add(panel);

        // Navegación con flechas y Enter
        usuarioTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_ENTER) {
                    contrasenaPasswordField.requestFocusInWindow();
                }
            }
        });

        contrasenaPasswordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_ENTER) {
                    iniciarSesionButton.requestFocusInWindow();
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    usuarioTextField.requestFocusInWindow();
                }
            }
        });

        iniciarSesionButton.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    contrasenaPasswordField.requestFocusInWindow();
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    cancelarButton.requestFocusInWindow();
                }
            }
        });

        cancelarButton.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    iniciarSesionButton.requestFocusInWindow();
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    contrasenaPasswordField.requestFocusInWindow();
                }
            }
        });

        setVisible(true);

        // Selecciona el textbox de usuario al abrir la ventana
        usuarioTextField.requestFocusInWindow();
    }

    private void personalizarBoton(JButton boton, Color colorFondo, Color colorTexto) {
        boton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        boton.setBackground(colorFondo);
        boton.setForeground(colorTexto);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorFondo.darker(), 1),
                new EmptyBorder(10, 25, 10, 25)
        ));
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(colorFondo.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(colorFondo);
            }
        });
    }
}
