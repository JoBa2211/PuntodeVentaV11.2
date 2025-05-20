package vista;

import clases.Producto;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PanelDevoluciones extends JPanel {
    public JComboBox<String> comboProductos;
    public JSpinner spinnerCantidad;
    public JTextField txtPrecio;
    public JButton btnAgregar;
    public JButton btnCancelar;

    public PanelDevoluciones() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder("Registrar Devolución"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        int y = 0;

        // Producto (ComboBox)
        gbc.gridx = 0; gbc.gridy = y; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0;
        add(new JLabel("Producto:"), gbc);
        comboProductos = new JComboBox<>();
        gbc.gridx = 1; gbc.weightx = 1.0;
        add(comboProductos, gbc);

        y++;
        // Cantidad
        gbc.gridx = 0; gbc.gridy = y; gbc.weightx = 0;
        add(new JLabel("Cantidad:"), gbc);
        spinnerCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
        gbc.gridx = 1; gbc.weightx = 1.0;
        add(spinnerCantidad, gbc);

        y++;
        // Precio (solo visualización)
        gbc.gridx = 0; gbc.gridy = y; gbc.weightx = 0;
        add(new JLabel("Precio:"), gbc);
        txtPrecio = new JTextField(10);
        txtPrecio.setEditable(false); // Solo visualización
        gbc.gridx = 1; gbc.weightx = 1.0;
        add(txtPrecio, gbc);

        y++;
        // Botones
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        btnAgregar = new JButton("Agregar");
        btnCancelar = new JButton("Cancelar");
        panelBotones.add(btnAgregar);
        panelBotones.add(btnCancelar);
        add(panelBotones, gbc);
    }

    // Método para llenar el combo desde el controlador
    public void setProductosComboBox(List<Producto> productos) {
        comboProductos.removeAllItems();
        for (Producto p : productos) {
            String display = p.getCodigo() + " - " + p.getNombre() + " - $" + p.getPrecioVenta() + " - " + p.getExistencias();
            comboProductos.addItem(display);
        }
    }
}
