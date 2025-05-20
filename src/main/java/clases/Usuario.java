package Clases;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Usuario {
    private String usuario; // Nombre de usuario
    private String contraseña; // Contraseña del usuario
    private String rol; // Rol del usuario (ej. Administrador, Usuario)

    // Constructor
    public Usuario(String usuario, String contraseña, String rol) {
        this.usuario = usuario;
        this.contraseña = contraseña;
        this.rol = rol;
    }

    // Getters y Setters para acceder y modificar los atributos
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    /**
     * Clase para representar una devolución.
     */
    public static class Devoluciones {
        private String id; // ID de la devolución
        private List<ProductoCantidad> productos; // Lista de productos y sus cantidades
        private String razon; // Razón de la devolución
        private Date fecha; // Fecha de la devolución
        private double dineroDevuelto; // Dinero devuelto
        private String idUsuario; // ID del usuario que realizó la devolución

        /**
         * Constructor para inicializar una devolución.
         * @param id ID de la devolución.
         * @param razon Razón de la devolución.
         * @param fecha Fecha de la devolución.
         * @param dineroDevuelto Dinero devuelto.
         * @param idUsuario ID del usuario que realizó la devolución.
         */
        public Devoluciones(String id, String razon, Date fecha, double dineroDevuelto, String idUsuario) {
            this.id = id;
            this.productos = new ArrayList<>();
            this.razon = razon;
            this.fecha = fecha;
            this.dineroDevuelto = dineroDevuelto;
            this.idUsuario = idUsuario;
        }

        // Getters y setters
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<ProductoCantidad> getProductos() {
            return productos;
        }

        public void setProductos(List<ProductoCantidad> productos) {
            this.productos = productos;
        }

        public String getRazon() {
            return razon;
        }

        public void setRazon(String razon) {
            this.razon = razon;
        }

        public Date getFecha() {
            return fecha;
        }

        public void setFecha(Date fecha) {
            this.fecha = fecha;
        }

        public double getDineroDevuelto() {
            return dineroDevuelto;
        }

        public void setDineroDevuelto(double dineroDevuelto) {
            this.dineroDevuelto = dineroDevuelto;
        }

        public String getIdUsuario() {
            return idUsuario;
        }

        public void setIdUsuario(String idUsuario) {
            this.idUsuario = idUsuario;
        }

        /**
         * Agrega un producto y su cantidad a la lista de productos.
         * @param idProducto ID del producto.
         * @param cantidad Cantidad del producto.
         */
        public void agregarProducto(String idProducto, int cantidad) {
            this.productos.add(new ProductoCantidad(idProducto, cantidad));
        }

        /**
         * Clase interna para representar un producto y su cantidad.
         */
        public static class ProductoCantidad {
            private String idProducto; // ID del producto
            private int cantidad; // Cantidad del producto

            public ProductoCantidad(String idProducto, int cantidad) {
                this.idProducto = idProducto;
                this.cantidad = cantidad;
            }

            public String getIdProducto() {
                return idProducto;
            }

            public void setIdProducto(String idProducto) {
                this.idProducto = idProducto;
            }

            public int getCantidad() {
                return cantidad;
            }

            public void setCantidad(int cantidad) {
                this.cantidad = cantidad;
            }
        }
    }
}
