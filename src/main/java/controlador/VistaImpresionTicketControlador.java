package controlador;

import clases.Ticket;
import vista.VistaImpresionTicket;

public class VistaImpresionTicketControlador {
    private final VistaImpresionTicket vistaImpresionTicket;

    public VistaImpresionTicketControlador(VistaImpresionTicket vistaImpresionTicket) {
        this.vistaImpresionTicket = vistaImpresionTicket;
    }

    public void mostrarTicket(Ticket ticket) {
        vistaImpresionTicket.mostrarTicket(ticket);
        vistaImpresionTicket.setVisible(true);
    }
}
