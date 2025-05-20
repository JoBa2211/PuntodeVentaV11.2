package tablesModels;

import clases.Producto;

import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.ArrayList;

public class ProductoVentaTableModel extends AbstractTableModel {
    private final String[] columnas = {
        "ID", "Código", "Nombre", "Cantidad", "Precio", "IVA (%)", "IEPS (%)"
    };
    private List<Producto> productos = new ArrayList<>();

    public ProductoVentaTableModel() {}

    public ProductoVentaTableModel(List<Producto> productos) {
        this.productos = new ArrayList<>(productos);
    }

    public void setProductos(List<Producto> productos) {
        this.productos = new ArrayList<>(productos);
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return productos.size();
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
        Producto p = productos.get(rowIndex);
        switch (columnIndex) {
            case 0: return p.getId();
            case 1: return p.getCodigo();
            case 2: return p.getNombre();
            case 3: return p.getExistencias();
            case 4: return p.getPrecioVenta();
            case 5: return p.getIva();
            case 6: return p.getIeps();
            default: return "";
        }
    }

    public Producto getProductoAt(int row) {
        return productos.get(row);
    }
}
