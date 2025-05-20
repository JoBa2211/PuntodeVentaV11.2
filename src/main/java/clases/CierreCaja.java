package Clases;

import java.util.List;

public class  CierreCaja {
    private int id;
    private List<Integer> idUsuarios;
    private int numeroDeTickets;
    private double subtotal;
    private double total;
    private double iepsTotal;
    private List<Ticket> listaDeTickets;

    // Constructor
    public CierreCaja(int id, List<Integer> idUsuarios, int numeroDeTickets, double subtotal, double total, 
                      double iepsTotal, List<Ticket> listaDeTickets) {
        this.id = id;
        this.idUsuarios = idUsuarios;
        this.numeroDeTickets = numeroDeTickets;
        this.subtotal = subtotal;
        this.total = total;
        this.iepsTotal = iepsTotal;
        this.listaDeTickets = listaDeTickets;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Integer> getIdUsuarios() {
        return idUsuarios;
    }

    public void setIdUsuarios(List<Integer> idUsuarios) {
        this.idUsuarios = idUsuarios;
    }

    public int getNumeroDeTickets() {
        return numeroDeTickets;
    }

    public void setNumeroDeTickets(int numeroDeTickets) {
        this.numeroDeTickets = numeroDeTickets;
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

    public double getIepsTotal() {
        return iepsTotal;
    }

    public void setIepsTotal(double iepsTotal) {
        this.iepsTotal = iepsTotal;
    }

    public List<Ticket> getListaDeTickets() {
        return listaDeTickets;
    }

    public void setListaDeTickets(List<Ticket> listaDeTickets) {
        this.listaDeTickets = listaDeTickets;
    }
}
