package controlador;

import vista.PanelReabastecimientos;
import DAO.ProductoDAO;
import DAO.RefillDAO;
import clases.Producto;
import clases.Refill;
import clases.Inventario;

import javax.swing.*;
import java.util.List;

public class PanelReabastecimientosControlador {
    private final PanelReabastecimientos vista;
    private List<Producto> productosActuales;
    private final JFrameTablaBusquedaControlador tablaRefillsControlador;
    private final JFrameTablaBusquedaControlador tablaProductosControlador;
    private final Inventario inventario; // NUEVO

    public PanelReabastecimientosControlador(
            PanelReabastecimientos vista,
            JFrameTablaBusquedaControlador tablaRefillsControlador,
            JFrameTablaBusquedaControlador tablaProductosControlador,
            Inventario inventario // NUEVO
    ) {
        this.vista = vista;
        this.tablaRefillsControlador = tablaRefillsControlador;
        this.tablaProductosControlador = tablaProductosControlador;
        this.inventario = inventario;
        cargarProductosComboBox();
        agregarListeners();
    }

    private void cargarProductosComboBox() {
        productosActuales = inventario.getProductos();
        vista.setProductosComboBox(productosActuales);
    }

    private void agregarListeners() {
        vista.btnSolicitar.addActionListener(e -> solicitarReabastecimiento());
        vista.btnBorrar.addActionListener(e -> borrarRefillSeleccionado()); // Nuevo listener
    }

    private void solicitarReabastecimiento() {
        int selectedIndex = vista.comboProductos.getSelectedIndex();
        if (selectedIndex < 0 || productosActuales == null || productosActuales.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Selecciona un producto.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Producto producto = productosActuales.get(selectedIndex);
        int cantidadAgregar = (Integer) vista.spinnerCantidad.getValue();
        double cantidadActual = producto.getExistencias();
        double cantidadMaxima = producto.getExistenciaMaxima();
        double cantidadNueva = cantidadActual + cantidadAgregar;

        // Validar máximo
        if (cantidadNueva > cantidadMaxima) {
            double cantidadPermitida = cantidadMaxima - cantidadActual;
            if (cantidadPermitida <= 0) {
                JOptionPane.showMessageDialog(vista, "No puedes agregar más de " + cantidadMaxima + " unidades para este producto.", "Límite alcanzado", JOptionPane.WARNING_MESSAGE);
                return;
            } else {
                JOptionPane.showMessageDialog(vista, "Solo puedes agregar hasta " + (int)cantidadPermitida + " unidades para no exceder el máximo (" + (int)cantidadMaxima + ").", "Límite alcanzado", JOptionPane.WARNING_MESSAGE);
                cantidadAgregar = (int) cantidadPermitida;
                cantidadNueva = cantidadMaxima;
            }
        }

        // Crear objeto refill
        Refill refill = new Refill(
            0,
            producto.getId(),
            (int) cantidadActual,
            (int) cantidadNueva,
            cantidadAgregar,
            vista.txtNotas.getText(),
            1, // idUsuario, cámbialo por el usuario real si lo tienes
            new java.sql.Date(System.currentTimeMillis())
        );

        // Guardar en la base de datos
        boolean exito = new RefillDAO().insertar(refill);
        if (exito) {
            // Actualizar existencias del producto en la lista de inventario (en memoria)
            producto.setExistencias(cantidadNueva);
            inventario.actualizarProducto(producto);

            JOptionPane.showMessageDialog(vista, "Reabastecimiento realizado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarProductosComboBox(); // Refresca el combo
            if (tablaRefillsControlador != null) {
                tablaRefillsControlador.actualizar(); // Actualiza la tabla de refills
            }
            if (tablaProductosControlador != null) {
                tablaProductosControlador.actualizar(); // Actualiza la tabla de productos
            }
        } else {
            JOptionPane.showMessageDialog(vista, "Error al guardar el reabastecimiento.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        // Asegura actualización de la tabla de productos
        if (tablaProductosControlador != null) {
            tablaProductosControlador.actualizar();
        }
    }

    private void borrarRefillSeleccionado() {
        int row = tablaRefillsControlador.getTabla().getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(vista, "Selecciona un reabastecimiento en la tabla para borrar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = tablaRefillsControlador.getTabla().convertRowIndexToModel(row);
        tablesModels.RefillTableModel model = (tablesModels.RefillTableModel) tablaRefillsControlador.getTabla().getModel();
        clases.Refill refill = model.getRefillAt(modelRow);

        int confirm = JOptionPane.showConfirmDialog(
            vista,
            "¿Estás seguro de que deseas borrar este reabastecimiento?",
            "Confirmar borrado",
            JOptionPane.YES_NO_OPTION
        );
        if (confirm != JOptionPane.YES_OPTION) return;

        // Busca el producto en el inventario
        Producto producto = null;
        for (Producto p : inventario.getProductos()) {
            if (p.getId() == refill.getIdProducto()) {
                producto = p;
                System.out.println("Producto encontrado: " + producto.getId() + ", " + producto.getNombre() + ", Existencias actuales: " + producto.getExistencias());
                break;
            }
        }

        // Si el producto existe, pregunta si quiere restar del inventario
        boolean restarInventario = false;
        if (producto != null) {
            int quitarInventario = JOptionPane.showConfirmDialog(
                vista,
                "¿Deseas quitar del inventario la cantidad de productos de este reabastecimiento?",
                "Quitar del inventario",
                JOptionPane.YES_NO_OPTION
            );
            restarInventario = (quitarInventario == JOptionPane.YES_OPTION);
        }

        // Elimina el refill de la base de datos
        boolean exito = new DAO.RefillDAO().eliminar(refill.getId());
        if (exito) {
            // Si el usuario aceptó y el producto existe, descuenta la cantidad del inventario
            if (producto != null && restarInventario) {
                double existenciasActuales = producto.getExistencias();
                double cantidadARestar = refill.getCantidadNueva(); // CORRECTO: restar la cantidad original antes del refill
                double resultado = existenciasActuales - cantidadARestar;
                System.out.println("=== Proceso de resta de inventario ===");
                System.out.println("Producto: " + producto.getNombre());
                System.out.println("ID Producto: " + producto.getId());
                System.out.println("Existencias actuales: " + existenciasActuales);
                System.out.println("Cantidad a restar (cantidad): " + cantidadARestar);
                System.out.println("Resultado de la resta: " + resultado);
                if (resultado < 0) resultado = 0;
                producto.setExistencias(resultado);
                inventario.actualizarProducto(producto);
                System.out.println("Existencias finales en inventario: " + producto.getExistencias());
                System.out.println("======================================");
            }
            JOptionPane.showMessageDialog(vista, "Reabastecimiento eliminado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarProductosComboBox();
            if (tablaRefillsControlador != null) tablaRefillsControlador.actualizar();
            if (tablaProductosControlador != null) tablaProductosControlador.actualizar();
        } else {
            JOptionPane.showMessageDialog(vista, "Error al eliminar el reabastecimiento.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        // Actualiza la tabla de refills al terminar el proceso (éxito o error)
        if (tablaRefillsControlador != null) tablaRefillsControlador.actualizar();
        // Asegura actualización de la tabla de productos
        if (tablaProductosControlador != null) {
            tablaProductosControlador.actualizar();
        }
    }
}
