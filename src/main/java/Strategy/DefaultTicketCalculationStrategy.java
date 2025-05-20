package Strategy;

import clases.Ticket;
import clases.DetalleProductoTicket;
import java.util.List;

public class DefaultTicketCalculationStrategy implements TicketCalculationStrategy {

    @Override
    public int calcularNumeroDeTickets(List<Ticket> tickets) {
        return tickets != null ? tickets.size() : 0;
    }

    @Override
    public double calcularSubtotal(List<Ticket> tickets) {
        double subtotal = 0.0;
        if (tickets != null) {
            for (Ticket t : tickets) {
                if (t.getDetalles() != null) {
                    for (DetalleProductoTicket d : t.getDetalles()) {
                        subtotal += d.getPrecio() * d.getCantidad();
                    }
                }
            }
        }
        return subtotal;
    }

    @Override
    public double calcularIvaTotal(List<Ticket> tickets) {
        double iva = 0.0;
        if (tickets != null) {
            for (Ticket t : tickets) {
                if (t.getDetalles() != null) {
                    for (DetalleProductoTicket d : t.getDetalles()) {
                        iva += d.getPrecio() * d.getCantidad() * (d.getIva() / 100.0);
                    }
                }
            }
        }
        return iva;
    }

    @Override
    public double calcularIepsTotal(List<Ticket> tickets) {
        double ieps = 0.0;
        if (tickets != null) {
            for (Ticket t : tickets) {
                if (t.getDetalles() != null) {
                    for (DetalleProductoTicket d : t.getDetalles()) {
                        ieps += d.getPrecio() * d.getCantidad() * (d.getIeps() / 100.0);
                    }
                }
            }
        }
        return ieps;
    }

    @Override
    public double calcularTotal(List<Ticket> tickets) {
        double total = 0.0;
        if (tickets != null) {
            for (Ticket t : tickets) {
                if (t.getDetalles() != null) {
                    for (DetalleProductoTicket d : t.getDetalles()) {
                        double base = d.getPrecio() * d.getCantidad();
                        double iva = base * (d.getIva() / 100.0);
                        double ieps = base * (d.getIeps() / 100.0);
                        total += base + iva + ieps;
                    }
                }
            }
        }
        return total;
    }
}
