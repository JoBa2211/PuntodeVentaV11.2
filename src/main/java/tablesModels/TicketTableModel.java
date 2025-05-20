package tablesModels;

import clases.Ticket;

import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.ArrayList;

public class TicketTableModel extends AbstractTableModel {
    private final String[] columnas = {
        "ID", "Fecha", "ID Usuario", "No. Ticket", "Subtotal", "Total", "IVA", "IEPS"
    };
    private List<Ticket> tickets;
    private List<Ticket> ticketsOriginales; // NUEVO

    public TicketTableModel(List<Ticket> tickets) {
        this.tickets = new ArrayList<>(tickets);
        this.ticketsOriginales = new ArrayList<>(tickets); // NUEVO
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = new ArrayList<>(tickets);
        fireTableDataChanged();
    }

    // NUEVO: Para restaurar la lista original
    public void restaurarTicketsOriginales() {
        this.tickets = new ArrayList<>(ticketsOriginales);
        fireTableDataChanged();
    }

    // NUEVO: Para acceder a la lista original
    public List<Ticket> getTicketsOriginales() {
        return ticketsOriginales;
    }

    public Ticket getTicketAt(int row) {
        return tickets.get(row);
    }

    @Override
    public int getRowCount() {
        return tickets == null ? 0 : tickets.size();
    }

    @Override
    public int getColumnCount() {
        return columnas.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnas[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Ticket t = tickets.get(rowIndex);
        switch (columnIndex) {
            case 0: return t.getId() ;
            case 1: return t.getFechaDeCreacion();
            case 2: return t.getIdUsuario();
            case 3: return t.getNumeroDeTicket();
            case 4: return t.getSubtotal();
            case 5: return t.getTotal();
            case 6: return t.getIvaTotal();
            case 7: return t.getIepsTotal();
            default: return null;
        }
    }
}
