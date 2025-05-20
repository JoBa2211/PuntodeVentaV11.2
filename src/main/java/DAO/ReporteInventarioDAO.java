package DAO;

import clases.ReporteInventario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;

import lib.ConnectionFactory;

public class ReporteInventarioDAO {

    public boolean insertar(ReporteInventario reporte) {
        String sql = "INSERT INTO ReporteInventario " +
                "(fechaCreacion, idUsuario, totalProductos, productosBajoMinimo, valorTotalInventario, comentarios) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (
            Connection conn = ConnectionFactory.getInstancia().getConexion();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setDate(1, new Date(reporte.getFechaCreacion().getTime()));
            ps.setInt(2, reporte.getIdUsuario());
            ps.setInt(3, reporte.getTotalProductos());
            ps.setInt(4, reporte.getProductosBajoMinimo());
            ps.setDouble(5, reporte.getValorTotalInventario());
            ps.setString(6, reporte.getComentarios());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[ReporteInventarioDAO] Error al insertar reporte de inventario: " + e.getMessage());
            return false;
        }
    }
}
