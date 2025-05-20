package vista;

import javax.swing.*;
import java.awt.*;

public class PanelAgregarCategoria extends JPanel {
    public JTextField txtNombre;
    public JTextArea txtNotas;
    public JButton btnAgregar;
    public JButton btnEditar;
    public JButton btnEliminar;

    public PanelAgregarCategoria() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int y = 0;

        // Nombre
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0;
        add(new JLabel("Nombre:"), gbc);

        txtNombre = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = y;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        add(txtNombre, gbc);

        y++;
        // Notas
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.weightx = 0;
        add(new JLabel("Notas:"), gbc);

        txtNotas = new JTextArea(4, 20);
        txtNotas.setLineWrap(true);
        txtNotas.setWrapStyleWord(true);
        JScrollPane scrollNotas = new JScrollPane(txtNotas);
        gbc.gridx = 1;
        gbc.gridy = y;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        add(scrollNotas, gbc);

        y++;
        // Botones
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1.0;
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        btnAgregar = crearBoton("Agregar", new Color(46, 204, 113));
        btnEditar = crearBoton("Editar", new Color(52, 152, 219));
        btnEliminar = crearBoton("Eliminar", new Color(231, 76, 60));
        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        add(panelBotones, gbc);
    }

    private JButton crearBoton(String texto, Color colorFondo) {
        JButton boton = new JButton(texto);
        boton.setPreferredSize(new Dimension(110, 36));
        boton.setFocusPainted(false);
        boton.setFont(new Font("Arial", Font.BOLD, 15));
        boton.setForeground(Color.WHITE);
        boton.setBackground(colorFondo);
        boton.setOpaque(true);
        boton.setBorderPainted(false);
        return boton;
    }
}
