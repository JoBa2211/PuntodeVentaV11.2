package tablesModels;

import clases.DetalleProductoTicket;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class DetalleProductoTicketTableModel extends AbstractTableModel {
    private final String[] columnas = {
        "ID Producto", "Nombre", "Cantidad", "Precio", "IVA", "IEPS"
    };
    private List<DetalleProductoTicket> detalles;

    public DetalleProductoTicketTableModel(List<DetalleProductoTicket> detalles) {
        this.detalles = detalles;
    }

    public void setDetalles(List<DetalleProductoTicket> detalles) {
        this.detalles = detalles;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return detalles != null ? detalles.size() : 0;
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
        DetalleProductoTicket d = detalles.get(rowIndex);
        switch (columnIndex) {
            case 0: return d.getIdProducto();
            case 1: return d.getNombre();
            case 2: return d.getCantidad();
            case 3: return d.getPrecio();
            case 4: return d.getIva();
            case 5: return d.getIeps();
            default: return "";
        }
    }

    public DetalleProductoTicket getDetalleAt(int row) {
        return detalles.get(row);
    }
}
