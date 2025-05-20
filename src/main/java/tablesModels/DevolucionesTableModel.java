package tablesModels;

import clases.Devoluciones;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class DevolucionesTableModel extends AbstractTableModel {
    private final String[] columnas = {"ID", "Nombre", "Cantidad", "Precio", "Fecha", "ID Usuario"};
    private List<Devoluciones> devoluciones;

    public DevolucionesTableModel(List<Devoluciones> devoluciones) {
        this.devoluciones = devoluciones;
    }

    @Override
    public int getRowCount() {
        return devoluciones != null ? devoluciones.size() : 0;
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
        if (devoluciones == null || rowIndex >= devoluciones.size()) return null;
        Devoluciones d = devoluciones.get(rowIndex);
        switch (columnIndex) {
            case 0: return d.getId();
            case 1: return d.getNombre();
            case 2: return d.getCantidad();
            case 3: return d.getPrecio();
            case 4: return d.getFecha();
            case 5: return d.getIdUsuario();
            default: return null;
        }
    }

    public Devoluciones getDevolucionAt(int row) {
        if (devoluciones == null || row < 0 || row >= devoluciones.size()) return null;
        return devoluciones.get(row);
    }

    public void setDevoluciones(List<Devoluciones> devoluciones) {
        this.devoluciones = devoluciones;
        fireTableDataChanged();
    }
}
