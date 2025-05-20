package tablesModels;

import clases.Categoria;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class CategoriaTableModel extends AbstractTableModel {
    private final String[] columnas = {"ID", "Nombre", "Notas"};
    private List<Categoria> categorias;

    public CategoriaTableModel(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    @Override
    public int getRowCount() {
        return categorias != null ? categorias.size() : 0;
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
        Categoria c = categorias.get(rowIndex);
        switch (columnIndex) {
            case 0: return c.getId();
            case 1: return c.getNombre();
            case 2: return c.getNotas();
            default: return null;
        }
    }

    public Categoria getCategoriaAt(int row) {
        return categorias.get(row);
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
        fireTableDataChanged();
    }
}
