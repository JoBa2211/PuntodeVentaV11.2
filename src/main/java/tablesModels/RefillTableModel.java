package tablesModels;

import clases.Refill;
import DAO.ProductoDAO;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class RefillTableModel extends AbstractTableModel {
    private final String[] columnas = {
        "ID", "ID Producto", "Nombre del Producto", "Cantidad Anterior", "Cantidad Nueva", "Cantidad Reabastecida", "Notas", "ID Usuario", "Fecha"
    };
    private List<Refill> refills;

    public RefillTableModel(List<Refill> refills) {
        this.refills = refills;
    }

    @Override
    public int getRowCount() {
        return refills != null ? refills.size() : 0;
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
        if (refills == null || rowIndex >= refills.size()) return null;
        Refill r = refills.get(rowIndex);
        switch (columnIndex) {
            case 0: return r.getId();
            case 1: return r.getIdProducto();
            case 2: // Nombre del producto
                ProductoDAO productoDAO = new ProductoDAO();
                var producto = productoDAO.obtenerPorId(r.getIdProducto());
                return producto != null ? producto.getNombre() : "Desconocido";
            case 3: return r.getCantidad();
            case 4: return r.getCantidadNueva();
            case 5: return r.getCantidadRefill();
            case 6: return r.getNotas();
            case 7: return r.getIdUsuario();
            case 8: return r.getFecha();
            default: return null;
        }
    }

    public Refill getRefillAt(int row) {
        if (refills == null || row < 0 || row >= refills.size()) return null;
        return refills.get(row);
    }

    public void setRefills(List<Refill> refills) {
        this.refills = refills;
        fireTableDataChanged();
    }
}
