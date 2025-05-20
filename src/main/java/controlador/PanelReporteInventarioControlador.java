package controlador;

import vista.PanelReporteInventario;
import tablesModels.ProductoTableModel;
import DAO.CategoriaDAO;
import DAO.ReporteInventarioDAO;
import clases.Producto;
import clases.Categoria;
import clases.ReporteInventario;
import Printers.InventarioPrinter;
import vista.VistaImpresionTicket;
import main.Main;

import java.util.List;
import java.util.Date;
import java.util.stream.Collectors;

public class PanelReporteInventarioControlador {
    private final PanelReporteInventario vista;
    private ProductoTableModel productoTableModel;

    public PanelReporteInventarioControlador(PanelReporteInventario vista) {
        this.vista = vista;
        cargarCategorias();
        agregarListenerComboCategorias();
        agregarListenerGenerarReporte();
        consultarTotalesInventario();
    }

    public void setProductoTableModel(ProductoTableModel productoTableModel) {
        this.productoTableModel = productoTableModel;
    }

    private void cargarCategorias() {
        vista.comboCategorias.removeAllItems();
        vista.comboCategorias.addItem("Todos");
        List<Categoria> categorias = new CategoriaDAO().obtenerTodas();
        for (Categoria cat : categorias) {
            vista.comboCategorias.addItem(cat.getNombre());
        }
    }

    private void agregarListenerComboCategorias() {
        vista.comboCategorias.addActionListener(e -> {
            consultarTotalesInventario();
            actualizarTablaProductosPorCategoria();
        });
    }

    private void agregarListenerGenerarReporte() {
        vista.btnGenerarReporte.addActionListener(e -> generarReporteInventario());
    }

    private void generarReporteInventario() {
        String categoriaSeleccionada = (String) vista.comboCategorias.getSelectedItem();
        List<Producto> productos;
        if (categoriaSeleccionada != null && !"Todos".equals(categoriaSeleccionada)) {
            productos = main.Main.inventario.getProductos().stream()
                .filter(p -> {
                    Categoria cat = p.getCategoriaId() != 0 ? new CategoriaDAO().obtenerPorId(p.getCategoriaId()) : null;
                    return cat != null && categoriaSeleccionada.equals(cat.getNombre());
                })
                .collect(Collectors.toList());
        } else {
            productos = main.Main.inventario.getProductos();
        }
        int totalProductos = productos.size();
        int productosBajoMinimo = 0;
        double valorTotalInventario = 0.0;
        for (Producto p : productos) {
            if (p.getExistencias() < p.getExistenciaMinima()) productosBajoMinimo++;
            valorTotalInventario += p.getPrecioCompra() * p.getExistencias();
        }
        String comentarios = "Reporte generado automáticamente";
        int idUsuario = (Main.usuarioActual != null) ? Main.usuarioActual.getId() : 0;

        ReporteInventario reporte = new ReporteInventario(
            0,
            new Date(),
            idUsuario,
            productos,
            totalProductos,
            productosBajoMinimo,
            valorTotalInventario,
            comentarios
        );

        // Inserta en la base de datos
        boolean exito = new ReporteInventarioDAO().insertar(reporte);

        // Imprime el reporte en la ventana de impresión usando InventarioPrinter
        VistaImpresionTicket vistaImpresion = new VistaImpresionTicket();
        vistaImpresion.setTitle("Vista de Impresión de Reporte de Inventario");
        vistaImpresion.mostrarTextoPersonalizado(InventarioPrinter.imprimir(productos));
        vistaImpresion.setVisible(true);

        // Mensaje en consola
        if (exito) {
            System.out.println("Reporte de inventario guardado en la base de datos.");
        } else {
            System.out.println("No se pudo guardar el reporte de inventario en la base de datos.");
        }
    }

    private void consultarTotalesInventario() {
        String categoriaSeleccionada = (String) vista.comboCategorias.getSelectedItem();
        List<Producto> productos;
        if (categoriaSeleccionada != null && !"Todos".equals(categoriaSeleccionada)) {
            productos = main.Main.inventario.getProductos().stream()
                .filter(p -> {
                    Categoria cat = p.getCategoriaId() != 0 ? new CategoriaDAO().obtenerPorId(p.getCategoriaId()) : null;
                    return cat != null && categoriaSeleccionada.equals(cat.getNombre());
                })
                .collect(Collectors.toList());
        } else {
            productos = main.Main.inventario.getProductos();
        }
        int cantidad = productos.size();
        double total = 0.0;
        double stockTotal = 0.0;
        for (Producto p : productos) {
            total += p.getExistencias() * p.getPrecioCompra();
            stockTotal += p.getExistencias();
        }
        vista.setCantidadProductos(cantidad);
        vista.setValorTotal(total);
        vista.setStockTotal(stockTotal);
    }

    private void actualizarTablaProductosPorCategoria() {
        if (productoTableModel == null) return;
        String categoriaSeleccionada = (String) vista.comboCategorias.getSelectedItem();
        List<Producto> productos;
        if (categoriaSeleccionada != null && !"Todos".equals(categoriaSeleccionada)) {
            productos = main.Main.inventario.getProductos().stream()
                .filter(p -> {
                    Categoria cat = p.getCategoriaId() != 0 ? new CategoriaDAO().obtenerPorId(p.getCategoriaId()) : null;
                    return cat != null && categoriaSeleccionada.equals(cat.getNombre());
                })
                .collect(Collectors.toList());
        } else {
            productos = main.Main.inventario.getProductos();
        }
        productoTableModel.setProductos(productos);
        productoTableModel.fireTableDataChanged();
    }
}
