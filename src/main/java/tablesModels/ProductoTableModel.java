package tablesModels;

import clases.Producto;
import DAO.ProveedorDAO;

import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductoTableModel extends AbstractTableModel {
    private final String[] columnas = {
        "ID", "Código", "Nombre", "Precio Venta", "Stock", "Mínimo", "Máximo", "Proveedor"
    };
    private List<Producto> productos;

    // Cache para evitar múltiples consultas por el mismo proveedor
    private final Map<Integer, String> proveedorCache = new HashMap<>();

    public ProductoTableModel(List<Producto> productos) {
        this.productos = productos;
    }

    public ProductoTableModel() {
        this.productos = new ArrayList<>();
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
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
            case 3: return p.getPrecioVenta();
            case 4: return p.getExistencias();
            case 5: return p.getExistenciaMinima();
            case 6: return p.getExistenciaMaxima();
            case 7: {
                int idProveedor = p.getIdProveedor();
                if (idProveedor > 0) {
                    if (!proveedorCache.containsKey(idProveedor)) {
                        ProveedorDAO proveedorDAO = new ProveedorDAO();
                        String nombre = proveedorDAO.obtenerNombrePorId(idProveedor);
                        proveedorCache.put(idProveedor, nombre != null ? nombre : "N/A");
                    }
                    return proveedorCache.get(idProveedor);
                } else {
                    return "N/A";
                }
            }
            default: return "";
        }
    }

    public Producto getProductoAt(int row) {
        return productos.get(row);
    }

    public java.util.List<clases.Producto> getProductos() {
        return productos;
    }
}
