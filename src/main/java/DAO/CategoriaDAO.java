package DAO;

import clases.Categoria;
import lib.ConnectionFactory;
import lib.DatabaseUniversalTranslatorFactory;
import lib.SQLQueries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    public List<Categoria> obtenerTodas() {
        List<Categoria> categorias = new ArrayList<>();
        try (
            DatabaseUniversalTranslatorFactory traductor =
                new DatabaseUniversalTranslatorFactory(ConnectionFactory.TipoBD.SQLSERVER, ConnectionFactory.TipoBD.SQLSERVER);
            Connection conn = ConnectionFactory.getInstancia().getConexion();
            PreparedStatement ps = conn.prepareStatement(SQLQueries.SELECT_ALL_CATEGORIAS);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String notas = rs.getString("notas");
                categorias.add(new Categoria(id, nombre, notas));
            }
        } catch (SQLException e) {
            System.err.println("[CategoriaDAO] Error al obtener categorías: " + e.getMessage());
        }
        return categorias;
    }

    public boolean insertar(Categoria categoria) {
        try (
            DatabaseUniversalTranslatorFactory traductor =
                new DatabaseUniversalTranslatorFactory(ConnectionFactory.TipoBD.SQLSERVER, ConnectionFactory.TipoBD.SQLSERVER);
            Connection conn = ConnectionFactory.getInstancia().getConexion();
            PreparedStatement ps = conn.prepareStatement(SQLQueries.INSERT_CATEGORIA)
        ) {
            ps.setString(1, categoria.getNombre());
            ps.setString(2, categoria.getNotas());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[CategoriaDAO] Error al insertar categoría: " + e.getMessage());
            return false;
        }
    }

    public Integer obtenerIdPorNombre(String nombre) {
        try (
            DatabaseUniversalTranslatorFactory traductor =
                new DatabaseUniversalTranslatorFactory(ConnectionFactory.TipoBD.SQLSERVER, ConnectionFactory.TipoBD.SQLSERVER);
            Connection conn = ConnectionFactory.getInstancia().getConexion();
            PreparedStatement ps = conn.prepareStatement(SQLQueries.SELECT_CATEGORIA_ID_BY_NOMBRE)
        ) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            System.err.println("[CategoriaDAO] Error al obtener ID de categoría por nombre: " + e.getMessage());
        }
        return null;
    }

    public String obtenerNombrePorId(int id) {
        try (
            DatabaseUniversalTranslatorFactory traductor =
                new DatabaseUniversalTranslatorFactory(ConnectionFactory.TipoBD.SQLSERVER, ConnectionFactory.TipoBD.SQLSERVER);
            Connection conn = ConnectionFactory.getInstancia().getConexion();
            PreparedStatement ps = conn.prepareStatement(lib.SQLQueries.SELECT_CATEGORIA_BY_ID)
        ) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("nombre");
                }
            }
        } catch (SQLException e) {
            System.err.println("[CategoriaDAO] Error al obtener nombre de categoría por id: " + e.getMessage());
        }
        return null;
    }

    public Categoria obtenerPorId(int id) {
        try (
            DatabaseUniversalTranslatorFactory traductor =
                new DatabaseUniversalTranslatorFactory(ConnectionFactory.TipoBD.SQLSERVER, ConnectionFactory.TipoBD.SQLSERVER);
            Connection conn = ConnectionFactory.getInstancia().getConexion();
            PreparedStatement ps = conn.prepareStatement(lib.SQLQueries.SELECT_CATEGORIA_BY_ID)
        ) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Ajusta si tu consulta trae más columnas, aquí solo nombre
                    String nombre = rs.getString("nombre");
                    // Si tienes notas, puedes obtenerlas así:
                    String notas = null;
                    try { notas = rs.getString("notas"); } catch (Exception ignored) {}
                    return new Categoria(id, nombre, notas);
                }
            }
        } catch (SQLException e) {
            System.err.println("[CategoriaDAO] Error al obtener categoría por id: " + e.getMessage());
        }
        return null;
    }

    public boolean actualizar(Categoria categoria) {
        try (
            DatabaseUniversalTranslatorFactory traductor =
                new DatabaseUniversalTranslatorFactory(ConnectionFactory.TipoBD.SQLSERVER, ConnectionFactory.TipoBD.SQLSERVER);
            Connection conn = ConnectionFactory.getInstancia().getConexion();
            PreparedStatement ps = conn.prepareStatement(lib.SQLQueries.UPDATE_CATEGORIA)
        ) {
            ps.setString(1, categoria.getNombre());
            ps.setString(2, categoria.getNotas());
            ps.setInt(3, categoria.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[CategoriaDAO] Error al actualizar categoría: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idCategoria) {
        try (
            DatabaseUniversalTranslatorFactory traductor =
                new DatabaseUniversalTranslatorFactory(ConnectionFactory.TipoBD.SQLSERVER, ConnectionFactory.TipoBD.SQLSERVER);
            Connection conn = ConnectionFactory.getInstancia().getConexion();
            PreparedStatement ps = conn.prepareStatement(lib.SQLQueries.DELETE_CATEGORIA)
        ) {
            ps.setInt(1, idCategoria);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[CategoriaDAO] Error al eliminar categoría: " + e.getMessage());
            return false;
        }
    }
}
