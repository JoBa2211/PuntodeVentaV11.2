package Clases;

import java.util.Date;
import java.util.List;

public class Ticket {
    private int id;
    private Date fechaDeCreacion;
    private int idUsuario; // Quien lo creó
    private String numeroDeTicket;
    private List<Producto> productos;
    private double subtotal;
    private double total;
    private double ivaTotal;
    private double iepsTotal;

    // Constructor
    public Ticket(int id, Date fechaDeCreacion, int idUsuario, String numeroDeTicket, List<Producto> productos,
                 double subtotal, double total, double ivaTotal, double iepsTotal) {
        this.id = id;
        this.fechaDeCreacion = fechaDeCreacion;
        this.idUsuario = idUsuario;
        this.numeroDeTicket = numeroDeTicket;
        this.productos = productos;
        this.subtotal = subtotal;
        this.total = total;
        this.ivaTotal = ivaTotal;
        this.iepsTotal = iepsTotal;
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

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNumeroDeTicket() {
        return numeroDeTicket;
    }

    public void setNumeroDeTicket(String numeroDeTicket) {
        this.numeroDeTicket = numeroDeTicket;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TICKET DE VENTA\n");
        sb.append("No. Ticket: ").append(numeroDeTicket)
          .append("    Fecha: ").append(fechaDeCreacion).append("\n");
        sb.append("Usuario ID: ").append(idUsuario).append("\n");
        sb.append("----------------------------------------------------------------------\n");
        sb.append("ID   Código          Nombre              Precio  Cant.   IVA   IEPS\n");
        sb.append("----------------------------------------------------------------------\n");
        if (productos != null) {
            for (Producto p : productos) {
                sb.append(
                    p.getId()).append("   ")
                    .append(p.getCodigo()).append("   ")
                    .append(ajustar(p.getNombre(), 15)).append("   ")
                    .append(String.format("%.2f", p.getPrecioVenta())).append("   ")
                    .append(String.format("%.2f", p.getExistencias())).append("   ")
                    .append(String.format("%.2f", p.getIva())).append("   ")
                    .append(String.format("%.2f", p.getIeps())).append("\n");
            }
        }
        sb.append("----------------------------------------------------------------------\n");
        sb.append("Subtotal:   $").append(String.format("%.2f", subtotal)).append("\n");
        sb.append("IVA:        $").append(String.format("%.2f", ivaTotal)).append("\n");
        sb.append("IEPS:       $").append(String.format("%.2f", iepsTotal)).append("\n");
        sb.append("TOTAL:      $").append(String.format("%.2f", total)).append("\n");
        sb.append("=======================================================================\n");
        return sb.toString();
    }

    // Método auxiliar para ajustar el nombre a un ancho fijo (rellena o recorta)
    private String ajustar(String texto, int ancho) {
        if (texto.length() > ancho) return texto.substring(0, ancho);
        StringBuilder sb = new StringBuilder(texto);
        while (sb.length() < ancho) sb.append(" ");
        return sb.toString();
    }
}
