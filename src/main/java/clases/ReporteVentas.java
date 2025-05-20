package Clases;

import java.util.Date;
import java.util.List;

public class ReporteVentas {
    private int id;
    private Date fechaDeCreacion;
    private int cantidadProductosVendidos;
    private int cantidadTicketsCreados;
    private double total;
    private double subtotal;
    private double ivaTotal;
    private double iepsTotal;
    private TipoReporte tipoReporte;
    private List<Ticket> tickets;
    
    // Enum para el tipo de reporte
    public enum TipoReporte {
        DIARIO,
        SEMANAL,
        MENSUAL
    }
    
    // Constructor
    public ReporteVentas(int id, Date fechaDeCreacion, int cantidadProductosVendidos, 
                         int cantidadTicketsCreados, double total, double subtotal, 
                         double ivaTotal, double iepsTotal, TipoReporte tipoReporte,
                         List<Ticket> tickets) {
        this.id = id;
        this.fechaDeCreacion = fechaDeCreacion;
        this.cantidadProductosVendidos = cantidadProductosVendidos;
        this.cantidadTicketsCreados = cantidadTicketsCreados;
        this.total = total;
        this.subtotal = subtotal;
        this.ivaTotal = ivaTotal;
        this.iepsTotal = iepsTotal;
        this.tipoReporte = tipoReporte;
        this.tickets = tickets;
    }
    
    // Método para generar el reporte según el tipo
    public String generarReporte() {
        switch (tipoReporte) {
            case DIARIO:
                return "Reporte Diario generado para la fecha: " + fechaDeCreacion;
            case SEMANAL:
                return "Reporte Semanal generado para la semana de: " + fechaDeCreacion;
            case MENSUAL:
                return "Reporte Mensual generado para el mes de: " + fechaDeCreacion;
            default:
                return "Tipo de reporte no válido";
        }
    }
    
    // Getters y Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public Date getFechaDeCreacion() {
        return fechaDeCreacion;
    }
    
    public void setFechaDeCreacion(Date fechaDeCreacion) {
        this.fechaDeCreacion = fechaDeCreacion;
    }
    
    public int getCantidadProductosVendidos() {
        return cantidadProductosVendidos;
    }
    
    public void setCantidadProductosVendidos(int cantidadProductosVendidos) {
        this.cantidadProductosVendidos = cantidadProductosVendidos;
    }
    
    public int getCantidadTicketsCreados() {
        return cantidadTicketsCreados;
    }
    
    public void setCantidadTicketsCreados(int cantidadTicketsCreados) {
        this.cantidadTicketsCreados = cantidadTicketsCreados;
    }
    
    public double getTotal() {
        return total;
    }
    
    public void setTotal(double total) {
        this.total = total;
    }
    
    public double getSubtotal() {
        return subtotal;
    }
    
    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
    
    public double getIvaTotal() {
        return ivaTotal;
    }
    
    public void setIvaTotal(double ivaTotal) {
        this.ivaTotal = ivaTotal;
    }
    
    public double getIepsTotal() {
        return iepsTotal;
    }
    
    public void setIepsTotal(double iepsTotal) {
        this.iepsTotal = iepsTotal;
    }
    
    public TipoReporte getTipoReporte() {
        return tipoReporte;
    }
    
    public void setTipoReporte(TipoReporte tipoReporte) {
        this.tipoReporte = tipoReporte;
    }
    
    public List<Ticket> getTickets() {
        return tickets;
    }
    
    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }
}
