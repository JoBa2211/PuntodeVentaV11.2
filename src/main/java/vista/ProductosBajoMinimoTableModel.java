package vista;

import clases.Producto;
import DAO.ProveedorDAO;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class ProductosBajoMinimoTableModel extends AbstractTableModel {
    private final List<Producto> productos;
    private final String[] columnas = {"ID", "Código", "Nombre", "Existencias", "Mínimo", "Proveedor"};
    private final ProveedorDAO proveedorDAO = new ProveedorDAO();

    public ProductosBajoMinimoTableModel(List<Producto> productos) {
        this.productos = productos;
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
            case 4: return p.getExistenciaMinima();
            case 5:
                return obtenerNombreProveedorSeguro(p.getIdProveedor());
            default: return "";
        }
    }

    // Método auxiliar para obtener el nombre del proveedor por id
    private String obtenerNombreProveedorSeguro(int idProveedor) {
        if (idProveedor > 0) {
            String nombre = proveedorDAO.obtenerNombrePorId(idProveedor);
            return (nombre != null && !nombre.isEmpty()) ? nombre : String.valueOf(idProveedor);
        } else {
            return "N/A";
        }
    }
}
