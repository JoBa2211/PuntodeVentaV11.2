package DAO;

import clases.Producto;
import lib.ConnectionFactory;
import lib.DatabaseUniversalTranslatorFactory;
import lib.SQLQueries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    public List<Producto> obtenerTodos() {
        List<Producto> productos = new ArrayList<>();
        try (
            DatabaseUniversalTranslatorFactory traductor =
                new DatabaseUniversalTranslatorFactory(ConnectionFactory.TipoBD.SQLSERVER, ConnectionFactory.TipoBD.SQLSERVER);
            Connection conn = ConnectionFactory.getInstancia().getConexion();
            PreparedStatement ps = conn.prepareStatement(SQLQueries.SELECT_ALL_PRODUCTS);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                Producto producto = new Producto(
                    rs.getInt("id"),
                    rs.getString("codigo"),
                    rs.getString("nombre"),
                    rs.getDouble("precioVenta"),
                    rs.getDouble("precioCompra"),
                    rs.getInt("categoriaId"),
                    rs.getDouble("existencias"),
                    rs.getDouble("existenciaMinima"),
                    rs.getDouble("existenciaMaxima"),
                    rs.getInt("idProveedor"),
                    rs.getDouble("iva"),
                    rs.getDouble("ieps"),
                    rs.getDate("fechaDeCreacion"),
                    rs.getString("tipoProducto")
                );
                productos.add(producto);
            }
        } catch (SQLException e) {
            System.err.println("[ProductoDAO] Error al obtener productos: " + e.getMessage());
        }
        return productos;
    }

    public Producto obtenerPorId(int id) {
        try (
            DatabaseUniversalTranslatorFactory traductor =
                new DatabaseUniversalTranslatorFactory(ConnectionFactory.TipoBD.SQLSERVER, ConnectionFactory.TipoBD.SQLSERVER);
            Connection conn = ConnectionFactory.getInstancia().getConexion();
            PreparedStatement ps = conn.prepareStatement(SQLQueries.SELECT_PRODUCT_BY_ID)
        ) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Producto(
                        rs.getInt("id"),
                        rs.getString("codigo"),
                        rs.getString("nombre"),
                        rs.getDouble("precioVenta"),
                        rs.getDouble("precioCompra"),
                        rs.getInt("categoriaId"),
                        rs.getDouble("existencias"),
                        rs.getDouble("existenciaMinima"),
                        rs.getDouble("existenciaMaxima"),
                        rs.getInt("idProveedor"),
                        rs.getDouble("iva"),
                        rs.getDouble("ieps"),
                        rs.getDate("fechaDeCreacion"),
                        rs.getString("tipoProducto")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("[ProductoDAO] Error al obtener producto por ID: " + e.getMessage());
        }
        return null;
    }

    public boolean insertar(Producto producto) {
        try (
            DatabaseUniversalTranslatorFactory traductor =
                new DatabaseUniversalTranslatorFactory(ConnectionFactory.TipoBD.SQLSERVER, ConnectionFactory.TipoBD.SQLSERVER);
            Connection conn = ConnectionFactory.getInstancia().getConexion();
            PreparedStatement ps = conn.prepareStatement(SQLQueries.INSERT_PRODUCT)
        ) {
            ps.setString(1, producto.getCodigo());
            ps.setString(2, producto.getNombre());
            ps.setDouble(3, producto.getPrecioVenta());
            ps.setDouble(4, producto.getPrecioCompra());
            ps.setInt(5, producto.getCategoriaId());
            ps.setDouble(6, producto.getExistencias());
            ps.setDouble(7, producto.getExistenciaMinima());
            ps.setDouble(8, producto.getExistenciaMaxima());
            ps.setInt(9, producto.getIdProveedor());
            ps.setDouble(10, producto.getIva());
            ps.setDouble(11, producto.getIeps());
            ps.setDate(12, new java.sql.Date(producto.getFechaDeCreacion().getTime()));
            ps.setString(13, producto.getTipoProducto());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[ProductoDAO] Error al insertar producto: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Producto producto) {
        try (
            DatabaseUniversalTranslatorFactory traductor =
                new DatabaseUniversalTranslatorFactory(ConnectionFactory.TipoBD.SQLSERVER, ConnectionFactory.TipoBD.SQLSERVER);
            Connection conn = ConnectionFactory.getInstancia().getConexion();
            PreparedStatement ps = conn.prepareStatement(SQLQueries.UPDATE_PRODUCT)
        ) {
            ps.setString(1, producto.getCodigo());
            ps.setString(2, producto.getNombre());
            ps.setDouble(3, producto.getPrecioVenta());
            ps.setDouble(4, producto.getPrecioCompra());
            ps.setInt(5, producto.getCategoriaId());
            ps.setDouble(6, producto.getExistencias());
            ps.setDouble(7, producto.getExistenciaMinima());
            ps.setDouble(8, producto.getExistenciaMaxima());
            ps.setInt(9, producto.getIdProveedor());
            ps.setDouble(10, producto.getIva());
            ps.setDouble(11, producto.getIeps());
            ps.setDate(12, new java.sql.Date(producto.getFechaDeCreacion().getTime()));
            ps.setString(13, producto.getTipoProducto());
            ps.setInt(14, producto.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[ProductoDAO] Error al actualizar producto: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int id) {
        try (
            DatabaseUniversalTranslatorFactory traductor =
                new DatabaseUniversalTranslatorFactory(ConnectionFactory.TipoBD.SQLSERVER, ConnectionFactory.TipoBD.SQLSERVER);
            Connection conn = ConnectionFactory.getInstancia().getConexion();
            PreparedStatement ps = conn.prepareStatement(SQLQueries.DELETE_PRODUCT)
        ) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[ProductoDAO] Error al eliminar producto: " + e.getMessage());
            return false;
        }
    }

    public boolean existeCodigo(String codigo) {
        try (
            DatabaseUniversalTranslatorFactory traductor =
                new DatabaseUniversalTranslatorFactory(ConnectionFactory.TipoBD.SQLSERVER, ConnectionFactory.TipoBD.SQLSERVER);
            Connection conn = ConnectionFactory.getInstancia().getConexion();
            PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM Productos WHERE codigo = ?")
        ) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("[ProductoDAO] Error al verificar código: " + e.getMessage());
        }
        return false;
    }

    public List<Producto> obtenerPorCategoriaNombre(String nombreCategoria) {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT p.* FROM Productos p JOIN Categorias c ON p.categoriaId = c.id WHERE c.nombre = ?";
        try (
            DatabaseUniversalTranslatorFactory traductor =
                new DatabaseUniversalTranslatorFactory(ConnectionFactory.TipoBD.SQLSERVER, ConnectionFactory.TipoBD.SQLSERVER);
            Connection conn = ConnectionFactory.getInstancia().getConexion();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, nombreCategoria);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Producto producto = new Producto(
                        rs.getInt("id"),
                        rs.getString("codigo"),
                        rs.getString("nombre"),
                        rs.getDouble("precioVenta"),
                        rs.getDouble("precioCompra"),
                        rs.getInt("categoriaId"),
                        rs.getDouble("existencias"),
                        rs.getDouble("existenciaMinima"),
                        rs.getDouble("existenciaMaxima"),
                        rs.getInt("idProveedor"),
                        rs.getDouble("iva"),
                        rs.getDouble("ieps"),
                        rs.getDate("fechaDeCreacion"),
                        rs.getString("tipoProducto")
                    );
                    productos.add(producto);
                }
            }
        } catch (SQLException e) {
            System.err.println("[ProductoDAO] Error al obtener productos por categoría: " + e.getMessage());
        }
        return productos;
    }

    /**
     * Obtiene todos los productos que aparecen en una lista de tickets (por sus IDs).
     */
    public List<Producto> obtenerProductosPorTickets(List<Integer> ticketIds) {
        List<Producto> productos = new ArrayList<>();
        if (ticketIds == null || ticketIds.isEmpty()) return productos;
        StringBuilder placeholders = new StringBuilder();
        for (int i = 0; i < ticketIds.size(); i++) {
            placeholders.append("?");
            if (i < ticketIds.size() - 1) placeholders.append(",");
        }
        String sql = "SELECT DISTINCT p.* FROM Productos p " +
                     "JOIN TicketDetalles td ON p.id = td.idProducto " +
                     "WHERE td.idTicket IN (" + placeholders + ")";
        try (
            Connection conn = ConnectionFactory.getInstancia().getConexion();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            for (int i = 0; i < ticketIds.size(); i++) {
                ps.setInt(i + 1, ticketIds.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Producto p = new Producto(
                        rs.getInt("id"),
                        rs.getString("codigo"),
                        rs.getString("nombre"),
                        rs.getDouble("precioVenta"),
                        rs.getDouble("precioCompra"),
                        rs.getInt("categoriaId"),
                        rs.getInt("existencias"),
                        rs.getInt("existenciaMinima"),
                        rs.getInt("existenciaMaxima"),
                        rs.getInt("idProveedor"),
                        rs.getDouble("iva"),
                        rs.getDouble("ieps"),
                        rs.getTimestamp("fechaDeCreacion"),
                        rs.getString("tipoProducto")
                    );
                    productos.add(p);
                }
            }
        } catch (Exception e) {
            System.err.println("[ProductoDAO] Error al obtener productos por tickets: " + e.getMessage());
        }
        return productos;
    }

    /**
     * Verifica si un producto con id, código y nombre está presente en los tickets dados.
     */
    public boolean existeProductoEnTickets(List<Integer> ticketIds, int idProducto, String codigo, String nombre) {
        if (ticketIds == null || ticketIds.isEmpty()) return false;
        StringBuilder placeholders = new StringBuilder();
        for (int i = 0; i < ticketIds.size(); i++) {
            placeholders.append("?");
            if (i < ticketIds.size() - 1) placeholders.append(",");
        }
        String sql = "SELECT COUNT(*) FROM TicketDetalles td " +
                     "INNER JOIN Productos p ON td.idProducto = p.id " +
                     "WHERE td.idTicket IN (" + placeholders + ") " +
                     "AND p.id = ? AND p.codigo = ? AND p.nombre = ?";
        try (
            Connection conn = ConnectionFactory.getInstancia().getConexion();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            int idx = 1;
            for (Integer ticketId : ticketIds) {
                ps.setInt(idx++, ticketId);
            }
            ps.setInt(idx++, idProducto);
            ps.setString(idx++, codigo);
            ps.setString(idx, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (Exception e) {
            System.err.println("[ProductoDAO] Error al verificar producto en tickets: " + e.getMessage());
        }
        return false;
    }
}
