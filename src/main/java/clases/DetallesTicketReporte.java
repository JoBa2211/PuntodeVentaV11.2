package clases;

import java.util.Date;

public class DetallesTicketReporte {
    private int idReporte;
    private int idTicket;
    private double total;
    private Date fechaCreacion;
    private String numeroTicket;
    private double iva;
    private double ieps;

    public DetallesTicketReporte(int idReporte, int idTicket, double total, Date fechaCreacion, String numeroTicket, double iva, double ieps) {
        this.idReporte = idReporte;
        this.idTicket = idTicket;
        this.total = total;
        this.fechaCreacion = fechaCreacion;
        this.numeroTicket = numeroTicket;
        this.iva = iva;
        this.ieps = ieps;
    }

    public int getIdReporte() {
        return idReporte;
    }

    public void setIdReporte(int idReporte) {
        this.idReporte = idReporte;
    }

    public int getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(int idTicket) {
        this.idTicket = idTicket;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getNumeroTicket() {
        return numeroTicket;
    }

    public void setNumeroTicket(String numeroTicket) {
        this.numeroTicket = numeroTicket;
    }

    public double getIva() {
        return iva;
    }

    public void setIva(double iva) {
        this.iva = iva;
    }

    public double getIeps() {
        return ieps;
    }

    public void setIeps(double ieps) {
        this.ieps = ieps;
    }

    @Override
    public String toString() {
        return "DetallesTicketReporte{" +
                "idReporte=" + idReporte +
                ", idTicket=" + idTicket +
                ", total=" + total +
                ", fechaCreacion=" + fechaCreacion +
                ", numeroTicket='" + numeroTicket + '\'' +
                ", iva=" + iva +
                ", ieps=" + ieps +
                '}';
    }
}
