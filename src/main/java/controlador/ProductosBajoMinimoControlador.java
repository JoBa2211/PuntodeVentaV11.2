package controlador;

import clases.Producto;
import clases.StockObserver;
import vista.DialogoProductosBajoMinimo;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class ProductosBajoMinimoControlador implements StockObserver {
    private final List<Producto> productosBajoMinimo = new ArrayList<>();
    private final JFrame parent;

    public ProductosBajoMinimoControlador(JFrame parent) {
        this.parent = parent;
    }

    @Override
    public void onStockBajoMinimo(Producto producto) {
        if (!productosBajoMinimo.contains(producto)) {
            productosBajoMinimo.add(producto);
        }
        // Actualiza el label de la ventana principal con la cantidad de productos bajo mínimo
        if (parent instanceof vista.VentanaPrincipal) {
            ((vista.VentanaPrincipal) parent).setProductosBajoMinimo(productosBajoMinimo.size());
        }
    }

    @Override
    public void onSinStock(Producto producto) {
        // No se agrega aquí, solo bajo mínimo
    }

    public void mostrarDialogo() {
        DialogoProductosBajoMinimo dialogo = new DialogoProductosBajoMinimo(parent, productosBajoMinimo, this);
        dialogo.setVisible(true);
    }

    public void limpiar() {
        productosBajoMinimo.clear();
        // Actualiza el label al limpiar la lista
        if (parent instanceof vista.VentanaPrincipal) {
            ((vista.VentanaPrincipal) parent).setProductosBajoMinimo(0);
        }
    }

    public List<Producto> getProductosBajoMinimo() {
        return productosBajoMinimo;
    }

    public void imprimirReporteBajoMinimo() {
        if (productosBajoMinimo.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "No hay productos bajo mínimo para imprimir.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("REPORTE DE PRODUCTOS BAJO MÍNIMO\n\n");
        sb.append(String.format("%-5s %-12s %-25s %-10s %-10s %-20s\n", "ID", "Código", "Nombre", "Stock", "Mínimo", "Proveedor"));
        sb.append("-------------------------------------------------------------------------------\n");
        DAO.ProveedorDAO proveedorDAO = new DAO.ProveedorDAO();
        for (clases.Producto p : productosBajoMinimo) {
            String proveedor = proveedorDAO.obtenerNombrePorId(p.getIdProveedor());
            sb.append(String.format("%-5d %-12s %-25s %-10.2f %-10.2f %-20s\n",
                    p.getId(), p.getCodigo(), p.getNombre(), p.getExistencias(), p.getExistenciaMinima(), proveedor));
        }
        // Mostrar la vista de impresión como un diálogo modal para que quede encima y bloquee la interacción
        vista.VistaImpresionTicket vista = new vista.VistaImpresionTicket();
        vista.setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE); // Permite interacción con otros diálogos
        vista.setAlwaysOnTop(true); // Asegura que esté al frente
        vista.mostrarTextoPersonalizado(sb.toString());
        vista.setVisible(true);
    }
}
