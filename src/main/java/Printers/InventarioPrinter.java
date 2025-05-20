package Printers;

import clases.Producto;
import clases.Proveedor;
import DAO.ProveedorDAO;

import java.util.List;

public class InventarioPrinter {
    public static String imprimir(List<Producto> productos) {
        ProveedorDAO proveedorDAO = new ProveedorDAO();
        StringBuilder sb = new StringBuilder();
        sb.append("REPORTE DE INVENTARIO\n");
        sb.append("--------------------------------------------------------------\n");
        sb.append(String.format("%-4s %-10s %-18s %-7s %-7s %-7s %-12s\n",
                "ID", "Código", "Nombre", "Stock", "Mín", "Máx", "Proveedor"));
        sb.append("--------------------------------------------------------------\n");
        for (Producto p : productos) {
            String nombreProducto = p.getNombre();
            String nombreProveedor = "N/A";
            int idProveedor = p.getIdProveedor();
            if (idProveedor > 0) {
                Proveedor proveedor = proveedorDAO.obtenerPorId(idProveedor);
                if (proveedor != null && proveedor.getNombre() != null && !proveedor.getNombre().trim().isEmpty()) {
                    nombreProveedor = proveedor.getNombre();
                }
            }
            sb.append(String.format("%-4d %-10s %-18s %-7.2f %-7.2f %-7.2f %-12s\n",
                    p.getId(),
                    p.getCodigo(),
                    nombreProducto,
                    p.getExistencias(),
                    p.getExistenciaMinima(),
                    p.getExistenciaMaxima(),
                    nombreProveedor));
        }
        sb.append("--------------------------------------------------------------\n");
        return sb.toString();
    }
}
