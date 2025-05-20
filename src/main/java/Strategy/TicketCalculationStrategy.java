package Strategy;

import clases.Ticket;
import java.util.List;

public interface TicketCalculationStrategy {
    int calcularNumeroDeTickets(List<Ticket> tickets);
    double calcularSubtotal(List<Ticket> tickets);
    double calcularIvaTotal(List<Ticket> tickets);
    double calcularIepsTotal(List<Ticket> tickets);
    double calcularTotal(List<Ticket> tickets);
}
