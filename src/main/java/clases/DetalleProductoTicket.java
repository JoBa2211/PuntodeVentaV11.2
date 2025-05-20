package clases;

public class DetalleProductoTicket {
    private int idProducto;
    private String codigo; // Nuevo atributo: código del producto
    private double cantidad;
    private double precio; // Nuevo campo: precio al que se compró el producto
    private String nombre; // Nuevo atributo: nombre del producto
    private double iva;    // Nuevo atributo: iva del producto (porcentaje)
    private double ieps;   // Nuevo atributo: ieps del producto (porcentaje)

    // Constructor principal
    public DetalleProductoTicket(int idProducto, String codigo, double cantidad, double precio, String nombre, double iva, double ieps) {
        this.idProducto = idProducto;
        this.codigo = codigo;
        this.cantidad = cantidad;
        this.precio = precio;
        this.nombre = nombre;
        this.iva = iva;
        this.ieps = ieps;
    }

    // Constructores anteriores para compatibilidad
    public DetalleProductoTicket(int idProducto, String codigo, double cantidad, double precio, String nombre) {
        this(idProducto, codigo, cantidad, precio, nombre, 0.0, 0.0);
    }

    public DetalleProductoTicket(int idProducto, String codigo, double cantidad, double precio) {
        this(idProducto, codigo, cantidad, precio, "", 0.0, 0.0);
    }

    public DetalleProductoTicket(int idProducto, String codigo, double cantidad) {
        this(idProducto, codigo, cantidad, 0.0, "", 0.0, 0.0);
    }

    // Para compatibilidad con código existente que no usa código
    public DetalleProductoTicket(int idProducto, double cantidad, double precio, String nombre, double iva, double ieps) {
        this(idProducto, "", cantidad, precio, nombre, iva, ieps);
    }
    public DetalleProductoTicket(int idProducto, double cantidad, double precio, String nombre) {
        this(idProducto, "", cantidad, precio, nombre, 0.0, 0.0);
    }
    public DetalleProductoTicket(int idProducto, double cantidad, double precio) {
        this(idProducto, "", cantidad, precio, "", 0.0, 0.0);
    }
    public DetalleProductoTicket(int idProducto, double cantidad) {
        this(idProducto, "", cantidad, 0.0, "", 0.0, 0.0);
    }

    // Getters y Setters
    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public double getCantidad() { return cantidad; }
    public void setCantidad(double cantidad) { this.cantidad = cantidad; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public double getIva() { return iva; }
    public void setIva(double iva) { this.iva = iva; }
    public double getIeps() { return ieps; }
    public void setIeps(double ieps) { this.ieps = ieps; }
}
