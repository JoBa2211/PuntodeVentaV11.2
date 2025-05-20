package clases;

import DAO.ProductoDAO;
import java.util.ArrayList;
import java.util.List;

public class Inventario {
    private final List<Producto> productos;
    private final ProductoDAO productoDAO;

    public Inventario() {
        this.productoDAO = new ProductoDAO();
        this.productos = new ArrayList<>(productoDAO.obtenerTodos());
        imprimirProductosEnConsola(); // Imprime todos los productos al crear la instancia
    }

    public List<Producto> getProductos() {
        return new ArrayList<>(productos);
    }

    public void agregarProducto(Producto producto) {
        if (productoDAO.insertar(producto)) {
            productos.add(producto);
            imprimirProductosEnConsola();
        }
    }

    public void eliminarProducto(Producto producto) {
        if (productoDAO.eliminar(producto.getId())) {
            productos.removeIf(p -> p.getId() == producto.getId());
            imprimirProductosEnConsola();
        }
    }

    public void actualizarProducto(Producto producto) {
        if (productoDAO.actualizar(producto)) {
            for (int i = 0; i < productos.size(); i++) {
                if (productos.get(i).getId() == producto.getId()) {
                    productos.set(i, producto);
                    break;
                }
            }
            imprimirProductosEnConsola();
        }
    }

    // Guarda todos los productos actuales en la base de datos (por ejemplo, al cerrar)
    public void guardar() {
        for (Producto producto : productos) {
            productoDAO.actualizar(producto);
        }
        imprimirProductosEnConsola();
    }

    // Imprime la lista de productos en consola
    private void imprimirProductosEnConsola() {
        if (productos.isEmpty()) {
            System.out.println("=== La lista de productos está vacía ===");
        } else {
            System.out.println("=== Lista de productos actualizada ===");
            for (Producto producto : productos) {
                System.out.println(producto);
            }
            System.out.println("======================================");
        }
    }

    // Nuevo método para imprimir todos los productos de la lista en cualquier momento
    public void imprimirTodosLosProductos() {
        imprimirProductosEnConsola();
    }
}
