package Clases;

import java.util.Date;

public class Producto {
    private int id;
    private String codigo;
    private String nombre;
    private double precioVenta;
    private double precioCompra;
    private int categoriaId;
    private String categoriaNombre;
    private double existencias;
    private double existenciaMinima;
    private double existenciaMaxima;
    private int idProveedor;
    private String proveedorNombre;
    private double iva; // En porcentaje
    private double ieps; // En porcentaje

    private Date fechaDeCreacion;
    private String tipoProducto;

    // Constructor completo
    public Producto(int id, String codigo, String nombre, double precioVenta, double precioCompra, int categoriaId,
                    String categoriaNombre, double existencias, double existenciaMinima, double existenciaMaxima,
                    int idProveedor, String proveedorNombre, double iva, double ieps,
                    Date fechaDeCreacion, String tipoProducto) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.precioVenta = precioVenta;
        this.precioCompra = precioCompra;
        this.categoriaId = categoriaId;
        this.categoriaNombre = categoriaNombre;
        this.existencias = existencias;
        this.existenciaMinima = existenciaMinima;
        this.existenciaMaxima = existenciaMaxima;
        this.idProveedor = idProveedor;
        this.proveedorNombre = proveedorNombre;
        this.iva = iva;
        this.ieps = ieps;
        this.fechaDeCreacion = fechaDeCreacion;
        this.tipoProducto = tipoProducto;
    }

    // Constructor alternativo para compatibilidad con datos tipo String
    public Producto(String codigo, String nombre, String precioVenta, String precioCompra, String categoriaNombre,
                    String existencias, String existenciaMinima, String existenciaMaxima, String proveedorNombre,
                    String iva, String ieps, String tipoDeVenta, String tipoProducto) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precioVenta = parseDouble(precioVenta);
        this.precioCompra = parseDouble(precioCompra);
        this.categoriaNombre = categoriaNombre;
        this.existencias = parseDouble(existencias);
        this.existenciaMinima = parseDouble(existenciaMinima);
        this.existenciaMaxima = parseDouble(existenciaMaxima);
        this.proveedorNombre = proveedorNombre;
        this.iva = parseDouble(iva);
        this.ieps = parseDouble(ieps);
        this.tipoProducto = tipoProducto;
        this.fechaDeCreacion = new Date();
    }

    // Constructor recomendado para el controlador (usando tipos correctos)
    public Producto(String codigo, String nombre, double precioVenta, double precioCompra, String categoriaNombre,
                    double existencias, double existenciaMinima, double existenciaMaxima, String proveedorNombre,
                    double iva, double ieps, String tipoDeVenta, String tipoProducto) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precioVenta = precioVenta;
        this.precioCompra = precioCompra;
        this.categoriaNombre = categoriaNombre;
        this.existencias = existencias;
        this.existenciaMinima = existenciaMinima;
        this.existenciaMaxima = existenciaMaxima;
        this.proveedorNombre = proveedorNombre;
        this.iva = iva;
        this.ieps = ieps;
        this.tipoProducto = tipoProducto;
        this.fechaDeCreacion = new java.util.Date();
    }

    private double parseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            return 0.0;
        }
    }

    public boolean esValido() {
        return codigo != null && !codigo.isEmpty() &&
               nombre != null && !nombre.isEmpty() &&
               precioVenta > 0 && precioCompra >= 0 &&
               existencias >= 0 && existenciaMinima >= 0 && existenciaMaxima >= 0 &&
               categoriaNombre != null && !categoriaNombre.equals("Seleccionar") &&
               proveedorNombre != null && !proveedorNombre.equals("Seleccionar");
    }

    public double calcularPrecioFinal() {
        return precioVenta + (precioVenta * iva / 100) + (precioVenta * ieps / 100);
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public double getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(double precioVenta) { this.precioVenta = precioVenta; }
    public double getPrecioCompra() { return precioCompra; }
    public void setPrecioCompra(double precioCompra) { this.precioCompra = precioCompra; }
    public int getCategoriaId() { return categoriaId; }
    public void setCategoriaId(int categoriaId) { this.categoriaId = categoriaId; }
    public String getCategoriaNombre() { return categoriaNombre; }
    public void setCategoriaNombre(String categoriaNombre) { this.categoriaNombre = categoriaNombre; }
    public double getExistencias() { return existencias; }
    public void setExistencias(double existencias) { this.existencias = existencias; }
    public double getExistenciaMinima() { return existenciaMinima; }
    public void setExistenciaMinima(double existenciaMinima) { this.existenciaMinima = existenciaMinima; }
    public double getExistenciaMaxima() { return existenciaMaxima; }
    public void setExistenciaMaxima(double existenciaMaxima) { this.existenciaMaxima = existenciaMaxima; }
    public int getIdProveedor() { return idProveedor; }
    public void setIdProveedor(int idProveedor) { this.idProveedor = idProveedor; }
    public String getProveedorNombre() { return proveedorNombre; }
    public void setProveedorNombre(String proveedorNombre) { this.proveedorNombre = proveedorNombre; }
    public double getIva() { return iva; }
    public void setIva(double iva) { this.iva = iva; }
    public double getIeps() { return ieps; }
    public void setIeps(double ieps) { this.ieps = ieps; }

    public Date getFechaDeCreacion() { return fechaDeCreacion; }
    public void setFechaDeCreacion(Date fechaDeCreacion) { this.fechaDeCreacion = fechaDeCreacion; }
    public String getTipoProducto() { return tipoProducto; }
    public void setTipoProducto(String tipoProducto) { this.tipoProducto = tipoProducto; }
}
