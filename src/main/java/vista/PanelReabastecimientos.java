package vista;

import clases.Producto;
import DAO.ProductoDAO;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PanelReabastecimientos extends JPanel {
    public JButton btnSolicitar;
    public JButton btnCancelar;
    public JButton btnBorrar; // Nuevo botón borrar
    public JComboBox<String> comboProductos;
    public JSpinner spinnerCantidad;
    public JTextArea txtNotas;

    public PanelReabastecimientos() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder("Reabastecimiento"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        int y = 0;

        // Producto (ahora ComboBox)
        gbc.gridx = 0; gbc.gridy = y; gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Producto:"), gbc);
        comboProductos = new JComboBox<>();
        gbc.gridx = 1; gbc.gridy = y; gbc.anchor = GridBagConstraints.WEST;
        add(comboProductos, gbc);

        y++;
        // Cantidad
        gbc.gridx = 0; gbc.gridy = y; gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Cantidad:"), gbc);
        spinnerCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
        gbc.gridx = 1; gbc.gridy = y; gbc.anchor = GridBagConstraints.WEST;
        add(spinnerCantidad, gbc);

        y++;
        // Notas
        gbc.gridx = 0; gbc.gridy = y; gbc.anchor = GridBagConstraints.NORTHEAST;
        add(new JLabel("Notas:"), gbc);
        txtNotas = new JTextArea(3, 18);
        txtNotas.setLineWrap(true);
        txtNotas.setWrapStyleWord(true);
        JScrollPane scrollNotas = new JScrollPane(txtNotas);
        gbc.gridx = 1; gbc.gridy = y; gbc.anchor = GridBagConstraints.WEST;
        add(scrollNotas, gbc);

        y++;
        // Botones
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        btnSolicitar = new JButton("Solicitar");
        btnCancelar = new JButton("Cancelar");
        btnBorrar = new JButton("Borrar"); // Agrega el botón borrar
        panelBotones.add(btnSolicitar);
        panelBotones.add(btnCancelar);
        panelBotones.add(btnBorrar); // Agrega el botón borrar al panel
        add(panelBotones, gbc);

        // Quita la llamada a cargarProductosComboBox();
    }

    // Solo recibe la lista y llena el combo, sin lógica de consulta
    public void setProductosComboBox(List<Producto> productos) {
        comboProductos.removeAllItems();
        for (Producto p : productos) {
            String display = p.getCodigo() + " - " + p.getNombre() + " - $" + p.getPrecioVenta() + " - " + p.getExistencias();
            comboProductos.addItem(display);
        }
    }
}
