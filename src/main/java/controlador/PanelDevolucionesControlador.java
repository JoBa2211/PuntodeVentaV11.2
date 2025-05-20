package controlador;

import vista.PanelDevoluciones;
import clases.Devoluciones;
import clases.Producto;
import DAO.DevolucionesDAO;
import main.Main;

import javax.swing.*;
import java.sql.Timestamp;
import java.util.List;

public class PanelDevolucionesControlador {
    private final PanelDevoluciones vista;
    private JFrameTablaBusquedaControlador tablaDevolucionesControlador; // NUEVO
    private List<Producto> productosActuales;

    public PanelDevolucionesControlador(PanelDevoluciones vista) {
        this.vista = vista;
        cargarProductosComboBox();
        agregarListeners();
    }

    // NUEVO: Permite inyectar el controlador de la tabla de devoluciones
    public void setTablaDevolucionesControlador(JFrameTablaBusquedaControlador tablaDevolucionesControlador) {
        this.tablaDevolucionesControlador = tablaDevolucionesControlador;
    }

    // Permite que el controlador externo de la tabla de productos notifique la selección
    public void enlazarTablaProductos(JFrameTablaBusquedaControlador tablaProductosControlador) {
        tablaProductosControlador.setProductoSeleccionListener(producto -> {
            if (producto != null) {
                seleccionarProductoEnCombo(producto);
            }
        });
    }

    private void cargarProductosComboBox() {
        productosActuales = main.Main.inventario.getProductos();
        vista.setProductosComboBox(productosActuales);
    }

    private void agregarListeners() {
        vista.btnAgregar.addActionListener(e -> agregarDevolucion());
        vista.btnCancelar.addActionListener(e -> limpiarCampos());

        // Actualiza el precio al seleccionar un producto en el combo
        vista.comboProductos.addActionListener(e -> actualizarPrecioDesdeCombo());
    }

    // Permite que el controlador externo de la tabla de productos notifique la selección
    public void seleccionarProductoEnCombo(Producto producto) {
        if (producto == null) return;
        String display = producto.getCodigo() + " - " + producto.getNombre() + " - $" + producto.getPrecioVenta() + " - " + producto.getExistencias();
        for (int i = 0; i < vista.comboProductos.getItemCount(); i++) {
            if (vista.comboProductos.getItemAt(i).equals(display)) {
                vista.comboProductos.setSelectedIndex(i);
                break;
            }
        }
        // Actualiza el precio al seleccionar desde la tabla
        vista.txtPrecio.setText(producto != null ? String.valueOf(producto.getPrecioVenta()) : "");
    }

    private void actualizarPrecioDesdeCombo() {
        int selectedIndex = vista.comboProductos.getSelectedIndex();
        if (selectedIndex >= 0 && productosActuales != null && selectedIndex < productosActuales.size()) {
            Producto producto = productosActuales.get(selectedIndex);
            vista.txtPrecio.setText(String.valueOf(producto.getPrecioVenta()));
        } else {
            vista.txtPrecio.setText("");
        }
    }

    private void agregarDevolucion() {
        int selectedIndex = vista.comboProductos.getSelectedIndex();
        if (selectedIndex < 0 || productosActuales == null || productosActuales.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Selecciona un producto.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Producto producto = productosActuales.get(selectedIndex);
        int cantidad = (Integer) vista.spinnerCantidad.getValue();

        // El precio se toma del producto, no del campo de texto
        double precio = producto.getPrecioVenta();

        Timestamp fecha = new Timestamp(System.currentTimeMillis());
        int idUsuario = (Main.usuarioIdActual != -1) ? Main.usuarioIdActual : 1;

        Devoluciones devolucion = new Devoluciones(
            0, producto.getNombre(), cantidad, precio, fecha, idUsuario
        );

        boolean exito = new DevolucionesDAO().insertar(devolucion);
        if (exito) {
            JOptionPane.showMessageDialog(vista, "Devolución registrada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
            // Actualiza la tabla de devoluciones si el controlador está presente
            if (tablaDevolucionesControlador != null) {
                tablaDevolucionesControlador.actualizar();
            }
        } else {
            JOptionPane.showMessageDialog(vista, "Error al registrar la devolución.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        vista.comboProductos.setSelectedIndex(-1);
        vista.spinnerCantidad.setValue(1);
        vista.txtPrecio.setText("");
    }
}
