package controlador;

import vista.PanelAgregarCategoria;
import vista.JFrameTablaBusqueda;
import tablesModels.CategoriaTableModel;
import clases.Categoria;
import DAO.CategoriaDAO;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.List;

public class PanelAgregarCategoriaControlador {
    private final PanelAgregarCategoria panelAgregarCategoria;
    private final JFrameTablaBusquedaControlador tablaBusquedaControlador;
    private final CategoriaDAO categoriaDAO;

    public PanelAgregarCategoriaControlador(PanelAgregarCategoria panelAgregarCategoria, JFrameTablaBusquedaControlador tablaBusquedaControlador) {
        this.panelAgregarCategoria = panelAgregarCategoria;
        this.tablaBusquedaControlador = tablaBusquedaControlador;
        this.categoriaDAO = new CategoriaDAO();
        configurarAcciones();
        configurarSeleccionTabla();
        actualizarTabla();
    }

    private void configurarAcciones() {
        // Botón Agregar
        panelAgregarCategoria.btnAgregar.addActionListener(e -> {
            String nombre = panelAgregarCategoria.txtNombre.getText().trim();
            String notas = panelAgregarCategoria.txtNotas.getText().trim();
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(panelAgregarCategoria, "El nombre es obligatorio.", "Campos incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Verifica si el nombre ya existe
            if (categoriaDAO.obtenerIdPorNombre(nombre) != null) {
                JOptionPane.showMessageDialog(panelAgregarCategoria, "El nombre de la categoría ya existe. Ingresa uno diferente.", "Nombre repetido", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Categoria categoria = new Categoria(0, nombre, notas);
            if (categoriaDAO.insertar(categoria)) {
                JOptionPane.showMessageDialog(panelAgregarCategoria, "Categoría agregada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                actualizarTabla();
            } else {
                JOptionPane.showMessageDialog(panelAgregarCategoria, "Error al agregar la categoría.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Botón Editar
        panelAgregarCategoria.btnEditar.addActionListener(e -> {
            Categoria seleccionada = getCategoriaSeleccionada();
            if (seleccionada == null) {
                JOptionPane.showMessageDialog(panelAgregarCategoria, "Selecciona una categoría para editar.", "Sin selección", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String nombre = panelAgregarCategoria.txtNombre.getText().trim();
            String notas = panelAgregarCategoria.txtNotas.getText().trim();
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(panelAgregarCategoria, "El nombre es obligatorio.", "Campos incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Verifica si el nombre ya existe en otra categoría
            Integer idExistente = categoriaDAO.obtenerIdPorNombre(nombre);
            if (idExistente != null && !idExistente.equals(seleccionada.getId())) {
                JOptionPane.showMessageDialog(panelAgregarCategoria, "El nombre de la categoría ya existe. Ingresa uno diferente.", "Nombre repetido", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Categoria categoriaEditada = new Categoria(seleccionada.getId(), nombre, notas);
            boolean exito = categoriaDAO.actualizar(categoriaEditada);
            if (exito) {
                JOptionPane.showMessageDialog(panelAgregarCategoria, "Categoría editada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                actualizarTabla();
            } else {
                JOptionPane.showMessageDialog(panelAgregarCategoria, "Error al editar la categoría.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Botón Eliminar
        panelAgregarCategoria.btnEliminar.addActionListener(e -> {
            Categoria seleccionada = getCategoriaSeleccionada();
            if (seleccionada == null) {
                JOptionPane.showMessageDialog(panelAgregarCategoria, "Selecciona una categoría para eliminar.", "Sin selección", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(
                panelAgregarCategoria,
                "¿Estás seguro de que deseas eliminar la categoría \"" + seleccionada.getNombre() + "\"?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            if (confirm == JOptionPane.YES_OPTION) {
                boolean exito = categoriaDAO.eliminar(seleccionada.getId());
                if (exito) {
                    JOptionPane.showMessageDialog(panelAgregarCategoria, "Categoría eliminada correctamente.", "Eliminado", JOptionPane.INFORMATION_MESSAGE);
                    limpiarCampos();
                    actualizarTabla();
                } else {
                    JOptionPane.showMessageDialog(panelAgregarCategoria, "Error al eliminar la categoría.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void configurarSeleccionTabla() {
        tablaBusquedaControlador.getTabla().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int row = tablaBusquedaControlador.getTabla().getSelectedRow();
                    if (row >= 0 && tablaBusquedaControlador.getTabla().getModel() instanceof CategoriaTableModel) {
                        CategoriaTableModel model = (CategoriaTableModel) tablaBusquedaControlador.getTabla().getModel();
                        int modelRow = tablaBusquedaControlador.getTabla().convertRowIndexToModel(row);
                        Categoria categoria = model.getCategoriaAt(modelRow);
                        if (categoria != null) {
                            panelAgregarCategoria.txtNombre.setText(categoria.getNombre());
                            panelAgregarCategoria.txtNotas.setText(categoria.getNotas());
                        }
                    } else {
                        panelAgregarCategoria.txtNombre.setText("");
                        panelAgregarCategoria.txtNotas.setText("");
                    }
                }
            }
        });
    }

    private Categoria getCategoriaSeleccionada() {
        int row = tablaBusquedaControlador.getTabla().getSelectedRow();
        if (row >= 0 && tablaBusquedaControlador.getTabla().getModel() instanceof CategoriaTableModel) {
            CategoriaTableModel model = (CategoriaTableModel) tablaBusquedaControlador.getTabla().getModel();
            int modelRow = tablaBusquedaControlador.getTabla().convertRowIndexToModel(row);
            return model.getCategoriaAt(modelRow);
        }
        return null;
    }

    private void limpiarCampos() {
        panelAgregarCategoria.txtNombre.setText("");
        panelAgregarCategoria.txtNotas.setText("");
        tablaBusquedaControlador.getTabla().clearSelection();
    }

    private void actualizarTabla() {
        java.util.List<Categoria> categorias = categoriaDAO.obtenerTodas();
        CategoriaTableModel model = new CategoriaTableModel(categorias);
        tablaBusquedaControlador.setNuevoTableModel(model);
    }
}
