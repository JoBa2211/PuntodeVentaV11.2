package controlador;

import vista.PanelReporteVentas;
import tablesModels.TicketTableModel;
import clases.Ticket;
import DAO.TicketDAO;
import clases.ReporteVentas;
import vista.VistaImpresionTicket;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import DAO.ReporteVentasDAO;
import DAO.ProductoDAO;
import clases.Producto;
import util.ReporteVentasExcelExporter;

public class PanelReporteVentasControlador {
    private final PanelReporteVentas vista;
    private JFrameTablaBusquedaControlador tablaTicketsControlador;

    public PanelReporteVentasControlador(PanelReporteVentas vista) {
        this.vista = vista;
        agregarListeners();
        agregarListenerGenerarReporte(); // NUEVO
        cargarProductosEnCombo();
        aplicarFiltro();
    }

    public void setTablaTicketsControlador(JFrameTablaBusquedaControlador controlador) {
        this.tablaTicketsControlador = controlador;
    }

    private void agregarListeners() {
        ActionListener filtroListener = e -> aplicarFiltro();
        vista.chkDia.addActionListener(filtroListener);
        vista.chkSemana.addActionListener(filtroListener);
        vista.chkMes.addActionListener(filtroListener);
        vista.chkIntervaloPersonalizado.addActionListener(filtroListener);
        vista.comboProductos.addActionListener(filtroListener); // NUEVO

        PropertyChangeListener dateListener = evt -> {
            if ("date".equals(evt.getPropertyName())) {
                if (vista.chkIntervaloPersonalizado.isSelected()) {
                    aplicarFiltro();
                }
            }
        };
        vista.dateChooserInicio.getDateEditor().addPropertyChangeListener(dateListener);
        vista.dateChooserFin.getDateEditor().addPropertyChangeListener(dateListener);
    }

    private void agregarListenerGenerarReporte() {
        vista.btnGenerarReporte.addActionListener(e -> generarReporte());
    }

    // Llama a este método cada vez que se entra en la pestaña de reporte de ventas
    public void recargarProductosCombo() {
        cargarProductosEnCombo();
        aplicarFiltro();
    }

    private void cargarProductosEnCombo() {
        ProductoDAO productoDAO = new ProductoDAO();
        List<Producto> productos = productoDAO.obtenerTodos();
        vista.comboProductos.removeAllItems();
        vista.comboProductos.addItem("Todos los productos");
        for (Producto p : productos) {
            vista.comboProductos.addItem(p.getId() + " - " + p.getCodigo() + " - " + p.getNombre());
        }
    }

    private void aplicarFiltro() {
        if (tablaTicketsControlador == null) return;
        TicketDAO ticketDAO = new TicketDAO();
        List<Ticket> ticketsFiltrados = null;
        Date hoy = new Date();

        if (vista.chkDia.isSelected()) {
            ticketsFiltrados = ticketDAO.obtenerPorDiaActual();
        } else if (vista.chkSemana.isSelected()) {
            ticketsFiltrados = ticketDAO.obtenerPorSemanaActual();
        } else if (vista.chkMes.isSelected()) {
            ticketsFiltrados = ticketDAO.obtenerPorMesActual();
        } else if (vista.chkIntervaloPersonalizado.isSelected()) {
            Date inicio = vista.dateChooserInicio.getDate();
            Date fin = vista.dateChooserFin.getDate();
            if (inicio != null && fin != null) {
                if (!inicio.after(fin)) {
                    ticketsFiltrados = ticketDAO.obtenerPorRango(inicio, fin);
                } else {
                    JOptionPane.showMessageDialog(vista, "La fecha de inicio no puede ser posterior a la fecha de fin.", "Rango inválido", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } else {
                return;
            }
        } else {
            ticketsFiltrados = ticketDAO.obtenerTodos();
        }

        // Filtrado por producto usando el método eficiente con INNER JOIN
        String productoSeleccionado = (String) vista.comboProductos.getSelectedItem();
        Producto producto = null;
        if (productoSeleccionado != null && !"Todos los productos".equals(productoSeleccionado)) {
            String[] partes = productoSeleccionado.split(" - ");
            if (partes.length > 2) {
                try {
                    int idProducto = Integer.parseInt(partes[0]);
                    String codigo = partes[1];
                    String nombre = partes[2];
                    ProductoDAO productoDAO = new ProductoDAO();
                    // Prepara la lista de IDs de tickets
                    List<Integer> ticketIds = new java.util.ArrayList<>();
                    for (Ticket t : ticketsFiltrados) {
                        ticketIds.add(t.getId());
                    }
                    // Filtra los tickets que contienen el producto usando el método eficiente
                    List<Ticket> ticketsConProducto = new java.util.ArrayList<>();
                    for (Ticket t : ticketsFiltrados) {
                        if (productoDAO.existeProductoEnTickets(
                                java.util.Collections.singletonList(t.getId()), idProducto, codigo, nombre)) {
                            ticketsConProducto.add(t);
                        }
                    }
                    ticketsFiltrados = ticketsConProducto;
                } catch (NumberFormatException ignored) {}
            }
        }

        if (ticketsFiltrados != null) {
            TicketTableModel model = new TicketTableModel(ticketsFiltrados);
            tablaTicketsControlador.setNuevoTableModel(model);

            // Actualiza los labels de resumen
            double subtotal = 0, total = 0, iva = 0, ieps = 0;
            int cantidadProductosVendidos = 0;
            if (productoSeleccionado != null && !"Todos los productos".equals(productoSeleccionado)) {
                String[] partes = productoSeleccionado.split(" - ");
                if (partes.length > 2) {
                    try {
                        int idProducto = Integer.parseInt(partes[0]);
                        for (Ticket t : ticketsFiltrados) {
                            if (t.getDetalles() == null) {
                                t.setDetalles(ticketDAO.obtenerDetallesPorTicketId(t.getId()));
                            }
                            for (clases.DetalleProductoTicket d : t.getDetalles()) {
                                if (d.getIdProducto() == idProducto) {
                                    cantidadProductosVendidos += d.getCantidad();
                                    subtotal += d.getPrecio() * d.getCantidad();
                                    total += d.getPrecio() * d.getCantidad();
                                    iva += d.getIva() * d.getCantidad();
                                    ieps += d.getIeps() * d.getCantidad();
                                }
                            }
                        }
                    } catch (NumberFormatException ignored) {}
                }
            } else {
                for (Ticket t : ticketsFiltrados) {
                    subtotal += t.getSubtotal();
                    total += t.getTotal();
                    iva += t.getIvaTotal();
                    ieps += t.getIepsTotal();
                }
            }
            setSubtotal(subtotal);
            setIVA(iva);
            setIEPS(ieps);
            setTotal(total);
            setCantidadTickets(ticketsFiltrados.size());
        }
    }

    // Lógica para generar el reporte y mostrarlo en consola
    private void generarReporte() {
        TicketDAO ticketDAO = new TicketDAO();
        List<Ticket> ticketsFiltrados = null;
        Date hoy = new Date();
        clases.ReporteVentas.TipoReporte tipoReporte = null;

        if (vista.chkDia.isSelected()) {
            ticketsFiltrados = ticketDAO.obtenerPorDiaActual();
            tipoReporte = clases.ReporteVentas.TipoReporte.DIARIO;
        } else if (vista.chkSemana.isSelected()) {
            ticketsFiltrados = ticketDAO.obtenerPorSemanaActual();
            tipoReporte = clases.ReporteVentas.TipoReporte.SEMANAL;
        } else if (vista.chkMes.isSelected()) {
            ticketsFiltrados = ticketDAO.obtenerPorMesActual();
            tipoReporte = clases.ReporteVentas.TipoReporte.MENSUAL;
        } else if (vista.chkIntervaloPersonalizado.isSelected()) {
            Date inicio = vista.dateChooserInicio.getDate();
            Date fin = vista.dateChooserFin.getDate();
            if (inicio != null && fin != null) {
                ticketsFiltrados = ticketDAO.obtenerPorRango(inicio, fin);
                tipoReporte = null; // Intervalo personalizado
            } else {
                ticketsFiltrados = new java.util.ArrayList<>();
            }
        } else {
            ticketsFiltrados = ticketDAO.obtenerTodos();
        }

        // Filtrado por producto para el reporte
        String productoSeleccionado = (String) vista.comboProductos.getSelectedItem();
        Producto producto = null;
        if (productoSeleccionado != null && !"Todos los productos".equals(productoSeleccionado)) {
            String[] partes = productoSeleccionado.split(" - ");
            if (partes.length > 0) {
                try {
                    int idProducto = Integer.parseInt(partes[0]);
                    ProductoDAO productoDAO = new ProductoDAO();
                    producto = productoDAO.obtenerTodos().stream()
                        .filter(p -> p.getId() == idProducto)
                        .findFirst().orElse(null);
                } catch (NumberFormatException ignored) {}
            }
            if (producto != null && ticketsFiltrados != null) {
                List<Ticket> ticketsConProducto = new java.util.ArrayList<>();
                for (Ticket t : ticketsFiltrados) {
                    if (t.getDetalles() == null) {
                        t.setDetalles(ticketDAO.obtenerDetallesPorTicketId(t.getId()));
                    }
                    boolean contiene = false;
                    for (clases.DetalleProductoTicket d : t.getDetalles()) {
                        if (d.getIdProducto() == producto.getId()) {
                            contiene = true;
                            break;
                        }
                    }
                    if (contiene) {
                        ticketsConProducto.add(t);
                    }
                }
                ticketsFiltrados = ticketsConProducto;
            }
        }

        int cantidadProductosVendidos = 0;
        int cantidadTicketsCreados = ticketsFiltrados != null ? ticketsFiltrados.size() : 0;
        double total = 0, subtotal = 0, ivaTotal = 0, iepsTotal = 0;

        if (ticketsFiltrados != null && !ticketsFiltrados.isEmpty()) {
            List<Integer> ticketIds = new java.util.ArrayList<>();
            for (Ticket t : ticketsFiltrados) {
                if (t.getDetalles() == null) {
                    t.setDetalles(ticketDAO.obtenerDetallesPorTicketId(t.getId()));
                }
                ticketIds.add(t.getId());
                if (producto != null) {
                    for (clases.DetalleProductoTicket d : t.getDetalles()) {
                        if (d.getIdProducto() == producto.getId()) {
                            cantidadProductosVendidos += d.getCantidad();
                            subtotal += d.getPrecio() * d.getCantidad();
                            total += d.getPrecio() * d.getCantidad();
                            ivaTotal += d.getIva() * d.getCantidad();
                            iepsTotal += d.getIeps() * d.getCantidad();
                        }
                    }
                } else {
                    subtotal += t.getSubtotal();
                    total += t.getTotal();
                    ivaTotal += t.getIvaTotal();
                    iepsTotal += t.getIepsTotal();
                }
            }
            if (producto == null) {
                cantidadProductosVendidos = ticketDAO.contarProductosVendidosPorTickets(ticketIds);
            }
        }

        // CONVERSIÓN: de List<Ticket> a List<DetallesTicketReporte>
        List<clases.DetallesTicketReporte> detallesTickets = new java.util.ArrayList<>();
        if (ticketsFiltrados != null) {
            for (Ticket t : ticketsFiltrados) {
                if (producto != null) {
                    if (t.getDetalles() == null) {
                        t.setDetalles(ticketDAO.obtenerDetallesPorTicketId(t.getId()));
                    }
                    for (clases.DetalleProductoTicket d : t.getDetalles()) {
                        if (d.getIdProducto() == producto.getId()) {
                            // Guardar el nombre del producto en numeroTicket para el reporte
                            detallesTickets.add(new clases.DetallesTicketReporte(
                                0,
                                t.getId(),
                                d.getPrecio() * d.getCantidad(),
                                t.getFechaDeCreacion(),
                                producto.getNombre(),
                                d.getIva() * d.getCantidad(),
                                d.getIeps() * d.getCantidad()
                            ));
                        }
                    }
                } else {
                    detallesTickets.addAll(clases.ReporteVentas.convertirTicketsADetalles(java.util.Arrays.asList(t), 0));
                }
            }
        }

        ReporteVentas reporte = new ReporteVentas(
            0,
            hoy,
            cantidadProductosVendidos,
            cantidadTicketsCreados,
            total,
            subtotal,
            ivaTotal,
            iepsTotal,
            tipoReporte,
            detallesTickets
        );

        // Actualiza los labels de resumen con los valores del reporte
        setSubtotal(subtotal);
        setIVA(ivaTotal);
        setIEPS(iepsTotal);
        setTotal(total);
        setCantidadTickets(cantidadTicketsCreados);

        // Insertar el reporte y sus detalles en la base de datos
        ReporteVentasDAO reporteDAO = new ReporteVentasDAO();
        boolean insertado = reporteDAO.insertar(reporte);
        String rutaExcel = null;
        if (insertado) {
            System.out.println("Reporte de ventas insertado en la base de datos correctamente.");
            // Exportar a Excel usando la clase utilitaria
            rutaExcel = ReporteVentasExcelExporter.exportarAExcel(reporte);
        } else {
            System.out.println("No se pudo insertar el reporte de ventas en la base de datos.");
        }

        // Imprime el reporte en consola
        System.out.println(Printers.ReporteVentasPrinter.imprimir(reporte));

        // Mostrar en ventana de impresión y pasar la ruta del Excel
        VistaImpresionTicket vistaImpresion = new VistaImpresionTicket();
        vistaImpresion.mostrarReporteVentas(reporte, rutaExcel);
        vistaImpresion.setVisible(true);
    }

    // Métodos para actualizar los labels desde la lógica del reporte
    public void setSubtotal(double subtotal) {
        vista.lblSubtotal.setText("Subtotal: $" + String.format("%.2f", subtotal));
    }

    public void setIVA(double iva) {
        vista.lblIVA.setText("IVA: $" + String.format("%.2f", iva));
    }

    public void setIEPS(double ieps) {
        vista.lblIEPS.setText("IEPS: $" + String.format("%.2f", ieps));
    }

    public void setTotal(double total) {
        vista.lblTotal.setText("Total: $" + String.format("%.2f", total));
    }

    public void setCantidadTickets(int cantidad) {
        vista.lblCantidadTickets.setText("Cantidad de Tickets: " + cantidad);
    }
}
