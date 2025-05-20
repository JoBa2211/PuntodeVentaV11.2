package vista;

import javax.swing.*;
import java.awt.*;

public class PanelReporteInventario extends JPanel {
    public JButton btnGenerarReporte;
    public JLabel lblCantidadProductos;
    public JLabel lblValorTotal;
    public JLabel lblStockTotal;
    public JComboBox<String> comboCategorias;

    public PanelReporteInventario() {
        setLayout(new BorderLayout());

        // Texto grande y centrado
        JLabel lblTitulo = new JLabel("REPORTE DE INVENTARIO", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(lblTitulo, BorderLayout.NORTH);

        // Panel para los datos de inventario (debajo del título)
        JPanel panelDatos = new JPanel();
        panelDatos.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 5));
        lblCantidadProductos = new JLabel("Cantidad de productos: ---");
        lblCantidadProductos.setFont(new Font("Arial", Font.PLAIN, 18));
        lblValorTotal = new JLabel("Valor total del inventario: ---");
        lblValorTotal.setFont(new Font("Arial", Font.PLAIN, 18));
        lblStockTotal = new JLabel("Stock total: ---");
        lblStockTotal.setFont(new Font("Arial", Font.PLAIN, 18));
        panelDatos.add(lblCantidadProductos);
        panelDatos.add(lblValorTotal);
        panelDatos.add(lblStockTotal);
        add(panelDatos, BorderLayout.CENTER);

        // Panel para el botón y combo box juntos
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnGenerarReporte = new JButton("Generar Reporte de Inventario");
        comboCategorias = new JComboBox<>();
        comboCategorias.addItem("Todos"); // Opción por defecto
        panelBoton.add(btnGenerarReporte);
        panelBoton.add(new JLabel("Categoría: "));
        panelBoton.add(comboCategorias);

        add(panelBoton, BorderLayout.SOUTH);
    }

    public void setCantidadProductos(int cantidad) {
        lblCantidadProductos.setText("Cantidad de productos: " + cantidad);
    }

    public void setValorTotal(double valor) {
        lblValorTotal.setText("Valor total del inventario: $" + String.format("%.2f", valor));
    }

    public void setStockTotal(double stock) {
        lblStockTotal.setText("Stock total: " + String.format("%.2f", stock));
    }
}
