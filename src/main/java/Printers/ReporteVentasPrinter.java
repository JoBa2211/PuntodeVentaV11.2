package Printers;

import clases.ReporteVentas;
import clases.DetallesTicketReporte;
import java.util.List;

public class ReporteVentasPrinter {
    public static String imprimir(ReporteVentas reporte) {
        StringBuilder sb = new StringBuilder();
        sb.append("REPORTE DE VENTAS\n");
        sb.append("Fecha de creación: ").append(reporte.getFechaDeCreacion()).append("\n");

        // NUEVO: Mostrar tipo de reporte con producto si aplica
        String nombreProducto = null;
        if (reporte.getDetallesTickets() != null && !reporte.getDetallesTickets().isEmpty()) {
            // Si todos los detalles tienen el mismo producto, lo mostramos
            // (esto requiere que guardes el nombre del producto en algún lado si es por producto)
            // Aquí solo mostramos el nombre si lo tienes, si no, omite esta parte
            // nombreProducto = ...;
        }
        String tipo = "";
        if (reporte.getTipoReporte() != null) {
            tipo = reporte.getTipoReporte().name();
        } else {
            // Si es intervalo personalizado, muestra el intervalo
            List<DetallesTicketReporte> detalles = reporte.getDetallesTickets();
            String intervalo = "";
            if (detalles != null && !detalles.isEmpty()) {
                Object inicio = detalles.stream().map(DetallesTicketReporte::getFechaCreacion).min(java.util.Date::compareTo).orElse(null);
                Object fin = detalles.stream().map(DetallesTicketReporte::getFechaCreacion).max(java.util.Date::compareTo).orElse(null);
                if (inicio != null && fin != null) {
                    intervalo = "Intervalo de fecha: " + inicio + " a " + fin;
                }
            }
            tipo = intervalo.isEmpty() ? "Intervalo de fecha personalizado" : intervalo;
        }

        // Si tienes el nombre del producto, inclúyelo en el tipo de reporte
        if (nombreProducto != null && !nombreProducto.isEmpty()) {
            sb.append("Tipo de reporte: Por Producto ").append(nombreProducto).append(" - ").append(tipo).append("\n");
        } else {
            sb.append("Tipo de reporte: ").append(tipo).append("\n");
        }

        sb.append("Cantidad de productos vendidos: ").append(reporte.getCantidadProductosVendidos()).append("\n");
        sb.append("Cantidad de tickets: ").append(reporte.getCantidadTicketsCreados()).append("\n");
        sb.append("Subtotal: $").append(String.format("%.2f", reporte.getSubtotal())).append("\n");
        sb.append("IVA: $").append(String.format("%.2f", reporte.getIvaTotal())).append("\n");
        sb.append("IEPS: $").append(String.format("%.2f", reporte.getIepsTotal())).append("\n");
        sb.append("Total: $").append(String.format("%.2f", reporte.getTotal())).append("\n\n");

        sb.append("Tickets:\n");
        sb.append(String.format("%-10s %-20s %-15s %-10s %-10s %-10s\n", "ID Ticket", "Fecha", "Num. Ticket", "Total", "IVA", "IEPS"));
        sb.append("-------------------------------------------------------------------------------\n");
        List<DetallesTicketReporte> detalles = reporte.getDetallesTickets();
        if (detalles != null) {
            for (DetallesTicketReporte d : detalles) {
                sb.append(String.format("%-10d %-20s %-15s %-10.2f %-10.2f %-10.2f\n",
                        d.getIdTicket(),
                        d.getFechaCreacion(),
                        d.getNumeroTicket(),
                        d.getTotal(),
                        d.getIva(),
                        d.getIeps()
                ));
            }
        }
        sb.append("-------------------------------------------------------------------------------\n");
        return sb.toString();
    }
}
