package vista;

import clases.Ticket;
import clases.DetalleProductoTicket;
import clases.Producto;
import DAO.ProductoDAO;
import Printers.TicketPrinter;
import clases.ReporteVentas;
import Printers.ReporteVentasPrinter;

import javax.swing.*;
import java.awt.*;
import java.awt.print.PrinterException;
import java.awt.Desktop;
import java.io.File;

public class VistaImpresionTicket extends JFrame {
    private JTextArea areaTicket;
    private JButton btnImprimir;
    private JButton btnAbrirExcel;
    private String rutaExcelActual = null;

    public VistaImpresionTicket() {
        setTitle("Vista de Impresión de Ticket");
        setSize(600, 600); // Más ancho y alto por defecto
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        areaTicket = new JTextArea();
        areaTicket.setEditable(false);
        areaTicket.setFont(new Font("Monospaced", Font.PLAIN, 12)); // Fuente monoespaciada
        areaTicket.setLineWrap(false); // No cortar líneas largas, permitir scroll horizontal
        areaTicket.setWrapStyleWord(false);

        JScrollPane scrollPane = new JScrollPane(areaTicket,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        btnImprimir = new JButton("Imprimir");
        btnImprimir.addActionListener(e -> imprimirTicket());

        btnAbrirExcel = new JButton("Abrir en Excel");
        btnAbrirExcel.setEnabled(false);
        btnAbrirExcel.addActionListener(e -> abrirEnExcel());

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBoton.add(btnImprimir);
        panelBoton.add(btnAbrirExcel);

        add(scrollPane, BorderLayout.CENTER);
        add(panelBoton, BorderLayout.SOUTH);
    }

    public void mostrarTicket(Ticket ticket) {
        if (ticket == null) {
            areaTicket.setText("No hay ticket seleccionado.");
            btnAbrirExcel.setEnabled(false);
            rutaExcelActual = null;
            return;
        }
        areaTicket.setText(TicketPrinter.imprimir(ticket));
        btnAbrirExcel.setEnabled(false);
        rutaExcelActual = null;
        ajustarVentanaAlContenido();
    }

    public void mostrarReporteVentas(ReporteVentas reporte, String rutaExcel) {
        if (reporte == null) {
            areaTicket.setText("No hay reporte para mostrar.");
            btnAbrirExcel.setEnabled(false);
            rutaExcelActual = null;
            return;
        }
        areaTicket.setText(ReporteVentasPrinter.imprimir(reporte));
        ajustarVentanaAlContenido();
        if (rutaExcel != null && !rutaExcel.isEmpty()) {
            btnAbrirExcel.setEnabled(true);
            rutaExcelActual = rutaExcel;
        } else {
            btnAbrirExcel.setEnabled(false);
            rutaExcelActual = null;
        }
    }

    // NUEVO: Mostrar texto personalizado (por ejemplo, reporte de inventario)
    public void mostrarTextoPersonalizado(String texto) {
        if (texto == null || texto.isEmpty()) {
            areaTicket.setText("No hay información para mostrar.");
        } else {
            areaTicket.setText(texto);
        }
        btnAbrirExcel.setEnabled(false);
        rutaExcelActual = null;
        ajustarVentanaAlContenido();
    }

    // NUEVO: Ajusta el tamaño de la ventana al contenido del área de texto
    private void ajustarVentanaAlContenido() {
        areaTicket.setCaretPosition(0); // Ir al inicio del texto
        // Opcional: puedes ajustar el tamaño de la ventana según el contenido
        // pero siempre permitiendo scroll si el texto es muy grande
        // pack(); // Descomenta si quieres que la ventana se ajuste automáticamente
    }

    private void imprimirTicket() {
        try {
            boolean completo = areaTicket.print();
            if (completo) {
                JOptionPane.showMessageDialog(this, "Impresión completada correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "La impresión fue cancelada.", "Impresión", JOptionPane.WARNING_MESSAGE);
            }
        } catch (PrinterException ex) {
            JOptionPane.showMessageDialog(this, "Error al imprimir: " + ex.getMessage(), "Error de impresión", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirEnExcel() {
        if (rutaExcelActual != null) {
            try {
                Desktop.getDesktop().open(new File(rutaExcelActual));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "No se pudo abrir el archivo Excel: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
