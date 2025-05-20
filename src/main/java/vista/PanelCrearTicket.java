package vista;

import javax.swing.*;
import java.awt.*;

public class PanelCrearTicket extends JPanel {
    public JFrameTablaBusqueda tablaBusquedaProductos;
    public JButton btnAgregar;
    public JButton btnEliminar;
    public JButton btnCancelar;
    public JButton btnCompletarVenta; // Nuevo botón
    public JSpinner spinnerCantidad;

    public PanelCrearTicket() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // Agrega el JFrameTablaBusqueda en la parte superior (como panel)
        tablaBusquedaProductos = new JFrameTablaBusqueda("Buscar Producto");
        // Solo usamos el panel de contenido, no el JFrame completo
        add(tablaBusquedaProductos.getContentPane(), BorderLayout.CENTER);

        // Panel de controles debajo de la tabla
        JPanel panelControles = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.gridy = 0;

        // Spinner de cantidad
        gbc.gridx = 0;
        panelControles.add(new JLabel("Cantidad:"), gbc);
        spinnerCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 9999, 1));
        spinnerCantidad.setPreferredSize(new Dimension(60, 28));
        gbc.gridx = 1;
        panelControles.add(spinnerCantidad, gbc);

        // Botón Agregar
        gbc.gridx = 2;
        btnAgregar = new JButton("Agregar");
        btnAgregar.setBackground(new Color(46, 204, 113));
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setFont(new Font("Arial", Font.BOLD, 14));
        btnAgregar.setPreferredSize(new Dimension(110, 36));
        btnAgregar.setFocusPainted(false);
        btnAgregar.setOpaque(true);
        btnAgregar.setBorderPainted(false);
        panelControles.add(btnAgregar, gbc);

        // Botón Eliminar
        gbc.gridx = 3;
        btnEliminar = new JButton("Eliminar");
        btnEliminar.setBackground(new Color(231, 76, 60));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFont(new Font("Arial", Font.BOLD, 14));
        btnEliminar.setPreferredSize(new Dimension(110, 36));
        btnEliminar.setFocusPainted(false);
        btnEliminar.setOpaque(true);
        btnEliminar.setBorderPainted(false);
        panelControles.add(btnEliminar, gbc);

        // Botón Cancelar
        gbc.gridx = 4;
        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(127, 140, 141));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 14));
        btnCancelar.setPreferredSize(new Dimension(110, 36));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setOpaque(true);
        btnCancelar.setBorderPainted(false);
        panelControles.add(btnCancelar, gbc);

        // Botón Completar Venta
        gbc.gridx = 5;
        btnCompletarVenta = new JButton("Completar Venta");
        btnCompletarVenta.setBackground(new Color(52, 152, 219));
        btnCompletarVenta.setForeground(Color.WHITE);
        btnCompletarVenta.setFont(new Font("Arial", Font.BOLD, 14));
        btnCompletarVenta.setPreferredSize(new Dimension(160, 36));
        btnCompletarVenta.setFocusPainted(false);
        btnCompletarVenta.setOpaque(true);
        btnCompletarVenta.setBorderPainted(false);
        panelControles.add(btnCompletarVenta, gbc);

        add(panelControles, BorderLayout.SOUTH);
    }
}
