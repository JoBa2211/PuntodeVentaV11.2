package DAO;

import clases.Refill;
import lib.ConnectionFactory;
import lib.DatabaseUniversalTranslatorFactory;
import lib.SQLQueries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RefillDAO {

    public boolean insertar(Refill refill) {
        try (
            DatabaseUniversalTranslatorFactory traductor =
                new DatabaseUniversalTranslatorFactory(ConnectionFactory.TipoBD.SQLSERVER, ConnectionFactory.TipoBD.SQLSERVER);
            Connection conn = ConnectionFactory.getInstancia().getConexion();
            PreparedStatement ps = conn.prepareStatement(SQLQueries.INSERT_REABASTECIMIENTO)
        ) {
            ps.setInt(1, refill.getIdProducto());
            ps.setInt(2, refill.getCantidad());
            ps.setInt(3, refill.getCantidadRefill());
            ps.setInt(4, refill.getCantidadNueva());
            ps.setDate(5, new java.sql.Date(System.currentTimeMillis())); // Puedes ajustar la fecha si tienes el campo en el modelo
            ps.setInt(6, refill.getIdUsuario());
            ps.setString(7, refill.getNotas());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[RefillDAO] Error al insertar refill: " + e.getMessage());
            return false;
        }
    }

    public List<Refill> obtenerTodos() {
        List<Refill> lista = new ArrayList<>();
        try (
            DatabaseUniversalTranslatorFactory traductor =
                new DatabaseUniversalTranslatorFactory(ConnectionFactory.TipoBD.SQLSERVER, ConnectionFactory.TipoBD.SQLSERVER);
            Connection conn = ConnectionFactory.getInstancia().getConexion();
            PreparedStatement ps = conn.prepareStatement(SQLQueries.SELECT_ALL_REABASTECIMIENTOS);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                Refill refill = new Refill(
                    rs.getInt("id"),
                    rs.getInt("idProducto"),
                    rs.getInt("cantidad"),
                    rs.getInt("cantidadRefill"),
                    rs.getInt("cantidadNueva"),
                    rs.getString("notas"),
                    rs.getInt("idUsuario"),
                    rs.getDate("fecha") // Nuevo campo
                );
                lista.add(refill);
            }
        } catch (SQLException e) {
            System.err.println("[RefillDAO] Error al obtener refills: " + e.getMessage());
        }
        return lista;
    }

    public boolean eliminar(int id) {
        try (
            DatabaseUniversalTranslatorFactory traductor =
                new DatabaseUniversalTranslatorFactory(ConnectionFactory.TipoBD.SQLSERVER, ConnectionFactory.TipoBD.SQLSERVER);
            Connection conn = ConnectionFactory.getInstancia().getConexion();
            PreparedStatement ps = conn.prepareStatement(SQLQueries.DELETE_REABASTECIMIENTO)
        ) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[RefillDAO] Error al eliminar refill: " + e.getMessage());
            return false;
        }
    }
}
