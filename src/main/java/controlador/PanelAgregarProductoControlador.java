package controlador;

import vista.PanelAgregarProducto;
import clases.Producto;
import clases.Inventario;
import DAO.CategoriaDAO;
import DAO.ProveedorDAO;
import clases.Categoria;
import clases.Proveedor;
import tablesModels.ProductoTableModel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.List;

public class PanelAgregarProductoControlador {
    private final PanelAgregarProducto vista;
    private final JFrameTablaBusquedaControlador tablaBusquedaControlador;
    private final Inventario inventario;
    private final ProductoTableModel productoTableModel;

    public PanelAgregarProductoControlador(
            PanelAgregarProducto vista,
            JFrameTablaBusquedaControlador tablaBusquedaControlador,
            Inventario inventario,
            ProductoTableModel productoTableModel
    ) {
        this.vista = vista;
        this.tablaBusquedaControlador = tablaBusquedaControlador;
        this.inventario = inventario;
        this.productoTableModel = productoTableModel;
        productoTableModel.setProductos(inventario.getProductos());
        llenarComboCategorias();
        llenarComboProveedores();
        agregarListeners();
        agregarListenerSeleccionTabla();
    }

    private void llenarComboCategorias() {
        CategoriaDAO categoriaDAO = new CategoriaDAO();
        List<Categoria> categorias = categoriaDAO.obtenerTodas();
        vista.comboCategoria.removeAllItems();
        for (Categoria cat : categorias) {
            vista.comboCategoria.addItem(cat.getNombre());
        }
    }

    private void llenarComboProveedores() {
        ProveedorDAO proveedorDAO = new ProveedorDAO();
        List<Proveedor> proveedores = proveedorDAO.obtenerTodos();
        vista.comboProveedor.removeAllItems();
        for (Proveedor prov : proveedores) {
            vista.comboProveedor.addItem(prov.getNombre());
        }
    }

    private void agregarListeners() {
        vista.btnAgregar.addActionListener(e -> onAgregar());
        vista.btnEditar.addActionListener(e -> onEditar());
        vista.btnEliminar.addActionListener(e -> onEliminar());
    }

    private void agregarListenerSeleccionTabla() {
        tablaBusquedaControlador.getTabla().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    Producto producto = tablaBusquedaControlador.getProductoSeleccionado();
                    if (producto != null) {
                        llenarCamposProducto(producto);
                    }
                }
            }
        });
    }

    private void onAgregar() {
        Producto producto = obtenerProductoDeFormulario();
        if (producto == null) return;
        // Verifica si el código ya existe en el inventario
        for (Producto p : inventario.getProductos()) {
            if (p.getCodigo().equalsIgnoreCase(producto.getCodigo())) {
                JOptionPane.showMessageDialog(vista, "El código del producto ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        inventario.agregarProducto(producto);
        productoTableModel.setProductos(inventario.getProductos());
        tablaBusquedaControlador.actualizar();
        JOptionPane.showMessageDialog(vista, "Producto agregado correctamente.");
        limpiarFormulario();
    }

    private void onEditar() {
        Producto seleccionado = tablaBusquedaControlador.getProductoSeleccionado();
        if (seleccionado == null) {
            JOptionPane.showMessageDialog(vista, "Selecciona un producto para editar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Producto producto = obtenerProductoDeFormulario();
        if (producto == null) return;
        producto.setId(seleccionado.getId());
        inventario.actualizarProducto(producto);
        productoTableModel.setProductos(inventario.getProductos());
        tablaBusquedaControlador.actualizar();
        JOptionPane.showMessageDialog(vista, "Producto actualizado correctamente.");
        limpiarFormulario();
    }

    private void onEliminar() {
        Producto seleccionado = tablaBusquedaControlador.getProductoSeleccionado();
        if (seleccionado == null) {
            JOptionPane.showMessageDialog(vista, "Selecciona un producto para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(vista, "¿Seguro que deseas eliminar el producto seleccionado?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            inventario.eliminarProducto(seleccionado);
            productoTableModel.setProductos(inventario.getProductos());
            tablaBusquedaControlador.actualizar();
            JOptionPane.showMessageDialog(vista, "Producto eliminado correctamente.");
            limpiarFormulario();
        }
    }

    private void llenarCamposProducto(Producto producto) {
        vista.txtCodigo.setText(producto.getCodigo());
        vista.txtNombre.setText(producto.getNombre());
        vista.txtPrecioVenta.setText(String.valueOf(producto.getPrecioVenta()));
        vista.txtPrecioCompra.setText(String.valueOf(producto.getPrecioCompra()));
        vista.txtExistencias.setText(String.valueOf(producto.getExistencias()));
        vista.txtExistenciaMinima.setText(String.valueOf(producto.getExistenciaMinima()));
        vista.txtExistenciaMaxima.setText(String.valueOf(producto.getExistenciaMaxima()));
        vista.txtIva.setText(String.valueOf(producto.getIva()));
        vista.txtIeps.setText(String.valueOf(producto.getIeps()));

        if ("Unitario".equalsIgnoreCase(producto.getTipoProducto())) {
            vista.chkUnitario.setSelected(true);
            vista.chkGranel.setSelected(false);
        } else if ("Granel".equalsIgnoreCase(producto.getTipoProducto()) || "A granel".equalsIgnoreCase(producto.getTipoProducto())) {
            vista.chkUnitario.setSelected(false);
            vista.chkGranel.setSelected(true);
        } else {
            vista.chkUnitario.setSelected(false);
            vista.chkGranel.setSelected(false);
        }

        seleccionarComboPorNombre(vista.comboCategoria, producto.getCategoriaId(), true);
        seleccionarComboPorNombre(vista.comboProveedor, producto.getIdProveedor(), false);
    }

    private void seleccionarComboPorNombre(JComboBox<String> combo, int id, boolean esCategoria) {
        String nombre = "";
        if (esCategoria) {
            CategoriaDAO categoriaDAO = new CategoriaDAO();
            String nombreCat = categoriaDAO.obtenerNombrePorId(id);
            if (nombreCat != null) nombre = nombreCat;
        } else {
            ProveedorDAO proveedorDAO = new ProveedorDAO();
            Proveedor prov = proveedorDAO.obtenerPorId(id);
            if (prov != null) nombre = prov.getNombre();
        }
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).equals(nombre)) {
                combo.setSelectedIndex(i);
                break;
            }
        }
    }

    private Producto obtenerProductoDeFormulario() {
        try {
            String codigo = vista.txtCodigo.getText().trim();
            String nombre = vista.txtNombre.getText().trim();
            double precioVenta = Double.parseDouble(vista.txtPrecioVenta.getText().trim());
            double precioCompra = Double.parseDouble(vista.txtPrecioCompra.getText().trim());
            int categoriaId = vista.comboCategoria.getSelectedIndex() + 1;
            double existencias = Double.parseDouble(vista.txtExistencias.getText().trim());
            double existenciaMinima = Double.parseDouble(vista.txtExistenciaMinima.getText().trim());
            double existenciaMaxima = Double.parseDouble(vista.txtExistenciaMaxima.getText().trim());
            // --- CORREGIDO: Obtener el id del proveedor usando el nombre seleccionado ---
            String nombreProveedor = (String) vista.comboProveedor.getSelectedItem();
            int idProveedor = 0;
            if (nombreProveedor != null && !nombreProveedor.isEmpty()) {
                ProveedorDAO proveedorDAO = new ProveedorDAO();
                Integer id = proveedorDAO.obtenerIdPorNombre(nombreProveedor);
                if (id != null) idProveedor = id;
            }
            double iva = Double.parseDouble(vista.txtIva.getText().trim());
            double ieps = Double.parseDouble(vista.txtIeps.getText().trim());
            String tipoProducto = vista.chkUnitario.isSelected() ? "Unitario" : (vista.chkGranel.isSelected() ? "Granel" : "");

            if (codigo.isEmpty() || nombre.isEmpty() || tipoProducto.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Completa todos los campos obligatorios.", "Campos incompletos", JOptionPane.WARNING_MESSAGE);
                return null;
            }

            return new Producto(0, codigo, nombre, precioVenta, precioCompra, categoriaId, existencias, existenciaMinima, existenciaMaxima, idProveedor, iva, ieps, new java.util.Date(), tipoProducto);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Verifica los datos ingresados.", "Error de datos", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private void limpiarFormulario() {
        vista.txtCodigo.setText("");
        vista.txtNombre.setText("");
        vista.txtPrecioVenta.setText("");
        vista.txtPrecioCompra.setText("");
        vista.txtExistencias.setText("");
        vista.txtExistenciaMinima.setText("");
        vista.txtExistenciaMaxima.setText("");
        vista.txtIva.setText("");
        vista.txtIeps.setText("");
        vista.chkUnitario.setSelected(false);
        vista.chkGranel.setSelected(false);
    }
}
