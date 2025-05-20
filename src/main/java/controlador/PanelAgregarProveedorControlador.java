package controlador;

import DAO.ProveedorDAO;
import clases.Proveedor;
import vista.PanelAgregarProveedor;
import vista.JFrameTablaBusqueda;
import Tables.ProveedorTableModel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class PanelAgregarProveedorControlador {
    private final PanelAgregarProveedor panelAgregarProveedor;
    private final JFrameTablaBusquedaControlador tablaBusquedaControlador;
    private final ProveedorDAO proveedorDAO;

    public PanelAgregarProveedorControlador(PanelAgregarProveedor panelAgregarProveedor, JFrameTablaBusquedaControlador tablaBusquedaControlador) {
        this.panelAgregarProveedor = panelAgregarProveedor;
        this.tablaBusquedaControlador = tablaBusquedaControlador;
        this.proveedorDAO = new ProveedorDAO();
        agregarListeners(); // <-- Aquí se asigna la funcionalidad a los botones
        configurarSeleccionTabla();
    }

    private void agregarListeners() {
        panelAgregarProveedor.btnAgregar.addActionListener(e -> agregarProveedor());
        panelAgregarProveedor.btnEditar.addActionListener(e -> editarProveedor());
        panelAgregarProveedor.btnEliminar.addActionListener(e -> eliminarProveedor());
    }

    private void configurarSeleccionTabla() {
        if (tablaBusquedaControlador == null) return;
        tablaBusquedaControlador.getTabla().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    Proveedor proveedor = getProveedorSeleccionado();
                    if (proveedor != null) {
                        panelAgregarProveedor.txtNombre.setText(proveedor.getNombre());
                        panelAgregarProveedor.txtTelefono.setText(proveedor.getTelefono());
                        panelAgregarProveedor.txtEmail.setText(proveedor.getMail());
                        panelAgregarProveedor.txtDireccion.setText(proveedor.getDireccion());
                    } else {
                        limpiarCampos();
                    }
                }
            }
        });
    }

    private Proveedor getProveedorSeleccionado() {
        if (tablaBusquedaControlador == null) return null;
        int row = tablaBusquedaControlador.getTabla().getSelectedRow();
        if (row >= 0 && tablaBusquedaControlador.getTabla().getModel() instanceof Tables.ProveedorTableModel) {
            Tables.ProveedorTableModel model = (Tables.ProveedorTableModel) tablaBusquedaControlador.getTabla().getModel();
            int modelRow = tablaBusquedaControlador.getTabla().convertRowIndexToModel(row);
            return model.getProveedorAt(modelRow);
        }
        return null;
    }

    private void limpiarCampos() {
        panelAgregarProveedor.txtNombre.setText("");
        panelAgregarProveedor.txtTelefono.setText("");
        panelAgregarProveedor.txtEmail.setText("");
        panelAgregarProveedor.txtDireccion.setText("");
        if (tablaBusquedaControlador != null) {
            tablaBusquedaControlador.getTabla().clearSelection();
        }
    }

    private void agregarProveedor() {
        String nombre = panelAgregarProveedor.txtNombre.getText().trim();
        String telefono = panelAgregarProveedor.txtTelefono.getText().trim();
        String mail = panelAgregarProveedor.txtEmail.getText().trim();
        String direccion = panelAgregarProveedor.txtDireccion.getText().trim();

        if (nombre.isEmpty() || telefono.isEmpty() || mail.isEmpty() || direccion.isEmpty()) {
            JOptionPane.showMessageDialog(panelAgregarProveedor, "Completa todos los campos.", "Campos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Verificar si el nombre ya existe
        if (proveedorDAO.obtenerIdPorNombre(nombre) != null) {
            JOptionPane.showMessageDialog(panelAgregarProveedor, "El nombre del proveedor ya existe. Ingresa uno diferente.", "Nombre repetido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Proveedor proveedor = new Proveedor(nombre, telefono, mail, direccion);
        if (proveedorDAO.insertar(proveedor)) {
            JOptionPane.showMessageDialog(panelAgregarProveedor, "Proveedor agregado correctamente.");
            if (tablaBusquedaControlador != null) {
                tablaBusquedaControlador.actualizar();
            }
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(panelAgregarProveedor, "Error al agregar proveedor.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarProveedor() {
        Proveedor seleccionado = getProveedorSeleccionado();
        if (seleccionado == null) {
            JOptionPane.showMessageDialog(panelAgregarProveedor, "Selecciona un proveedor para editar.", "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String nombre = panelAgregarProveedor.txtNombre.getText().trim();
        String telefono = panelAgregarProveedor.txtTelefono.getText().trim();
        String mail = panelAgregarProveedor.txtEmail.getText().trim();
        String direccion = panelAgregarProveedor.txtDireccion.getText().trim();

        if (nombre.isEmpty() || telefono.isEmpty() || mail.isEmpty() || direccion.isEmpty()) {
            JOptionPane.showMessageDialog(panelAgregarProveedor, "Completa todos los campos.", "Campos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Proveedor proveedorEditado = new Proveedor(seleccionado.getId(), nombre, telefono, mail, direccion);
        if (proveedorDAO.actualizar(proveedorEditado)) {
            JOptionPane.showMessageDialog(panelAgregarProveedor, "Proveedor actualizado correctamente.");
            if (tablaBusquedaControlador != null) {
                tablaBusquedaControlador.actualizar();
            }
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(panelAgregarProveedor, "Error al actualizar proveedor.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarProveedor() {
        Proveedor seleccionado = getProveedorSeleccionado();
        if (seleccionado == null) {
            JOptionPane.showMessageDialog(panelAgregarProveedor, "Selecciona un proveedor para eliminar.", "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(panelAgregarProveedor, "¿Seguro que deseas eliminar este proveedor?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (proveedorDAO.eliminar(seleccionado.getId())) {
                JOptionPane.showMessageDialog(panelAgregarProveedor, "Proveedor eliminado correctamente.");
                if (tablaBusquedaControlador != null) {
                    tablaBusquedaControlador.actualizar();
                }
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(panelAgregarProveedor, "Error al eliminar proveedor.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
