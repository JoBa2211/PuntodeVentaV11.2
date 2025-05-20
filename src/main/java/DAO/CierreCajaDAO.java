package DAO;

import clases.CierreCaja;
import clases.Ticket;
import clases.DetalleProductoTicket;
import lib.ConnectionFactory;
import lib.DatabaseUniversalTranslatorFactory;
import lib.SQLQueries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CierreCajaDAO {

    public boolean insertar(CierreCaja cierreCaja) {
        try (
            DatabaseUniversalTranslatorFactory traductor =
                new DatabaseUniversalTranslatorFactory(ConnectionFactory.TipoBD.SQLSERVER, ConnectionFactory.TipoBD.SQLSERVER);
            Connection conn = ConnectionFactory.getInstancia().getConexion();
            PreparedStatement ps = conn.prepareStatement(SQLQueries.INSERT_CIERRE_CAJA, PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setInt(1, cierreCaja.getNumeroDeTickets());
            ps.setDouble(2, cierreCaja.getSubtotal());
            ps.setDouble(3, cierreCaja.getTotal());
            ps.setDouble(4, cierreCaja.getIepsTotal());
            ps.setInt(5, cierreCaja.getIdUsuarios().isEmpty() ? 0 : cierreCaja.getIdUsuarios().get(0));

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                System.err.println("[CierreCajaDAO] No se pudo insertar el cierre de caja.");
                return false;
            }

            int idCierreCaja = -1;
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    idCierreCaja = generatedKeys.getInt(1);
                }
            }

            // Insertar relación de tickets con cierre de caja y detalles de productos
            if (idCierreCaja != -1 && cierreCaja.getListaDeTickets() != null) {
                for (Ticket ticket : cierreCaja.getListaDeTickets()) {
                    try (PreparedStatement psDetalle = conn.prepareStatement(SQLQueries.INSERT_TICKET_CIERRE_CAJA)) {
                        psDetalle.setInt(1, ticket.getId());
                        psDetalle.setInt(2, idCierreCaja);
                        psDetalle.executeUpdate();
                    }
                    if (ticket.getDetalles() != null) {
                        for (DetalleProductoTicket detalle : ticket.getDetalles()) {
                            try (PreparedStatement psProd = conn.prepareStatement(SQLQueries.INSERT_TICKET_DETALLE)) {
                                psProd.setInt(1, ticket.getId());
                                psProd.setInt(2, detalle.getIdProducto());
                                psProd.setDouble(3, detalle.getCantidad());
                                psProd.executeUpdate();
                            }
                        }
                    }
                }
            }

            return true;
        } catch (SQLException e) {
            System.err.println("[CierreCajaDAO] Error al insertar cierre de caja: " + e.getMessage());
            return false;
        }
    }

    public List<CierreCaja> obtenerTodos() {
        List<CierreCaja> cierres = new ArrayList<>();
        try (
            DatabaseUniversalTranslatorFactory traductor =
                new DatabaseUniversalTranslatorFactory(ConnectionFactory.TipoBD.SQLSERVER, ConnectionFactory.TipoBD.SQLSERVER);
            Connection conn = ConnectionFactory.getInstancia().getConexion();
            PreparedStatement ps = conn.prepareStatement(SQLQueries.SELECT_ALL_CIERRES_CAJA);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                int id = rs.getInt("id");
                int numeroDeTickets = rs.getInt("numeroDeTickets");
                double subtotal = rs.getDouble("subtotal");
                double total = rs.getDouble("total");
                double iepsTotal = rs.getDouble("iepsTotal");
                int idUsuario = rs.getInt("idUsuario");

                // Obtener lista de tickets asociados a este cierre de caja usando solo SQLQueries
                List<Ticket> listaDeTickets = new ArrayList<>();
                try (PreparedStatement psTickets = conn.prepareStatement(SQLQueries.SELECT_TICKETS_BY_CIERRE_CAJA)) {
                    psTickets.setInt(1, id);
                    try (ResultSet rsTickets = psTickets.executeQuery()) {
                        while (rsTickets.next()) {
                            int idTicket = rsTickets.getInt("IdTicket");
                            // Crea el ticket con los campos obligatorios, los demás pueden ser null o 0
                            Ticket ticket = new Ticket(
                                idTicket,
                                null, // fechaDeCreacion
                                0,    // idUsuario
                                null, // numeroDeTicket
                                new ArrayList<>(), // detalles
                                0, 0, 0, 0 // subtotal, total, ivaTotal, iepsTotal
                            );
                            // Obtener detalles del ticket usando solo SQLQueries
                            List<DetalleProductoTicket> detalles = new ArrayList<>();
                            try (PreparedStatement psDetalles = conn.prepareStatement(SQLQueries.SELECT_TICKET_DETALLES_BY_TICKET_ID)) {
                                psDetalles.setInt(1, idTicket);
                                try (ResultSet rsDetalles = psDetalles.executeQuery()) {
                                    while (rsDetalles.next()) {
                                        int idProducto = rsDetalles.getInt("idProducto");
                                        double cantidad = rsDetalles.getDouble("cantidad");
                                        detalles.add(new DetalleProductoTicket(idProducto, cantidad));
                                    }
                                }
                            }
                            ticket.setDetalles(detalles);

                            listaDeTickets.add(ticket);
                        }
                    }
                }

                List<Integer> idUsuarios = new ArrayList<>();
                idUsuarios.add(idUsuario);

                CierreCaja cierre = new CierreCaja(id, idUsuarios, numeroDeTickets, subtotal, total, iepsTotal, listaDeTickets);
                cierres.add(cierre);
            }
        } catch (SQLException e) {
            System.err.println("[CierreCajaDAO] Error al obtener cierres de caja: " + e.getMessage());
        }
        return cierres;
    }
}
