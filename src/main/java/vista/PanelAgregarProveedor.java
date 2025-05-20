package vista;

import javax.swing.*;
import java.awt.*;

public class PanelAgregarProveedor extends JPanel {
    public JTextField txtNombre;
    public JTextField txtTelefono;
    public JTextField txtEmail;
    public JTextField txtDireccion;
    public JButton btnAgregar;
    public JButton btnEditar;
    public JButton btnEliminar;

    public PanelAgregarProveedor() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblNombre = new JLabel("Nombre:");
        JLabel lblTelefono = new JLabel("Teléfono:");
        JLabel lblEmail = new JLabel("Email:");
        JLabel lblDireccion = new JLabel("Dirección:");

        txtNombre = new JTextField(20);
        txtTelefono = new JTextField(20);
        txtEmail = new JTextField(20);
        txtDireccion = new JTextField(20);

        btnAgregar = new JButton("Agregar");
        btnAgregar.setBackground(new Color(46, 204, 113));
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setFont(new Font("Arial", Font.BOLD, 16));
        btnAgregar.setPreferredSize(new Dimension(120, 40));
        btnAgregar.setFocusPainted(false);
        btnAgregar.setOpaque(true);
        btnAgregar.setBorderPainted(false);

        btnEditar = new JButton("Editar");
        btnEditar.setBackground(new Color(52, 152, 219));
        btnEditar.setForeground(Color.WHITE);
        btnEditar.setFont(new Font("Arial", Font.BOLD, 16));
        btnEditar.setPreferredSize(new Dimension(120, 40));
        btnEditar.setFocusPainted(false);
        btnEditar.setOpaque(true);
        btnEditar.setBorderPainted(false);

        btnEliminar = new JButton("Eliminar");
        btnEliminar.setBackground(new Color(231, 76, 60));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFont(new Font("Arial", Font.BOLD, 16));
        btnEliminar.setPreferredSize(new Dimension(120, 40));
        btnEliminar.setFocusPainted(false);
        btnEliminar.setOpaque(true);
        btnEliminar.setBorderPainted(false);

        gbc.gridx = 0; gbc.gridy = 0;
        add(lblNombre, gbc);
        gbc.gridx = 1;
        add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(lblTelefono, gbc);
        gbc.gridx = 1;
        add(txtTelefono, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(lblEmail, gbc);
        gbc.gridx = 1;
        add(txtEmail, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        add(lblDireccion, gbc);
        gbc.gridx = 1;
        add(txtDireccion, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        add(panelBotones, gbc);
    }

    public void limpiarCampos() {
        txtNombre.setText("");
        txtTelefono.setText("");
        txtEmail.setText("");
        txtDireccion.setText("");
    }
}
