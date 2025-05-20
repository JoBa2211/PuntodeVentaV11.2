package controlador;

import DAO.TicketDAO;
import Tables.PanelTickets;
import vista.VistaImpresionTicket;
import clases.DetalleProductoTicket;
import clases.Ticket;

import javax.swing.*;

public class PanelTicketsControlador {
    private final PanelTickets panelTickets;
    private final TicketDAO ticketDAO;
    private VistaImpresionTicket vistaImpresionTicket;

    // NUEVO: Permite enlazar un JFrameTablaBusquedaControlador externo para sincronizar la tabla de tickets
    private JFrameTablaBusquedaControlador tablaBusquedaControlador;

    public PanelTicketsControlador(PanelTickets panelTickets) {
        this.panelTickets = panelTickets;
        this.ticketDAO = new TicketDAO();
        configurarFiltro();
        cargarTicketsDia(); // Por defecto, carga los tickets del día

        // Registrar el callback para actualizar la tabla al mostrar el panel
        this.panelTickets.setActualizarTablaCallback(() -> {
            // Ejecuta la consulta según el filtro seleccionado cada vez que el panel sea visible
            String seleccion = (String) panelTickets.getFiltroComboBox().getSelectedItem();
            if (seleccion != null) {
                switch (seleccion) {
                    case "Día":
                        cargarTicketsDia();
                        break;
                    case "Semana":
                        cargarTicketsSemana();
                        break;
                    case "Mes":
                        cargarTicketsMes();
                        break;
                    case "Año":
                        cargarTicketsAnio();
                        break;
                    case "Todos":
                        cargarTicketsTodos();
                        break;
                }
            }
        });

        // NUEVO: Funcionalidad del botón borrar
        this.panelTickets.getBtnBorrar().addActionListener(e -> borrarTicketSeleccionado());
    }

    // Nuevo constructor para recibir la vista de impresión y sincronizar la selección
    public PanelTicketsControlador(PanelTickets panelTickets, VistaImpresionTicket vistaImpresionTicket) {
        this(panelTickets);
        this.vistaImpresionTicket = vistaImpresionTicket;
        sincronizarSeleccionConVistaImpresion();
    }

    public void setTablaBusquedaControlador(JFrameTablaBusquedaControlador tablaBusquedaControlador) {
        this.tablaBusquedaControlador = tablaBusquedaControlador;
    }

    // Llama a este método después de cargar tickets para actualizar la tabla externa si está enlazada
    private void actualizarTablaBusquedaExterna() {
        if (tablaBusquedaControlador != null) {
            tablaBusquedaControlador.actualizar();
        }
    }

    private void configurarFiltro() {
        JComboBox<String> filtroComboBox = panelTickets.getFiltroComboBox();
        filtroComboBox.addActionListener(e -> {
            String seleccion = (String) filtroComboBox.getSelectedItem();
            switch (seleccion) {
                case "Día":
                    cargarTicketsDia();
                    break;
                case "Semana":
                    cargarTicketsSemana();
                    break;
                case "Mes":
                    cargarTicketsMes();
                    break;
                case "Año":
                    cargarTicketsAnio();
                    break;
                case "Todos":
                    cargarTicketsTodos();
                    break;
            }
        });
    }

    public void cargarTicketsDia() {
        panelTickets.setTickets(ticketDAO.obtenerPorDiaActual());
        actualizarTablaBusquedaExterna();
    }

    public void cargarTicketsSemana() {
        panelTickets.setTickets(ticketDAO.obtenerPorSemanaActual());
        actualizarTablaBusquedaExterna();
    }

    public void cargarTicketsMes() {
        panelTickets.setTickets(ticketDAO.obtenerPorMesActual());
        actualizarTablaBusquedaExterna();
    }

    public void cargarTicketsAnio() {
        panelTickets.setTickets(ticketDAO.obtenerPorAnioActual());
        actualizarTablaBusquedaExterna();
    }

    public void cargarTicketsTodos() {
        panelTickets.setTickets(ticketDAO.obtenerTodos());
        actualizarTablaBusquedaExterna();
    }

    // Permite activar o desactivar el botón "Borrar" desde el controlador
    public void setBtnBorrarEnabled(boolean enabled) {
        panelTickets.setBtnBorrarEnabled(enabled);
    }

    // Método para sincronizar la selección de la tabla con la vista de impresión
    public void sincronizarSeleccionConVistaImpresion() {
        if (vistaImpresionTicket == null) return;
        panelTickets.getTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Ticket seleccionado = panelTickets.getTicketSeleccionado();
                if (seleccionado != null) {
                    // Consultar detalles solo al seleccionar
                    seleccionado.setDetalles(ticketDAO.obtenerDetallesPorTicketId(seleccionado.getId()));
                    // Imprimir productos del ticket en la consola
                    if (seleccionado.getDetalles() != null) {
                        System.out.println("Productos del ticket seleccionado:");
                        for (DetalleProductoTicket detalle : seleccionado.getDetalles()) {
                            System.out.println("Producto ID: " + detalle.getIdProducto() +
                                    ", Cantidad: " + detalle.getCantidad() +
                                    ", Precio: " + detalle.getPrecio());
                        }
                    }
                }
                vistaImpresionTicket.mostrarTicket(seleccionado);
            }
        });
    }

    private void borrarTicketSeleccionado() {
        Ticket seleccionado = panelTickets.getTicketSeleccionado();
        if (seleccionado == null) {
            JOptionPane.showMessageDialog(panelTickets, "Selecciona un ticket para borrar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(panelTickets,
                "¿Seguro que deseas borrar el ticket seleccionado?\nEsta acción eliminará también sus productos.",
                "Confirmar borrado", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        boolean exito = borrarTicketYDetalles(seleccionado.getId());
        if (exito) {
            JOptionPane.showMessageDialog(panelTickets, "Ticket eliminado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarTicketsDia(); // O recarga el filtro actual
        } else {
            JOptionPane.showMessageDialog(panelTickets, "No se pudo eliminar el ticket.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para borrar ticket y sus detalles
    private boolean borrarTicketYDetalles(int ticketId) {
        try (
            lib.DatabaseUniversalTranslatorFactory traductor =
                new lib.DatabaseUniversalTranslatorFactory(lib.ConnectionFactory.TipoBD.SQLSERVER, lib.ConnectionFactory.TipoBD.SQLSERVER);
            java.sql.Connection conn = lib.ConnectionFactory.getInstancia().getConexion()
        ) {
            conn.setAutoCommit(false);
            try (
                java.sql.PreparedStatement psDetalles = conn.prepareStatement("DELETE FROM TicketDetalles WHERE idTicket = ?");
                java.sql.PreparedStatement psTicket = conn.prepareStatement("DELETE FROM Tickets WHERE ticketId = ?")
            ) {
                psDetalles.setInt(1, ticketId);
                psDetalles.executeUpdate();

                psTicket.setInt(1, ticketId);
                int filas = psTicket.executeUpdate();

                conn.commit();
                return filas > 0;
            } catch (Exception e) {
                conn.rollback();
                System.err.println("[TicketDAO] Error al borrar ticket y detalles: " + e.getMessage());
                return false;
            }
        } catch (Exception e) {
            System.err.println("[TicketDAO] Error al borrar ticket y detalles: " + e.getMessage());
            return false;
        }
    }
}
