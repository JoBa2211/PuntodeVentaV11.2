package DAO;

import clases.DetalleProductoTicket;
import lib.ConnectionFactory;
import lib.DatabaseUniversalTranslatorFactory;
import lib.SQLQueries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TicketDetalleDAO {

    public List<DetalleProductoTicket> obtenerDetallesPorTicketId(int ticketId) {
        List<DetalleProductoTicket> detalles = new ArrayList<>();
        try (
            DatabaseUniversalTranslatorFactory traductor =
                new DatabaseUniversalTranslatorFactory(ConnectionFactory.TipoBD.SQLSERVER, ConnectionFactory.TipoBD.SQLSERVER);
            Connection conn = ConnectionFactory.getInstancia().getConexion();
            PreparedStatement ps = conn.prepareStatement(SQLQueries.SELECT_TICKET_DETALLES_COMPLETO_BY_TICKET_ID)
        ) {
            ps.setInt(1, ticketId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int idProducto = rs.getInt("idProducto");
                    double cantidad = rs.getDouble("cantidad");
                    double precio = rs.getDouble("precio");
                    String nombre = rs.getString("nombre");
                    double iva = rs.getDouble("iva");
                    double ieps = rs.getDouble("ieps");
                    DetalleProductoTicket detalle = new DetalleProductoTicket(idProducto, cantidad, precio, nombre, iva, ieps);
                    detalles.add(detalle);
                }
            }
        } catch (Exception e) {
            System.err.println("[TicketDetalleDAO] Error al obtener detalles del ticket: " + e.getMessage());
        }
        return detalles;
    }
}
