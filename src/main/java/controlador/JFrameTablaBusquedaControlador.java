package controlador;

import vista.JFrameTablaBusqueda;

import javax.swing.*;
import javax.swing.table.TableModel;

import tablesModels.ProductoTableModel;
import clases.Producto;
import tablesModels.DevolucionesTableModel;
import DAO.DevolucionesDAO;
import tablesModels.TicketTableModel;
import clases.Ticket;
import java.util.Date;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class JFrameTablaBusquedaControlador {
    private final JFrameTablaBusqueda vista;
    private TableModel modeloActual;

    // Listener para selección de producto
    private java.util.function.Consumer<Producto> productoSeleccionListener;

    public JFrameTablaBusquedaControlador(JFrameTablaBusqueda vista, TableModel modelo) {
        this.vista = vista;
        setNuevoTableModel(modelo);
        actualizar();
    }

    public JTable getTabla() {
        return vista.getTabla();
    }

    public JTextField getBuscadorTextField() {
        return vista.getBuscadorTextField();
    }

    public void setTableModel(TableModel model) {
        modeloActual = model;
        vista.getTabla().setModel(model);
    }

    public void setNuevoTableModel(TableModel model) {
        modeloActual = model;
        vista.actualizarTabla(model);
    }

    public Producto getProductoSeleccionado() {
        int row = vista.getTabla().getSelectedRow();
        if (row >= 0 && vista.getTabla().getModel() instanceof ProductoTableModel) {
            ProductoTableModel model = (ProductoTableModel) vista.getTabla().getModel();
            int modelRow = vista.getTabla().convertRowIndexToModel(row);
            return model.getProductoAt(modelRow);
        }
        return null;
    }

    public void setProductoSeleccionListener(java.util.function.Consumer<Producto> listener) {
        this.productoSeleccionListener = listener;
        vista.getTabla().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Producto seleccionado = getProductoSeleccionado();
                if (productoSeleccionListener != null && seleccionado != null) {
                    productoSeleccionListener.accept(seleccionado);
                }
            }
        });
    }

    // Método para refrescar la tabla y recargar datos si es necesario
    public void actualizar() {
        // Si el modelo es DevolucionesTableModel, recarga los datos desde el DAO
        if (modeloActual instanceof tablesModels.DevolucionesTableModel) {
            DevolucionesTableModel model = (DevolucionesTableModel) modeloActual;
            model.setDevoluciones(new DAO.DevolucionesDAO().obtenerTodas());
        }
        // Si el modelo es de otro tipo y tiene un método similar, agrégalo aquí

        // Refresca la tabla en todos los casos
        if (modeloActual instanceof javax.swing.table.AbstractTableModel) {
            ((javax.swing.table.AbstractTableModel) modeloActual).fireTableDataChanged();
        }
    }

    // --- FILTROS DE TICKETS PARA REPORTE ---
    public void filtrarPorDia(Date fecha) {
        if (modeloActual instanceof TicketTableModel) {
            TicketTableModel model = (TicketTableModel) modeloActual;
            List<Ticket> filtrados = model.getTicketsOriginales().stream()
                .filter(t -> esMismaFecha(t.getFechaDeCreacion(), fecha))
                .collect(Collectors.toList());
            model.setTickets(filtrados);
            vista.actualizarTabla(modeloActual); // <-- Asegura refresco visual
        }
    }

    public void filtrarPorSemana(Date fecha) {
        if (modeloActual instanceof TicketTableModel) {
            TicketTableModel model = (TicketTableModel) modeloActual;
            Calendar cal = Calendar.getInstance();
            cal.setTime(fecha);
            int semana = cal.get(Calendar.WEEK_OF_YEAR);
            int anio = cal.get(Calendar.YEAR);
            List<Ticket> filtrados = model.getTicketsOriginales().stream()
                .filter(t -> {
                    Calendar c = Calendar.getInstance();
                    c.setTime(t.getFechaDeCreacion());
                    return c.get(Calendar.WEEK_OF_YEAR) == semana && c.get(Calendar.YEAR) == anio;
                })
                .collect(Collectors.toList());
            model.setTickets(filtrados);
            vista.actualizarTabla(modeloActual);
        }
    }

    public void filtrarPorMes(Date fecha) {
        if (modeloActual instanceof TicketTableModel) {
            TicketTableModel model = (TicketTableModel) modeloActual;
            Calendar cal = Calendar.getInstance();
            cal.setTime(fecha);
            int mes = cal.get(Calendar.MONTH);
            int anio = cal.get(Calendar.YEAR);
            List<Ticket> filtrados = model.getTicketsOriginales().stream()
                .filter(t -> {
                    Calendar c = Calendar.getInstance();
                    c.setTime(t.getFechaDeCreacion());
                    return c.get(Calendar.MONTH) == mes && c.get(Calendar.YEAR) == anio;
                })
                .collect(Collectors.toList());
            model.setTickets(filtrados);
            vista.actualizarTabla(modeloActual);
        }
    }

    public void filtrarPorRango(Date inicio, Date fin) {
        if (modeloActual instanceof TicketTableModel) {
            TicketTableModel model = (TicketTableModel) modeloActual;
            List<Ticket> filtrados = model.getTicketsOriginales().stream()
                .filter(t -> {
                    Date f = t.getFechaDeCreacion();
                    return !f.before(inicio) && !f.after(fin);
                })
                .collect(Collectors.toList());
            model.setTickets(filtrados);
            vista.actualizarTabla(modeloActual);
        }
    }

    public void limpiarFiltroTickets() {
        if (modeloActual instanceof TicketTableModel) {
            TicketTableModel model = (TicketTableModel) modeloActual;
            model.restaurarTicketsOriginales();
            vista.actualizarTabla(modeloActual);
        }
    }

    private boolean esMismaFecha(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(d1);
        c2.setTime(d2);
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
            && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)
            && c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH);
    }
}
