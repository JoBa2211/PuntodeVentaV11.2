package Clases;

import java.util.Date;
import java.util.List;

/**
 * Esta clase representa un reporte de inventario que contiene información
 * sobre el estado actual de los productos en stock.
 */
public class ReporteInventario {
    private int id;
    private Date fechaCreacion;
    private int idUsuario; // Usuario que generó el reporte
    private List<Producto> productos;
    private int totalProductos;
    private int productosBajoMinimo;
    private double valorTotalInventario;
    private String comentarios;

    /**
     * Constructor para crear un nuevo reporte de inventario
     * 
     * @param id Identificador único del reporte
     * @param fechaCreacion Fecha en que se generó el reporte
     * @param idUsuario ID del usuario que generó el reporte
     * @param productos Lista de productos incluidos en el reporte
     * @param totalProductos Cantidad total de productos en inventario
     * @param productosBajoMinimo Cantidad de productos bajo el mínimo de existencias
     * @param valorTotalInventario Valor monetario total del inventario
     * @param comentarios Notas adicionales sobre el reporte
     */
    public ReporteInventario(int id, Date fechaCreacion, int idUsuario, List<Producto> productos,
                            int totalProductos, int productosBajoMinimo, double valorTotalInventario,
                            String comentarios) {
        this.id = id;
        this.fechaCreacion = fechaCreacion;
        this.idUsuario = idUsuario;
        this.productos = productos;
        this.totalProductos = totalProductos;
        this.productosBajoMinimo = productosBajoMinimo;
        this.valorTotalInventario = valorTotalInventario;
        this.comentarios = comentarios;
    }

    /**
     * Calcula el valor total del inventario basado en la lista de productos
     * @return Valor total del inventario
     */
    public double calcularValorTotalInventario() {
        double valorTotal = 0.0;
        for (Producto producto : productos) {
            // Para ambos tipos, se usa getPrecioCompra() * getExistencias()
            valorTotal += producto.getPrecioCompra() * producto.getExistencias();
        }
        return valorTotal;
    }

    /**
     * Cuenta la cantidad de productos que están bajo el mínimo de existencias
     * @return Número de productos bajo mínimo
     */
    public int contarProductosBajoMinimo() {
        int contador = 0;
        for (Producto producto : productos) {
            // Para ambos tipos, se compara existencias con existenciaMinima
            if (producto.getExistencias() < producto.getExistenciaMinima()) {
                contador++;
            }
        }
        return contador;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public int getTotalProductos() {
        return totalProductos;
    }

    public void setTotalProductos(int totalProductos) {
        this.totalProductos = totalProductos;
    }

    public int getProductosBajoMinimo() {
        return productosBajoMinimo;
    }

    public void setProductosBajoMinimo(int productosBajoMinimo) {
        this.productosBajoMinimo = productosBajoMinimo;
    }

    public double getValorTotalInventario() {
        return valorTotalInventario;
    }

    public void setValorTotalInventario(double valorTotalInventario) {
        this.valorTotalInventario = valorTotalInventario;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }
}
