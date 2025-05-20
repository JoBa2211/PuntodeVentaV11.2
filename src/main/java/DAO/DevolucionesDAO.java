package DAO;

import clases.Devoluciones;
import lib.ConnectionFactory;
import lib.DatabaseUniversalTranslatorFactory;
import lib.SQLQueries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DevolucionesDAO {

    public boolean insertar(Devoluciones devolucion) {
        try (
            DatabaseUniversalTranslatorFactory traductor =
                new DatabaseUniversalTranslatorFactory(ConnectionFactory.TipoBD.SQLSERVER, ConnectionFactory.TipoBD.SQLSERVER);
            Connection conn = ConnectionFactory.getInstancia().getConexion();
            PreparedStatement ps = conn.prepareStatement(SQLQueries.INSERT_DEVOLUCION)
        ) {
            ps.setString(1, devolucion.getNombre());
            ps.setDouble(2, devolucion.getCantidad());
            ps.setDouble(3, devolucion.getPrecio());
            ps.setTimestamp(4, devolucion.getFecha());
            ps.setInt(5, devolucion.getIdUsuario());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[DevolucionesDAO] Error al insertar devolución: " + e.getMessage());
            return false;
        }
    }

    public List<Devoluciones> obtenerTodas() {
        List<Devoluciones> lista = new ArrayList<>();
        try (
            DatabaseUniversalTranslatorFactory traductor =
                new DatabaseUniversalTranslatorFactory(ConnectionFactory.TipoBD.SQLSERVER, ConnectionFactory.TipoBD.SQLSERVER);
            Connection conn = ConnectionFactory.getInstancia().getConexion();
            PreparedStatement ps = conn.prepareStatement(SQLQueries.SELECT_ALL_DEVOLUCIONES);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                Devoluciones d = new Devoluciones(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getDouble("cantidad"),
                    rs.getDouble("precio"),
                    rs.getTimestamp("fecha"),
                    rs.getInt("id_usuario")
                );
                lista.add(d);
            }
        } catch (SQLException e) {
            System.err.println("[DevolucionesDAO] Error al obtener devoluciones: " + e.getMessage());
        }
        return lista;
    }
}
