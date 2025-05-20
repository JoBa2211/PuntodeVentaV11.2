package DAO;

import clases.ReporteVentas;
import clases.DetallesTicketReporte;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.List;

import lib.ConnectionFactory;
import lib.SQLQueries;

public class ReporteVentasDAO {

    public boolean insertar(ReporteVentas reporte) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet generatedKeys = null;
        try {
            conn = ConnectionFactory.getInstancia().getConexion();

            // --- NUEVO: Construcción avanzada de tipoReporteStr ---
            String tipoReporteStr;
            String nombreProducto = null;
            // Busca si todos los detalles son del mismo producto (por producto)
            if (reporte.getDetallesTickets() != null && !reporte.getDetallesTickets().isEmpty()) {
                // Si el reporte es por producto, puedes guardar el nombre aquí
                // Suponiendo que guardas el nombre del producto en el campo numeroTicket del primer detalle
                nombreProducto = reporte.getDetallesTickets().get(0).getNumeroTicket();
            }

            if (reporte.getTipoReporte() != null) {
                tipoReporteStr = "";
                if (nombreProducto != null && !nombreProducto.isEmpty() && !"Todos los productos".equals(nombreProducto)) {
                    tipoReporteStr += "Por Producto " + nombreProducto + " - ";
                }
                tipoReporteStr += reporte.getTipoReporte().name();
            } else {
                // Buscar fechas mínimas y máximas de los detalles
                String intervalo = "PERSONALIZADO";
                List<DetallesTicketReporte> detalles = reporte.getDetallesTickets();
                if (detalles != null && !detalles.isEmpty()) {
                    java.util.Date min = detalles.stream().map(DetallesTicketReporte::getFechaCreacion).min(java.util.Date::compareTo).orElse(null);
                    java.util.Date max = detalles.stream().map(DetallesTicketReporte::getFechaCreacion).max(java.util.Date::compareTo).orElse(null);
                    if (min != null && max != null) {
                        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
                        intervalo = "Intervalo de fecha: " + sdf.format(min) + " a " + sdf.format(max);
                    }
                }
                tipoReporteStr = "";
                if (nombreProducto != null && !nombreProducto.isEmpty() && !"Todos los productos".equals(nombreProducto)) {
                    tipoReporteStr += "Por Producto " + nombreProducto + " - ";
                }
                tipoReporteStr += intervalo;
            }
            // ------------------------------------------------------

            ps = conn.prepareStatement(SQLQueries.INSERT_REPORTE_VENTAS, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setDate(1, new Date(reporte.getFechaDeCreacion().getTime()));
            ps.setInt(2, reporte.getCantidadProductosVendidos());
            ps.setInt(3, reporte.getCantidadTicketsCreados());
            ps.setDouble(4, reporte.getTotal());
            ps.setDouble(5, reporte.getSubtotal());
            ps.setDouble(6, reporte.getIvaTotal());
            ps.setDouble(7, reporte.getIepsTotal());
            ps.setString(8, tipoReporteStr); // Cambiado aquí
            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                System.err.println("[ReporteVentasDAO] No se pudo insertar el reporte de ventas.");
                return false;
            }

            // Obtener el ID generado para el reporte
            int idReporte = -1;
            generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                idReporte = generatedKeys.getInt(1);
            }

            // Insertar los detalles de ticket asociados
            if (idReporte != -1 && reporte.getDetallesTickets() != null && !reporte.getDetallesTickets().isEmpty()) {
                // Actualiza el idReporte en cada detalle antes de insertar
                for (DetallesTicketReporte d : reporte.getDetallesTickets()) {
                    d.setIdReporte(idReporte);
                }
                insertarDetallesTicketReporte(conn, idReporte, reporte.getDetallesTickets());
            }

            return true;
        } catch (SQLException e) {
            System.err.println("[ReporteVentasDAO] Error al insertar reporte de ventas: " + e.getMessage());
            return false;
        } finally {
            try { if (generatedKeys != null) generatedKeys.close(); } catch (Exception ignored) {}
            try { if (ps != null) ps.close(); } catch (Exception ignored) {}
            try { if (conn != null) conn.close(); } catch (Exception ignored) {}
        }
    }

    private void insertarDetallesTicketReporte(Connection conn, int idReporte, List<DetallesTicketReporte> detalles) throws SQLException {
        // Usa la constante de SQLQueries para la inserción
        String sql = lib.SQLQueries.INSERT_DETALLE_TICKET_REPORTE;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (DetallesTicketReporte d : detalles) {
                ps.setInt(1, idReporte);
                ps.setInt(2, d.getIdTicket());
                ps.setDouble(3, d.getTotal());
                ps.setTimestamp(4, new java.sql.Timestamp(d.getFechaCreacion().getTime()));
                ps.setString(5, d.getNumeroTicket());
                ps.setDouble(6, d.getIva());
                ps.setDouble(7, d.getIeps());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }
}
