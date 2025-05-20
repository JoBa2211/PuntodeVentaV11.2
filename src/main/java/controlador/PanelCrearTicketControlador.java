package controlador;

import vista.PanelCrearTicket;
import vista.JFrameTablaBusqueda;
import clases.Producto;
import clases.DetalleProductoTicket;
import tablesModels.DetalleProductoTicketTableModel;
import clases.Ticket;
import DAO.TicketDAO;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import Strategy.DefaultTicketCalculationStrategy;
import clases.Inventario;
import main.Main;

import javax.swing.*;

public class PanelCrearTicketControlador {
    private final PanelCrearTicket vista;
    private final List<DetalleProductoTicket> detallesSeleccionados;
    private final DetalleProductoTicketTableModel detalleTableModel;
    private JFrameTablaBusquedaControlador tablaProductosControlador;
    private Inventario inventario; // Referencia al inventario en memoria

    public PanelCrearTicketControlador(PanelCrearTicket vista, Inventario inventario) {
        this.vista = vista;
        this.inventario = inventario;
        this.detallesSeleccionados = new ArrayList<>();
        this.detalleTableModel = new DetalleProductoTicketTableModel(detallesSeleccionados);
        vista.tablaBusquedaProductos.actualizarTabla(detalleTableModel);
        agregarListeners();
    }

    public void setTablaProductosControlador(JFrameTablaBusquedaControlador tablaProductosControlador) {
        this.tablaProductosControlador = tablaProductosControlador;
    }

    private void agregarListeners() {
        vista.btnAgregar.addActionListener(e -> agregarDetalleDesdeExternaYActualizar());
        vista.btnEliminar.addActionListener(e -> eliminarDetalleSeleccionado());
        vista.btnCancelar.addActionListener(e -> cancelarVentaYActualizar());
        vista.btnCompletarVenta.addActionListener(e -> completarVenta());
    }

    private void agregarDetalleDesdeExternaYActualizar() {
        if (tablaProductosControlador == null) {
            JOptionPane.showMessageDialog(vista, "No se ha enlazado la tabla de productos externa.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Producto producto = tablaProductosControlador.getProductoSeleccionado();
        if (producto == null) {
            JOptionPane.showMessageDialog(vista, "Selecciona un producto en la tabla de productos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int cantidadAgregar = (Integer) vista.spinnerCantidad.getValue();
        // Validar que la cantidad no sea 0
        if (cantidadAgregar <= 0) {
            JOptionPane.showMessageDialog(vista, "La cantidad debe ser mayor a 0.", "Cantidad inválida", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // NUEVO: Validar que la cantidad no sea mayor al stock disponible
        double cantidadEnTicket = 0;
        for (DetalleProductoTicket d : detallesSeleccionados) {
            if (d.getIdProducto() == producto.getId()) {
                cantidadEnTicket = d.getCantidad();
                break;
            }
        }
        double stockDisponible = producto.getExistencias() - cantidadEnTicket;
        if (cantidadAgregar > stockDisponible) {
            JOptionPane.showMessageDialog(vista, "No puedes agregar más de la existencia disponible (" + stockDisponible + ").", "Stock insuficiente", JOptionPane.ERROR_MESSAGE);
            return;
        }
        boolean encontrado = false;
        for (DetalleProductoTicket d : detallesSeleccionados) {
            if (d.getIdProducto() == producto.getId()) {
                d.setCantidad(d.getCantidad() + cantidadAgregar);
                encontrado = true;
                break;
            }
        }
        if (!encontrado) {
            DetalleProductoTicket nuevoDetalle = new DetalleProductoTicket(
                producto.getId(),
                producto.getCodigo(),
                cantidadAgregar,
                producto.getPrecioVenta(),
                producto.getNombre(),
                producto.getIva(),
                producto.getIeps()
            );
            System.out.println("DetalleProductoTicket creado: id=" + nuevoDetalle.getIdProducto() +
                ", codigo=" + nuevoDetalle.getCodigo() +
                ", nombre=" + nuevoDetalle.getNombre() +
                ", cantidad=" + nuevoDetalle.getCantidad() +
                ", precio=" + nuevoDetalle.getPrecio());
            detallesSeleccionados.add(nuevoDetalle);
        }

        // Imprime la lista de detalles seleccionados en consola
        System.out.println("Detalles seleccionados:");
        for (DetalleProductoTicket d : detallesSeleccionados) {
            System.out.println("ID: " + d.getIdProducto() + ", Código: " + d.getCodigo() + ", Nombre: " + d.getNombre() + ", Cantidad: " + d.getCantidad());
        }
        detalleTableModel.fireTableDataChanged();
        vista.tablaBusquedaProductos.actualizarTabla(detalleTableModel);
    }

    private void eliminarDetalleSeleccionado() {
        int row = vista.tablaBusquedaProductos.getTabla().getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(vista, "Selecciona un producto en la tabla para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = vista.tablaBusquedaProductos.getTabla().convertRowIndexToModel(row);
        if (modelRow < 0 || modelRow >= detallesSeleccionados.size()) return;

        DetalleProductoTicket detalle = detallesSeleccionados.get(modelRow);
        double cantidadActual = detalle.getCantidad();

        if (cantidadActual <= 2) {
            // Si la cantidad es 2 o menos, solo borra 1
            if (cantidadActual <= 1) {
                detallesSeleccionados.remove(modelRow);
            } else {
                detalle.setCantidad(cantidadActual - 1);
            }
        } else {
            // Si son más de 2, pregunta cuántos quiere borrar
            String input = JOptionPane.showInputDialog(vista, "¿Cuántos deseas borrar? (Máximo: " + (int)cantidadActual + ")", "Eliminar cantidad", JOptionPane.QUESTION_MESSAGE);
            if (input == null) return; // Cancelado
            try {
                int cantidadBorrar = Integer.parseInt(input);
                if (cantidadBorrar <= 0) return;
                if (cantidadBorrar >= cantidadActual) {
                    detallesSeleccionados.remove(modelRow);
                } else {
                    detalle.setCantidad(cantidadActual - cantidadBorrar);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(vista, "Cantidad inválida.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        detalleTableModel.fireTableDataChanged();
        vista.tablaBusquedaProductos.actualizarTabla(detalleTableModel);
    }

    private void cancelarVentaYActualizar() {
        detallesSeleccionados.clear();
        detalleTableModel.fireTableDataChanged();
        vista.tablaBusquedaProductos.actualizarTabla(detalleTableModel);
    }

    private void completarVenta() {
        if (detallesSeleccionados.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "No hay productos en el ticket.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Crear el objeto Ticket usando el constructor que calcula los totales
        Date fecha = new Date(System.currentTimeMillis());
        int idUsuario = Main.usuarioIdActual; // Usa el id del usuario actual
        String numeroDeTicket = "T-" + System.currentTimeMillis(); // Ejemplo de número de ticket único

        Ticket ticket = new Ticket(
            0, // id (se asignará en la BD)
            fecha,
            idUsuario,
            numeroDeTicket,
            new ArrayList<>(detallesSeleccionados),
            new DefaultTicketCalculationStrategy()
        );

        // Descontar existencias en el inventario en memoria
        for (DetalleProductoTicket detalle : detallesSeleccionados) {
            for (clases.Producto producto : inventario.getProductos()) {
                if (producto.getId() == detalle.getIdProducto()) {
                    double nuevaExistencia = producto.getExistencias() - detalle.getCantidad();
                    producto.setExistencias(nuevaExistencia);
                    break;
                }
            }
        }

        // Guardar en la base de datos
        TicketDAO ticketDAO = new TicketDAO();
        boolean exito = ticketDAO.insertar(ticket);

        if (exito) {
            JOptionPane.showMessageDialog(vista, "¡Venta completada y guardada exitosamente!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            detallesSeleccionados.clear();
            detalleTableModel.fireTableDataChanged();
            vista.tablaBusquedaProductos.actualizarTabla(detalleTableModel);
        } else {
            JOptionPane.showMessageDialog(vista, "Error al guardar el ticket en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public List<DetalleProductoTicket> getDetallesSeleccionados() {
        return detallesSeleccionados;
    }
}
