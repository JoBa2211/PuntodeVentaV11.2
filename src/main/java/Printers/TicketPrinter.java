package Printers;

import clases.DetalleProductoTicket;
import clases.Ticket;

import java.util.List;

public class TicketPrinter {
    public static String imprimir(Ticket ticket) {
        StringBuilder sb = new StringBuilder();
        sb.append("TICKET DE VENTA\n");
        sb.append("No. Ticket: ").append(ticket.getNumeroDeTicket())
          .append("  Fecha: ").append(ticket.getFechaDeCreacion()).append("\n");
        sb.append("Usuario ID: ").append(ticket.getIdUsuario()).append("\n");
        sb.append("-------------------------------------------------------------\n");
        sb.append(String.format("%-15s %-12s %7s %10s\n", "Código", "Nombre", "Cant", "Precio"));
        sb.append("-------------------------------------------------------------\n");

        List<DetalleProductoTicket> detalles = ticket.getDetalles();

        if (detalles != null) {
            for (DetalleProductoTicket d : detalles) {
                String nombre = d.getNombre() != null ? d.getNombre() : "N/A";
                String codigo = d.getCodigo() != null ? d.getCodigo() : "";
                if (nombre.length() > 12) nombre = nombre.substring(0, 12);
                if (codigo.length() > 15) codigo = codigo.substring(0, 15);
                sb.append(String.format("%-15s %-12s %7.2f %10.2f\n",
                        codigo,
                        nombre,
                        d.getCantidad(),
                        d.getPrecio()
                ));
            }
        }

        sb.append("-------------------------------------------------------------\n");
        sb.append(String.format("Subtotal: $%.2f\n", ticket.getSubtotal()));
        sb.append(String.format("IVA:      $%.2f\n", ticket.getIvaTotal()));
        sb.append(String.format("IEPS:     $%.2f\n", ticket.getIepsTotal()));
        sb.append(String.format("TOTAL:    $%.2f\n", ticket.getTotal()));
        sb.append("=============================================================\n");

        return sb.toString();
    }
}
