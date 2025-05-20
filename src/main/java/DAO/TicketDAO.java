package DAO;

import clases.DetalleProductoTicket;
import clases.Ticket;
import lib.ConnectionFactory;
import lib.DatabaseUniversalTranslatorFactory;
import lib.SQLQueries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class TicketDAO {

    public boolean insertar(Ticket ticket) {
        try (
                DatabaseUniversalTranslatorFactory traductor =
                        new DatabaseUniversalTranslatorFactory(ConnectionFactory.TipoBD.SQLSERVER, ConnectionFactory.TipoBD.SQLSERVER);
                Connection conn = ConnectionFactory.getInstancia().getConexion();
                PreparedStatement ps = conn.prepareStatement(SQLQueries.INSERT_TICKET, PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            // Cambia Date por Timestamp para conservar la hora
            ps.setTimestamp(1, new Timestamp(ticket.getFechaDeCreacion().getTime()));
            ps.setInt(2, ticket.getIdUsuario());
            ps.setString(3, ticket.getNumeroDeTicket());
            ps.setDouble(4, ticket.getSubtotal());
            ps.setDouble(5, ticket.getTotal());
            ps.setDouble(6, ticket.getIvaTotal());
            ps.setDouble(7, ticket.getIepsTotal());
            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                System.err.println("[TicketDAO] No se pudo insertar el ticket.");
                return false;
            }

            // Obtener el ID generado para el ticket
            int ticketId = -1;
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    ticketId = generatedKeys.getInt(1);
                }
            }

            // Insertar detalles del ticket
            if (ticket.getDetalles() != null && ticketId != -1) {
                for (DetalleProductoTicket detalle : ticket.getDetalles()) {
                    try (PreparedStatement psDetalle = conn.prepareStatement(
                            lib.SQLQueries.INSERT_TICKET_DETALLE_COMPLETO)) {
                        psDetalle.setInt(1, ticketId);
                        psDetalle.setInt(2, detalle.getIdProducto());
                        psDetalle.setString(3, detalle.getCodigo());
                        psDetalle.setDouble(4, detalle.getCantidad());
                        psDetalle.setDouble(5, detalle.getPrecio());
                        // Si tu tabla TicketDetalles tiene columna 'nombre', 'codigo', 'iva', 'ieps', agrégalos aquí:
                        // psDetalle.setString(6, detalle.getNombre());
                        // psDetalle.setDouble(7, detalle.getIva());
                        // psDetalle.setDouble(8, detalle.getIeps());
                        psDetalle.executeUpdate();
                    }
                }
            }

            return true;
        } catch (SQLException e) {
            System.err.println("[TicketDAO] Error al insertar ticket: " + e.getMessage());
            return false;
        }
    }

    // Ahora este método es público y reutilizable
    public List<DetalleProductoTicket> obtenerDetallesPorTicketId(int ticketId) {
        List<DetalleProductoTicket> detalles = new ArrayList<>();
        try (
                DatabaseUniversalTranslatorFactory traductor =
                        new DatabaseUniversalTranslatorFactory(ConnectionFactory.TipoBD.SQLSERVER, ConnectionFactory.TipoBD.SQLSERVER);
                Connection conn = ConnectionFactory.getInstancia().getConexion();
                PreparedStatement ps = conn.prepareStatement(lib.SQLQueries.SELECT_TICKET_DETALLES_COMPLETO_BY_TICKET_ID)
        ) {
            ps.setInt(1, ticketId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int idProducto = rs.getInt("idProducto");
                    String codigo = "";
                    try {
                        codigo = rs.getString("codigo");
                    } catch (Exception ignored) {
                    }
                    double cantidad = rs.getDouble("cantidad");
                    double precio = rs.getDouble("precio");
                    String nombre = rs.getString("nombre");
                    double iva = rs.getDouble("iva");
                    double ieps = rs.getDouble("ieps");
                    DetalleProductoTicket detalle = new DetalleProductoTicket(idProducto, codigo, cantidad, precio, nombre, iva, ieps);
                    detalles.add(detalle);
                }
            }
        } catch (Exception e) {
            System.err.println("[TicketDAO] Error al obtener detalles del ticket: " + e.getMessage());
        }
        return detalles;
    }

    public List<Ticket> obtenerTodos() {
        List<Ticket> tickets = new ArrayList<>();
        try (
                DatabaseUniversalTranslatorFactory traductor =
                        new DatabaseUniversalTranslatorFactory(ConnectionFactory.TipoBD.SQLSERVER, ConnectionFactory.TipoBD.SQLSERVER);
                Connection conn = ConnectionFactory.getInstancia().getConexion();
                PreparedStatement ps = conn.prepareStatement(SQLQueries.SELECT_ALL_TICKETS);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                int ticketId = rs.getInt("ticketId");
                Timestamp fecha = rs.getTimestamp("fechaDeCreacion");
                int idUsuario = rs.getInt("idUsuario");
                String numeroDeTicket = rs.getString("numeroDeTicket");
                double subtotal = rs.getDouble("subtotal");
                double total = rs.getDouble("total");
                double ivaTotal = rs.getDouble("ivaTotal");
                double iepsTotal = rs.getDouble("iepsTotal");
                // No cargar detalles aquí
                Ticket ticket = new Ticket(ticketId, fecha, idUsuario, numeroDeTicket, null, subtotal, total, ivaTotal, iepsTotal);
                tickets.add(ticket);
            }
        } catch (SQLException e) {
            System.err.println("[TicketDAO] Error al obtener tickets: " + e.getMessage());
        }
        return tickets;
    }

    public List<Ticket> obtenerPorDiaActual() {
        List<Ticket> tickets = new ArrayList<>();
        try (
                DatabaseUniversalTranslatorFactory traductor =
                        new DatabaseUniversalTranslatorFactory(ConnectionFactory.TipoBD.SQLSERVER, ConnectionFactory.TipoBD.SQLSERVER);
                Connection conn = ConnectionFactory.getInstancia().getConexion();
                PreparedStatement ps = conn.prepareStatement(SQLQueries.SELECT_TICKETS_HOY);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                int ticketId = rs.getInt("ticketId");
                Timestamp fecha = rs.getTimestamp("fechaDeCreacion");
                int idUsuario = rs.getInt("idUsuario");
                String numeroDeTicket = rs.getString("numeroDeTicket");
                double subtotal = rs.getDouble("subtotal");
                double total = rs.getDouble("total");
                double ivaTotal = rs.getDouble("ivaTotal");
                double iepsTotal = rs.getDouble("iepsTotal");
                // No cargar detalles aquí
                Ticket ticket = new Ticket(ticketId, fecha, idUsuario, numeroDeTicket, null, subtotal, total, ivaTotal, iepsTotal);
                tickets.add(ticket);
            }
        } catch (SQLException e) {
            System.err.println("[TicketDAO] Error al obtener tickets de hoy: " + e.getMessage());
        }
        return tickets;
    }

    public List<Ticket> obtenerPorSemanaActual() {
        List<Ticket> tickets = new ArrayList<>();
        try (
                DatabaseUniversalTranslatorFactory traductor =
                        new DatabaseUniversalTranslatorFactory(ConnectionFactory.TipoBD.SQLSERVER, ConnectionFactory.TipoBD.SQLSERVER);
                Connection conn = ConnectionFactory.getInstancia().getConexion();
                PreparedStatement ps = conn.prepareStatement(SQLQueries.SELECT_TICKETS_SEMANA);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                int ticketId = rs.getInt("ticketId");
                Timestamp fecha = rs.getTimestamp("fechaDeCreacion");
                int idUsuario = rs.getInt("idUsuario");
                String numeroDeTicket = rs.getString("numeroDeTicket");
                double subtotal = rs.getDouble("subtotal");
                double total = rs.getDouble("total");
                double ivaTotal = rs.getDouble("ivaTotal");
                double iepsTotal = rs.getDouble("iepsTotal");
                // No cargar detalles aquí
                Ticket ticket = new Ticket(ticketId, fecha, idUsuario, numeroDeTicket, null, subtotal, total, ivaTotal, iepsTotal);
                tickets.add(ticket);
            }
        } catch (SQLException e) {
            System.err.println("[TicketDAO] Error al obtener tickets de la semana: " + e.getMessage());
        }
        return tickets;
    }

    public List<Ticket> obtenerPorMesActual() {
        List<Ticket> tickets = new ArrayList<>();
        try (
                DatabaseUniversalTranslatorFactory traductor =
                        new DatabaseUniversalTranslatorFactory(ConnectionFactory.TipoBD.SQLSERVER, ConnectionFactory.TipoBD.SQLSERVER);
                Connection conn = ConnectionFactory.getInstancia().getConexion();
                PreparedStatement ps = conn.prepareStatement(SQLQueries.SELECT_TICKETS_MES);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                int ticketId = rs.getInt("ticketId");
                Timestamp fecha = rs.getTimestamp("fechaDeCreacion");
                int idUsuario = rs.getInt("idUsuario");
                String numeroDeTicket = rs.getString("numeroDeTicket");
                double subtotal = rs.getDouble("subtotal");
                double total = rs.getDouble("total");
                double ivaTotal = rs.getDouble("ivaTotal");
                double iepsTotal = rs.getDouble("iepsTotal");
                // No cargar detalles aquí
                Ticket ticket = new Ticket(ticketId, fecha, idUsuario, numeroDeTicket, null, subtotal, total, ivaTotal, iepsTotal);
                tickets.add(ticket);
            }
        } catch (SQLException e) {
            System.err.println("[TicketDAO] Error al obtener tickets del mes: " + e.getMessage());
        }
        return tickets;
    }

    public List<Ticket> obtenerPorAnioActual() {
        List<Ticket> tickets = new ArrayList<>();
        try (
                DatabaseUniversalTranslatorFactory traductor =
                        new DatabaseUniversalTranslatorFactory(ConnectionFactory.TipoBD.SQLSERVER, ConnectionFactory.TipoBD.SQLSERVER);
                Connection conn = ConnectionFactory.getInstancia().getConexion();
                PreparedStatement ps = conn.prepareStatement(SQLQueries.SELECT_TICKETS_ANIO);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                int ticketId = rs.getInt("ticketId");
                Timestamp fecha = rs.getTimestamp("fechaDeCreacion");
                int idUsuario = rs.getInt("idUsuario");
                String numeroDeTicket = rs.getString("numeroDeTicket");
                double subtotal = rs.getDouble("subtotal");
                double total = rs.getDouble("total");
                double ivaTotal = rs.getDouble("ivaTotal");
                double iepsTotal = rs.getDouble("iepsTotal");
                // No cargar detalles aquí
                Ticket ticket = new Ticket(ticketId, fecha, idUsuario, numeroDeTicket, null, subtotal, total, ivaTotal, iepsTotal);
                tickets.add(ticket);
            }
        } catch (SQLException e) {
            System.err.println("[TicketDAO] Error al obtener tickets del año: " + e.getMessage());
        }
        return tickets;
    }

    private List<Ticket> obtenerPorFiltroFecha(String filtro) {
        List<Ticket> tickets = new ArrayList<>();
        String query = "";
        switch (filtro) {
            case "DAY":
                query = lib.SQLQueries.SELECT_TICKETS_HOY;
                break;
            case "WEEK":
                query = lib.SQLQueries.SELECT_TICKETS_SEMANA;
                break;
            case "MONTH":
                query = lib.SQLQueries.SELECT_TICKETS_MES;
                break;
            case "YEAR":
                query = lib.SQLQueries.SELECT_TICKETS_ANIO;
                break;
        }
        try (
                DatabaseUniversalTranslatorFactory traductor =
                        new DatabaseUniversalTranslatorFactory(ConnectionFactory.TipoBD.SQLSERVER, ConnectionFactory.TipoBD.SQLSERVER);
                Connection conn = ConnectionFactory.getInstancia().getConexion();
                PreparedStatement ps = conn.prepareStatement(query);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                int ticketId = rs.getInt("ticketId");
                Timestamp fecha = rs.getTimestamp("fechaDeCreacion");
                int idUsuario = rs.getInt("idUsuario");
                String numeroDeTicket = rs.getString("numeroDeTicket");
                double subtotal = rs.getDouble("subtotal");
                double total = rs.getDouble("total");
                double ivaTotal = rs.getDouble("ivaTotal");
                double iepsTotal = rs.getDouble("iepsTotal");
                List<DetalleProductoTicket> detalles = obtenerDetallesPorTicketId(ticketId);
                Ticket ticket = new Ticket(ticketId, fecha, idUsuario, numeroDeTicket, detalles, subtotal, total, ivaTotal, iepsTotal);
                tickets.add(ticket);
            }
        } catch (SQLException e) {
            System.err.println("[TicketDAO] Error al obtener tickets por filtro: " + e.getMessage());
        }
        return tickets;
    }

    public List<Ticket> obtenerPorRango(java.util.Date inicio, java.util.Date fin) {
        List<Ticket> tickets = new ArrayList<>();
        try (
                DatabaseUniversalTranslatorFactory traductor =
                        new DatabaseUniversalTranslatorFactory(ConnectionFactory.TipoBD.SQLSERVER, ConnectionFactory.TipoBD.SQLSERVER);
                Connection conn = ConnectionFactory.getInstancia().getConexion();
                PreparedStatement ps = conn.prepareStatement(lib.SQLQueries.SELECT_TICKETS_RANGO)
        ) {
            ps.setTimestamp(1, new Timestamp(inicio.getTime()));
            ps.setTimestamp(2, new Timestamp(fin.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int ticketId = rs.getInt("ticketId");
                    Timestamp fecha = rs.getTimestamp("fechaDeCreacion");
                    int idUsuario = rs.getInt("idUsuario");
                    String numeroDeTicket = rs.getString("numeroDeTicket");
                    double subtotal = rs.getDouble("subtotal");
                    double total = rs.getDouble("total");
                    double ivaTotal = rs.getDouble("ivaTotal");
                    double iepsTotal = rs.getDouble("iepsTotal");
                    Ticket ticket = new Ticket(ticketId, fecha, idUsuario, numeroDeTicket, null, subtotal, total, ivaTotal, iepsTotal);
                    tickets.add(ticket);
                }
            }
        } catch (SQLException e) {
            System.err.println("[TicketDAO] Error al obtener tickets por rango: " + e.getMessage());
        }
        return tickets;
    }

    /**
     * Devuelve la cantidad total de productos vendidos en los tickets dados.
     *
     * @param ticketIds Lista de IDs de tickets a consultar.
     * @return Suma de la cantidad de productos vendidos en esos tickets.
     */
    public int contarProductosVendidosPorTickets(List<Integer> ticketIds) {
        if (ticketIds == null || ticketIds.isEmpty()) return 0;
        StringBuilder placeholders = new StringBuilder();
        for (int i = 0; i < ticketIds.size(); i++) {
            placeholders.append("?");
            if (i < ticketIds.size() - 1) placeholders.append(",");
        }
        String sql = "SELECT SUM(cantidad) AS totalProductos FROM TicketDetalles WHERE idTicket IN (" + placeholders + ")";
        int total = 0;
        try (
                Connection conn = ConnectionFactory.getInstancia().getConexion();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            for (int i = 0; i < ticketIds.size(); i++) {
                ps.setInt(i + 1, ticketIds.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    total = rs.getInt("totalProductos");
                }
            }
        } catch (SQLException e) {
            System.err.println("[TicketDAO] Error al contar productos vendidos: " + e.getMessage());
        }
        return total;
    }

    /**
     * Obtiene los tickets filtrados por tipo de fecha: DIA, SEMANA, MES, ANIO.
     * @param tipoFiltro "DIA", "SEMANA", "MES", "ANIO"
     * @param fecha Fecha base para el filtro (usualmente hoy)
     * @return Lista de tickets filtrados
     */
    public List<Ticket> obtenerPorFiltroFecha(String tipoFiltro, java.util.Date fecha) {
        List<Ticket> tickets = new ArrayList<>();
        String sqlBase = "SELECT ticketId, fechaDeCreacion, idUsuario, numeroDeTicket, subtotal, total, ivaTotal, iepsTotal FROM Tickets WHERE ";
        String where = "";
        java.sql.Date sqlFecha = new java.sql.Date(fecha.getTime());

        switch (tipoFiltro) {
            case "DIA":
                where = "CONVERT(date, fechaDeCreacion) = CONVERT(date, ?)";
                break;
            case "SEMANA":
                where = "DATEPART(week, fechaDeCreacion) = DATEPART(week, ?) AND YEAR(fechaDeCreacion) = YEAR(?)";
                break;
            case "MES":
                where = "MONTH(fechaDeCreacion) = MONTH(?) AND YEAR(fechaDeCreacion) = YEAR(?)";
                break;
            case "ANIO":
                where = "YEAR(fechaDeCreacion) = YEAR(?)";
                break;
            default:
                where = "1=1";
        }

        String sql = sqlBase + where + " ORDER BY fechaDeCreacion DESC";

        try (
            Connection conn = lib.ConnectionFactory.getInstancia().getConexion();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            switch (tipoFiltro) {
                case "DIA":
                case "ANIO":
                    ps.setDate(1, sqlFecha);
                    break;
                case "SEMANA":
                case "MES":
                    ps.setDate(1, sqlFecha);
                    ps.setDate(2, sqlFecha);
                    break;
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int ticketId = rs.getInt("ticketId");
                java.sql.Timestamp fechaCreacion = rs.getTimestamp("fechaDeCreacion");
                int idUsuario = rs.getInt("idUsuario");
                String numeroDeTicket = rs.getString("numeroDeTicket");
                double subtotal = rs.getDouble("subtotal");
                double total = rs.getDouble("total");
                double ivaTotal = rs.getDouble("ivaTotal");
                double iepsTotal = rs.getDouble("iepsTotal");
                Ticket ticket = new Ticket(ticketId, fechaCreacion, idUsuario, numeroDeTicket, null, subtotal, total, ivaTotal, iepsTotal);
                tickets.add(ticket);
            }
        } catch (Exception e) {
            System.err.println("[TicketDAO] Error en obtenerPorFiltroFecha: " + e.getMessage());
        }
        return tickets;
    }
}
