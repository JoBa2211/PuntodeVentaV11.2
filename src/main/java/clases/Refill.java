package Clases;

public class Refill {
    private int id;
    private int idProducto;
    private int cantidad;
    private double precio;
    private int cantidadNueva;
    private int cantidadRefill;
    private String notas;
    private int idUsuario;

    // Constructor
    public Refill(int id, int idProducto, int cantidad, double precio, int cantidadNueva, int cantidadRefill, String notas, int idUsuario) {
        this.id = id;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precio = precio;
        this.cantidadNueva = cantidadNueva;
        this.cantidadRefill = cantidadRefill;
        this.notas = notas;
        this.idUsuario = idUsuario;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getCantidadNueva() {
        return cantidadNueva;
    }

    public void setCantidadNueva(int cantidadNueva) {
        this.cantidadNueva = cantidadNueva;
    }

    public int getCantidadRefill() {
        return cantidadRefill;
    }

    public void setCantidadRefill(int cantidadRefill) {
        this.cantidadRefill = cantidadRefill;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
}
