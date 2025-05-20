package Tables;

import clases.Proveedor;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class ProveedorTableModel extends AbstractTableModel {
    private final String[] columnas = {"ID", "Nombre", "Teléfono", "Email", "Dirección"};
    private List<Proveedor> proveedores;

    public ProveedorTableModel(List<Proveedor> proveedores) {
        this.proveedores = proveedores;
    }

    public void setProveedores(List<Proveedor> proveedores) {
        this.proveedores = proveedores;
        fireTableDataChanged();
    }

    public Proveedor getProveedorAt(int row) {
        return proveedores.get(row);
    }

    @Override
    public int getRowCount() {
        return proveedores == null ? 0 : proveedores.size();
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
        Proveedor p = proveedores.get(rowIndex);
        switch (columnIndex) {
            case 0: return p.getId();
            case 1: return p.getNombre();
            case 2: return p.getTelefono();
            case 3: return p.getMail();
            case 4: return p.getDireccion();
            default: return null;
        }
    }
}
