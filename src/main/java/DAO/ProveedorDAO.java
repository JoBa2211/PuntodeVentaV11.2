package DAO;

import clases.Proveedor;
import lib.ConnectionFactory;
import lib.DatabaseUniversalTranslatorFactory;
import lib.SQLQueries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProveedorDAO {

    public boolean insertar(Proveedor proveedor) {
        try (
            DatabaseUniversalTranslatorFactory traductor =
                new DatabaseUniversalTranslatorFactory(ConnectionFactory.TipoBD.SQLSERVER, ConnectionFactory.TipoBD.SQLSERVER);
            Connection conn = ConnectionFactory.getInstancia().getConexion();
            PreparedStatement ps = conn.prepareStatement(SQLQueries.INSERT_PROVEEDOR)
        ) {
            ps.setString(1, proveedor.getNombre());
            ps.setString(2, proveedor.getTelefono());
            ps.setString(3, proveedor.getDireccion());
            ps.setString(4, proveedor.getMail());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[ProveedorDAO] Error al insertar proveedor: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Proveedor proveedor) {
        try (
            DatabaseUniversalTranslatorFactory traductor =
                new DatabaseUniversalTranslatorFactory(ConnectionFactory.TipoBD.SQLSERVER, ConnectionFactory.TipoBD.SQLSERVER);
            Connection conn = ConnectionFactory.getInstancia().getConexion();
            PreparedStatement ps = conn.prepareStatement(SQLQueries.UPDATE_PROVEEDOR)
        ) {
            ps.setString(1, proveedor.getNombre());
            ps.setString(2, proveedor.getTelefono());
            ps.setString(3, proveedor.getDireccion());
            ps.setString(4, proveedor.getMail());
            ps.setInt(5, proveedor.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[ProveedorDAO] Error al actualizar proveedor: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int id) {
        try (
            DatabaseUniversalTranslatorFactory traductor =
                new DatabaseUniversalTranslatorFactory(ConnectionFactory.TipoBD.SQLSERVER, ConnectionFactory.TipoBD.SQLSERVER);
            Connection conn = ConnectionFactory.getInstancia().getConexion();
            PreparedStatement ps = conn.prepareStatement(SQLQueries.DELETE_PROVEEDOR)
        ) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[ProveedorDAO] Error al eliminar proveedor: " + e.getMessage());
            return false;
        }
    }

    public List<Proveedor> obtenerTodos() {
        List<Proveedor> proveedores = new ArrayList<>();
        try (
            DatabaseUniversalTranslatorFactory traductor =
                new DatabaseUniversalTranslatorFactory(ConnectionFactory.TipoBD.SQLSERVER, ConnectionFactory.TipoBD.SQLSERVER);
            Connection conn = ConnectionFactory.getInstancia().getConexion();
            PreparedStatement ps = conn.prepareStatement(SQLQueries.SELECT_ALL_PROVEEDORES);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                Proveedor proveedor = new Proveedor(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("telefono"),
                    rs.getString("email"),
                    rs.getString("direccion")
                );
                proveedores.add(proveedor);
            }
        } catch (SQLException e) {
            System.err.println("[ProveedorDAO] Error al obtener proveedores: " + e.getMessage());
        }
        return proveedores;
    }

    public Proveedor obtenerPorId(int id) {
                try (
                        DatabaseUniversalTranslatorFactory traductor =
                                new DatabaseUniversalTranslatorFactory(ConnectionFactory.TipoBD.SQLSERVER, ConnectionFactory.TipoBD.SQLSERVER);
                        Connection conn = ConnectionFactory.getInstancia().getConexion();
                        PreparedStatement ps = conn.prepareStatement(SQLQueries.SELECT_PROVEEDOR_BY_ID)
                ) {
                    ps.setInt(1, id);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            return new Proveedor(
                                    rs.getInt("id"),
                                    rs.getString("nombre"),
                                    rs.getString("telefono"),
                                    rs.getString("email"),
                                    rs.getString("direccion")
                            );
                        }
                    }
        } catch (SQLException e) {
            System.err.println("[ProveedorDAO] Error al obtener proveedor por ID: " + e.getMessage());
        }
        return null;
    }

    public Integer obtenerIdPorNombre(String nombre) {
        try (
            DatabaseUniversalTranslatorFactory traductor =
                new DatabaseUniversalTranslatorFactory(ConnectionFactory.TipoBD.SQLSERVER, ConnectionFactory.TipoBD.SQLSERVER);
            Connection conn = ConnectionFactory.getInstancia().getConexion();
            PreparedStatement ps = conn.prepareStatement(lib.SQLQueries.SELECT_PROVEEDOR_ID_BY_NOMBRE)
        ) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            System.err.println("[ProveedorDAO] Error al obtener ID de proveedor por nombre: " + e.getMessage());
        }
        return null;
    }

    public String obtenerNombrePorId(int idProveedor) {
        String nombre = "N/A";
        String sql = "SELECT nombre FROM Proveedores WHERE id = ?";
        try (
            Connection conn = ConnectionFactory.getInstancia().getConexion();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, idProveedor);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    nombre = rs.getString("nombre");
                }
            }
        } catch (Exception e) {
            System.err.println("[ProveedorDAO] Error al obtener nombre de proveedor: " + e.getMessage());
        }
        return nombre;
    }
}
