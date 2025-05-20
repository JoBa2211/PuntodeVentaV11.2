package vista;

import clases.Producto;
import controlador.ProductosBajoMinimoControlador;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DialogoProductosBajoMinimo extends JDialog {
    private JTable tabla;
    private ProductosBajoMinimoTableModel tableModel;

    public DialogoProductosBajoMinimo(JFrame parent, List<Producto> productosBajoMinimo, ProductosBajoMinimoControlador controlador) {
        super(parent, "Productos Bajo Mínimo", true);
        setSize(600, 300);
        setLocationRelativeTo(parent);

        tableModel = new ProductosBajoMinimoTableModel(productosBajoMinimo);
        tabla = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(tabla);
        add(scrollPane, BorderLayout.CENTER);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());

        // Botón para imprimir reporte
        JButton btnImprimirReporte = new JButton("Imprimir Reporte");
        btnImprimirReporte.addActionListener(e -> {
            controlador.imprimirReporteBajoMinimo();
        });

        JPanel panelBoton = new JPanel();
        panelBoton.add(btnImprimirReporte);
        panelBoton.add(btnCerrar);
        add(panelBoton, BorderLayout.SOUTH);
    }
}
