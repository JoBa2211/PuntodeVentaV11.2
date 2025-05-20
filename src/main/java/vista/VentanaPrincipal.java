package vista;

import javax.swing.*;
import java.awt.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Tables.PanelTickets;
import controlador.PanelTicketsControlador;
import controlador.VistaImpresionTicketControlador;
import controlador.PanelAgregarProveedorControlador;
import controlador.JFrameTablaBusquedaControlador;
import tablesModels.ProductoTableModel;
import DAO.ProductoDAO;
import tablesModels.CategoriaTableModel;
import DAO.CategoriaDAO;
import Tables.ProveedorTableModel;
import DAO.ProveedorDAO;
import clases.Inventario;
import vista.PanelCrearTicket;
import controlador.PanelCrearTicketControlador;
import tablesModels.RefillTableModel;
import DAO.RefillDAO;
import tablesModels.DevolucionesTableModel;
import DAO.DevolucionesDAO;
import main.Main; // Importa Main para acceder al inventario
import vista.PanelDevoluciones;
import controlador.PanelDevolucionesControlador;
import controlador.ProductosBajoMinimoControlador;

public class VentanaPrincipal extends JFrame {

    public JMenuBar menuBar;
    public JMenu menuLogin; // Quitados: menuInventario, menuHistorialVentas, menuVentas, menuReportes
    public JMenuItem itemInventario;
    public JTabbedPane tabbedPane; // El JTabbedPane para las pestañas principales

    // NUEVO: Menú y label para productos bajo mínimo
    private JMenu menuProductosBajoMinimo;
    private JLabel lblProductosBajoMinimo;

    // NUEVO: JLabel para mostrar el usuario actual
    private JLabel lblUsuarioActual;

    public VentanaPrincipal() {
        super("Punto de Venta - Ventana Principal");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 720); // Ventana más grande (1280 x 720)
        setLocationRelativeTo(null);

        // Crear barra de menú
        menuBar = new JMenuBar();

        menuLogin = new JMenu("Login");
        // Quitados: menuInventario, menuHistorialVentas, menuVentas, menuReportes
        // menuInventario = new JMenu("Inventario");
        // itemInventario = new JMenuItem("Inventario");
        // menuInventario.add(itemInventario);

        // menuHistorialVentas = new JMenu("Historial de Ventas");
        // menuVentas = new JMenu("Ventas");
        // menuReportes = new JMenu("Reportes");

        menuBar.add(menuLogin);
        // Quitados del menú superior:
        // menuBar.add(menuInventario);
        // menuBar.add(menuHistorialVentas);
        // menuBar.add(menuVentas);
        // menuBar.add(menuReportes);

        // NUEVO: Menú "Productos bajo mínimo" y label pegada a la izquierda
        menuProductosBajoMinimo = new JMenu("Productos bajo mínimo");
        lblProductosBajoMinimo = new JLabel("0");
        lblProductosBajoMinimo.setForeground(Color.RED);
        lblProductosBajoMinimo.setBorder(null); // Sin borde
        JPanel panelBajoMinimo = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelBajoMinimo.setOpaque(false);
        panelBajoMinimo.add(lblProductosBajoMinimo);
        menuBar.add(menuProductosBajoMinimo);
        menuBar.add(panelBajoMinimo);

        // NUEVO: Usuario actual alineado a la derecha
        lblUsuarioActual = new JLabel("Usuario: ---");
        lblUsuarioActual.setHorizontalAlignment(SwingConstants.RIGHT);
        lblUsuarioActual.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 10));
        JPanel panelUsuario = new JPanel(new BorderLayout());
        panelUsuario.add(lblUsuarioActual, BorderLayout.EAST);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(panelUsuario);

        setJMenuBar(menuBar);

        // --- NUEVO: Controlador para productos bajo mínimo y registro de observer ---
        ProductosBajoMinimoControlador productosBajoMinimoControlador = new ProductosBajoMinimoControlador(this);
        // Registra el observer en todos los productos del inventario principal
        for (clases.Producto p : main.Main.inventario.getProductos()) {
            p.addStockObserver(productosBajoMinimoControlador);
        }

        // Listener para mostrar la ventana de productos bajo mínimo
        menuProductosBajoMinimo.addMenuListener(new javax.swing.event.MenuListener() {
            @Override
            public void menuSelected(javax.swing.event.MenuEvent e) {
                productosBajoMinimoControlador.limpiar();
                // Fuerza la notificación para actualizar la lista
                for (clases.Producto p : main.Main.inventario.getProductos()) {
                    p.getExistencias();
                }
                productosBajoMinimoControlador.mostrarDialogo();
            }
            @Override public void menuDeselected(javax.swing.event.MenuEvent e) {}
            @Override public void menuCanceled(javax.swing.event.MenuEvent e) {}
        });

        // Agrega el JTabbedPane al centro de la ventana
        tabbedPane = new JTabbedPane();

        // 1. Nueva Venta
        // --- NUEVO: Panel de productos a la izquierda, PanelCrearTicket a la derecha ---
        Inventario inventarioNuevaVenta = new Inventario();
        ProductoTableModel productoTableModelNuevaVenta = new ProductoTableModel();
        productoTableModelNuevaVenta.setProductos(inventarioNuevaVenta.getProductos());
        JFrameTablaBusqueda tablaProductosNuevaVenta = new JFrameTablaBusqueda("Productos");
        JFrameTablaBusquedaControlador tablaProductosNuevaVentaControlador = new JFrameTablaBusquedaControlador(tablaProductosNuevaVenta, productoTableModelNuevaVenta);

        // Instancia PanelCrearTicket y su controlador
        PanelCrearTicket panelCrearTicket = new PanelCrearTicket();
        PanelCrearTicketControlador panelCrearTicketControlador = new PanelCrearTicketControlador(panelCrearTicket, inventarioNuevaVenta);
        // Enlaza el controlador externo de productos al PanelCrearTicketControlador
        panelCrearTicketControlador.setTablaProductosControlador(tablaProductosNuevaVentaControlador);

        // Ajusta el tamaño preferido del panelCrearTicket para que ocupe el 50% del espacio
        panelCrearTicket.setPreferredSize(new Dimension(getWidth() / 2, getHeight()));

        JSplitPane splitPaneNuevaVenta = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            tablaProductosNuevaVenta.getContentPane(),
            panelCrearTicket
        );
        splitPaneNuevaVenta.setResizeWeight(0.5); // 50% y 50%
        splitPaneNuevaVenta.setDividerSize(10);
        splitPaneNuevaVenta.setContinuousLayout(true);

        tabbedPane.addTab("Nueva Venta", splitPaneNuevaVenta);

        // 2. Historial de Ventas
        PanelTickets panelTickets = new PanelTickets(new java.util.ArrayList<>());
        VistaImpresionTicket vistaImpresionTicket = new VistaImpresionTicket();
        VistaImpresionTicketControlador vistaImpresionTicketControlador = new VistaImpresionTicketControlador(vistaImpresionTicket);

        // Usar el controlador para sincronizar la selección
        PanelTicketsControlador panelTicketsControlador = new PanelTicketsControlador(panelTickets, vistaImpresionTicket);

        // Panel combinado con JSplitPane
        JSplitPane splitPaneHistorial = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            panelTickets,
            vistaImpresionTicket.getContentPane()
        );
        splitPaneHistorial.setResizeWeight(0.7);
        splitPaneHistorial.setDividerSize(10);
        splitPaneHistorial.setContinuousLayout(true);

        tabbedPane.addTab("Historial de Ventas", splitPaneHistorial);

        // 3. Inventario (con subpestañas)
        JTabbedPane inventarioTabbedPane = new JTabbedPane();

        // --- USAR Inventario para productos ---
        Inventario inventario = new Inventario();

        // --- USAR JFrameTablaBusqueda en vez de PanelProductos ---
        ProductoTableModel productoTableModel = new ProductoTableModel();
        productoTableModel.setProductos(inventario.getProductos());
        JFrameTablaBusqueda panelTablaBusqueda = new JFrameTablaBusqueda("Inventario de Productos");
        new JFrameTablaBusquedaControlador(panelTablaBusqueda, productoTableModel);
        inventarioTabbedPane.addTab("Inventario", panelTablaBusqueda.getContentPane());

        // --- NUEVO: Agregar Producto con JFrameTablaBusqueda a la derecha ---
        PanelAgregarProducto panelAgregarProducto = new PanelAgregarProducto();
        panelAgregarProducto.setVisible(true); 
        ProductoTableModel productoTableModelAgregar = new ProductoTableModel();
        productoTableModelAgregar.setProductos(inventario.getProductos());
        JFrameTablaBusqueda tablaBusquedaAgregar = new JFrameTablaBusqueda("Buscar Producto");
        JFrameTablaBusquedaControlador tablaBusquedaAgregarControlador = new JFrameTablaBusquedaControlador(tablaBusquedaAgregar, productoTableModelAgregar);

        // Instanciar el controlador y conectar la vista y el controlador de la tabla
        new controlador.PanelAgregarProductoControlador(
            panelAgregarProducto,
            tablaBusquedaAgregarControlador,
            inventario,
            productoTableModelAgregar
        );

        JSplitPane splitPaneAgregarProducto = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            panelAgregarProducto,
            tablaBusquedaAgregar.getContentPane()
        );
        splitPaneAgregarProducto.setResizeWeight(0.4);
        splitPaneAgregarProducto.setDividerSize(10);
        splitPaneAgregarProducto.setContinuousLayout(true);

        inventarioTabbedPane.addTab("Agregar Producto", splitPaneAgregarProducto);

        // --- Panel para formulario y tabla de categorías ---
        PanelAgregarCategoria panelAgregarCategoria = new PanelAgregarCategoria();

        // NUEVO: Usar JFrameTablaBusqueda para categorías
        CategoriaTableModel categoriaTableModel = new CategoriaTableModel(new CategoriaDAO().obtenerTodas());
        JFrameTablaBusqueda panelTablaCategorias = new JFrameTablaBusqueda("Categorías");
        JFrameTablaBusquedaControlador tablaCategoriasControlador = new JFrameTablaBusquedaControlador(panelTablaCategorias, categoriaTableModel);

        // Instanciar el controlador de PanelAgregarCategoriaControlador (FALTABA)
        new controlador.PanelAgregarCategoriaControlador(panelAgregarCategoria, tablaCategoriasControlador);

        // Panel combinado con JSplitPane
        JSplitPane splitPaneCategorias = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            panelAgregarCategoria,
            panelTablaCategorias.getContentPane()
        );
        splitPaneCategorias.setResizeWeight(0.33);
        splitPaneCategorias.setDividerSize(10);
        splitPaneCategorias.setContinuousLayout(true);

        inventarioTabbedPane.addTab("Agregar Categoría", splitPaneCategorias);

        // --- Panel para formulario y tabla de proveedores ---
        PanelAgregarProveedor panelAgregarProveedor = new PanelAgregarProveedor();

        // NUEVO: Usar JFrameTablaBusqueda para proveedores
        ProveedorTableModel proveedorTableModel = new ProveedorTableModel(new ProveedorDAO().obtenerTodos());
        JFrameTablaBusqueda panelTablaProveedores = new JFrameTablaBusqueda("Proveedores");
        JFrameTablaBusquedaControlador tablaProveedoresControlador = new JFrameTablaBusquedaControlador(panelTablaProveedores, proveedorTableModel);

        // Instanciar el controlador de PanelAgregarProveedorControlador y pasar el controlador de la tabla
        new PanelAgregarProveedorControlador(panelAgregarProveedor, tablaProveedoresControlador);

        // El menú (panelAgregarProveedor) a la izquierda, la tabla a la derecha
        JSplitPane splitPaneProveedores = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            panelAgregarProveedor,
            panelTablaProveedores.getContentPane()
        );
        splitPaneProveedores.setResizeWeight(0.3); // Más espacio para la tabla a la derecha
        splitPaneProveedores.setDividerSize(10);
        splitPaneProveedores.setContinuousLayout(true);

        inventarioTabbedPane.addTab("Agregar Proveedor", splitPaneProveedores);

        tabbedPane.addTab("Inventario", inventarioTabbedPane);

        // 4. Reabastecimientos
        JTabbedPane reabastecimientosTabbedPane = new JTabbedPane();

        // Panel de menú de reabastecimientos (ya creado)
        PanelReabastecimientos panelReabastecimientos = new PanelReabastecimientos();

        // Modelo y tabla para refills
        RefillTableModel refillTableModel = new RefillTableModel(new RefillDAO().obtenerTodos());
        JFrameTablaBusqueda tablaRefills = new JFrameTablaBusqueda("Refills");
        JFrameTablaBusquedaControlador tablaRefillsControlador = new JFrameTablaBusquedaControlador(tablaRefills, refillTableModel);

        // USAR la instancia de inventario principal
        ProductoTableModel productoTableModelRefill = new ProductoTableModel();
        productoTableModelRefill.setProductos(inventario.getProductos());
        JFrameTablaBusqueda tablaProductosRefill = new JFrameTablaBusqueda("Productos");
        JFrameTablaBusquedaControlador tablaProductosRefillControlador = new JFrameTablaBusquedaControlador(tablaProductosRefill, productoTableModelRefill);

        // Panel contenedor vertical para menú y tabla de productos
        JPanel panelIzquierdoRefills = new JPanel();
        panelIzquierdoRefills.setLayout(new BorderLayout(0, 10));
        panelIzquierdoRefills.add(panelReabastecimientos, BorderLayout.NORTH);
        panelIzquierdoRefills.add(tablaProductosRefill.getContentPane(), BorderLayout.CENTER);

        // SplitPane para Refills: menú+tabla productos a la izquierda, tabla de refills a la derecha
        JSplitPane splitPaneRefills = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            panelIzquierdoRefills,
            tablaRefills.getContentPane()
        );
        splitPaneRefills.setResizeWeight(0.3);
        splitPaneRefills.setDividerSize(10);
        splitPaneRefills.setContinuousLayout(true);

        // CORREGIDO: Instanciar el controlador para el panel de reabastecimientos y pasar el controlador de la tabla de refills y el inventario
        controlador.PanelReabastecimientosControlador panelReabastecimientosControlador =
            new controlador.PanelReabastecimientosControlador(
                panelReabastecimientos,
                tablaRefillsControlador,
                tablaProductosRefillControlador,
                inventario // <-- Usa la instancia principal
            );

        reabastecimientosTabbedPane.addTab("Refills", splitPaneRefills);

        // --- NUEVO: Agregar la pestaña "Devoluciones" con menú y dos tablas (productos y devoluciones) ---
        // Panel de menú de devoluciones
        PanelDevoluciones panelDevoluciones = new PanelDevoluciones();
        PanelDevolucionesControlador panelDevolucionesControlador = new PanelDevolucionesControlador(panelDevoluciones);

        // Tabla de productos (izquierda)
        ProductoTableModel productoTableModelDevoluciones = new ProductoTableModel();
        productoTableModelDevoluciones.setProductos(main.Main.inventario.getProductos());
        JFrameTablaBusqueda tablaProductosDevoluciones = new JFrameTablaBusqueda("Productos");
        // Instancia y enlaza el controlador de la tabla de productos
        JFrameTablaBusquedaControlador tablaProductosDevolucionesControlador = new JFrameTablaBusquedaControlador(tablaProductosDevoluciones, productoTableModelDevoluciones);

        // Tabla de devoluciones (derecha)
        DevolucionesTableModel devolucionesModel = new DevolucionesTableModel(new DevolucionesDAO().obtenerTodas());
        JFrameTablaBusqueda tablaDevoluciones = new JFrameTablaBusqueda("Devoluciones");
        // Instancia y enlaza el controlador de la tabla de devoluciones
        JFrameTablaBusquedaControlador tablaDevolucionesControlador = new JFrameTablaBusquedaControlador(tablaDevoluciones, devolucionesModel);

        // NUEVO: Enlaza el controlador de la tabla de devoluciones al controlador del panel de devoluciones
        panelDevolucionesControlador.setTablaDevolucionesControlador(tablaDevolucionesControlador);

        // NUEVO: Enlaza el listener del controlador de la tabla de productos con el combo box del menú de devoluciones
        panelDevolucionesControlador.enlazarTablaProductos(tablaProductosDevolucionesControlador);

        // Panel izquierdo: menú arriba, tabla productos abajo
        JPanel panelIzquierdoDevoluciones = new JPanel(new BorderLayout(0, 10));
        panelIzquierdoDevoluciones.add(panelDevoluciones, BorderLayout.NORTH);
        panelIzquierdoDevoluciones.add(tablaProductosDevoluciones.getContentPane(), BorderLayout.CENTER);

        // Panel combinado con JSplitPane
        JSplitPane splitPaneDevoluciones = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            panelIzquierdoDevoluciones,
            tablaDevoluciones.getContentPane()
        );
        splitPaneDevoluciones.setResizeWeight(0.33);
        splitPaneDevoluciones.setDividerSize(10);
        splitPaneDevoluciones.setContinuousLayout(true);

        reabastecimientosTabbedPane.addTab("Devoluciones", splitPaneDevoluciones);

        tabbedPane.addTab("Reabastecimientos", reabastecimientosTabbedPane);

        // Enlaza el listener del controlador de la tabla de productos con el combo box del menú de reabastecimientos
        tablaProductosRefillControlador.setProductoSeleccionListener(producto -> {
            if (producto != null) {
                // Busca el índice en el combo que coincida con el display del producto
                String display = producto.getCodigo() + " - " + producto.getNombre() + " - $" + producto.getPrecioVenta() + " - " + producto.getExistencias();
                JComboBox<String> combo = panelReabastecimientos.comboProductos;
                for (int i = 0; i < combo.getItemCount(); i++) {
                    if (combo.getItemAt(i).equals(display)) {
                        combo.setSelectedIndex(i);
                        break;
                    }
                }
            }
        });

        // 5. Reportes (con subpestañas)
        JTabbedPane reportesTabbedPane = new JTabbedPane();

        // NUEVO: Panel de menú de reporte de ventas
        PanelReporteVentas panelReporteVentas = new PanelReporteVentas();
        controlador.PanelReporteVentasControlador panelReporteVentasControlador = new controlador.PanelReporteVentasControlador(panelReporteVentas);

        // NUEVO: Tabla de tickets en "Reporte Ventas"
        tablesModels.TicketTableModel ticketTableModelReporte = new tablesModels.TicketTableModel(new DAO.TicketDAO().obtenerTodos());
        JFrameTablaBusqueda tablaTicketsReporte = new JFrameTablaBusqueda("Tickets");
        JFrameTablaBusquedaControlador tablaTicketsReporteControlador = new JFrameTablaBusquedaControlador(tablaTicketsReporte, ticketTableModelReporte);

        // --- ENLAZA EL CONTROLADOR DE TICKETS CON EL CONTROLADOR DE LA TABLA DE BÚSQUEDA ---
        panelReporteVentasControlador.setTablaTicketsControlador(tablaTicketsReporteControlador); // NUEVO

        // Panel vertical: menú arriba, tabla abajo
        JPanel panelReporteVentasCompleto = new JPanel();
        panelReporteVentasCompleto.setLayout(new BorderLayout());
        panelReporteVentasCompleto.add(panelReporteVentas, BorderLayout.NORTH);
        panelReporteVentasCompleto.add(tablaTicketsReporte.getContentPane(), BorderLayout.CENTER);

        reportesTabbedPane.addTab("Reporte Ventas", panelReporteVentasCompleto);

        // NUEVO: Reporte Inventario con menú y tabla de productos desde Main.inventario
        JPanel panelReporteInventario = new JPanel(new BorderLayout());

        // Menú de reporte de inventario (MVC)
        vista.PanelReporteInventario panelMenuReporteInventario = new vista.PanelReporteInventario();
        ProductoTableModel productoTableModelReporte = new ProductoTableModel(main.Main.inventario.getProductos());
        JFrameTablaBusqueda tablaProductosReporteInventario = new JFrameTablaBusqueda("Inventario de Productos");
        JFrameTablaBusquedaControlador tablaProductosReporteInventarioControlador = new JFrameTablaBusquedaControlador(tablaProductosReporteInventario, productoTableModelReporte);

        // Controlador del menú (conecta el modelo de la tabla)
        controlador.PanelReporteInventarioControlador panelReporteInventarioControlador =
            new controlador.PanelReporteInventarioControlador(panelMenuReporteInventario);
        panelReporteInventarioControlador.setProductoTableModel(productoTableModelReporte);

        // Añade el menú arriba y la tabla abajo
        panelReporteInventario.add(panelMenuReporteInventario, BorderLayout.NORTH);
        panelReporteInventario.add(tablaProductosReporteInventario.getContentPane(), BorderLayout.CENTER);

        reportesTabbedPane.addTab("Reporte Inventario", panelReporteInventario);

        tabbedPane.addTab("Reportes", reportesTabbedPane);

        setContentPane(tabbedPane);

        // --- NUEVO: Actualizar tablas al cambiar de pestaña ---
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int selectedIndex = tabbedPane.getSelectedIndex();
                String selectedTitle = tabbedPane.getTitleAt(selectedIndex);

                // Nueva Venta
                if ("Nueva Venta".equals(selectedTitle)) {
                    tablaProductosNuevaVentaControlador.actualizar();

                }
                // Historial de Ventas
                else if ("Historial de Ventas".equals(selectedTitle)) {
                    panelTicketsControlador.cargarTicketsDia();
                }
                // Inventario
                else if ("Inventario".equals(selectedTitle)) {
                    int subIndex = inventarioTabbedPane.getSelectedIndex();
                    String subTitle = inventarioTabbedPane.getTitleAt(subIndex);
                    if ("Inventario".equals(subTitle)) {
                        panelTablaBusqueda.getTabla().repaint();
                    } else if ("Agregar Producto".equals(subTitle)) {
                        tablaBusquedaAgregarControlador.actualizar();
                    } else if ("Agregar Categoría".equals(subTitle)) {
                        tablaCategoriasControlador.actualizar();
                    } else if ("Agregar Proveedor".equals(subTitle)) {
                        tablaProveedoresControlador.actualizar();
                    }
                }
                // Reabastecimientos
                else if ("Reabastecimientos".equals(selectedTitle)) {
                    int subIndex = reabastecimientosTabbedPane.getSelectedIndex();
                    String subTitle = reabastecimientosTabbedPane.getTitleAt(subIndex);
                    if ("Refills".equals(subTitle)) {
                        tablaRefillsControlador.actualizar();
                        tablaProductosRefillControlador.actualizar();
                    } else if ("Devoluciones".equals(subTitle)) {
                        tablaProductosDevolucionesControlador.actualizar();
                        tablaDevolucionesControlador.actualizar();
                    }
                }
            }
        });

        // --- También puedes agregar ChangeListener a los subpestañas si quieres actualización inmediata al cambiar subpestaña ---
        inventarioTabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int subIndex = inventarioTabbedPane.getSelectedIndex();
                String subTitle = inventarioTabbedPane.getTitleAt(subIndex);
                if ("Inventario".equals(subTitle)) {
                    panelTablaBusqueda.getTabla().repaint();
                } else if ("Agregar Producto".equals(subTitle)) {
                    tablaBusquedaAgregarControlador.actualizar();
                } else if ("Agregar Categoría".equals(subTitle)) {
                    tablaCategoriasControlador.actualizar();
                } else if ("Agregar Proveedor".equals(subTitle)) {
                    tablaProveedoresControlador.actualizar();
                }
            }
        });

        reabastecimientosTabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int subIndex = reabastecimientosTabbedPane.getSelectedIndex();
                String subTitle = reabastecimientosTabbedPane.getTitleAt(subIndex);
                if ("Refills".equals(subTitle)) {
                    tablaRefillsControlador.actualizar();
                    tablaProductosRefillControlador.actualizar();
                } else if ("Devoluciones".equals(subTitle)) {
                    tablaProductosDevolucionesControlador.actualizar();
                    tablaDevolucionesControlador.actualizar();
                }
            }
        });

        setVisible(true);
    }

    // Permite al controlador acceder al tabbedPane
    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    // NUEVO: Método para actualizar el usuario mostrado
    public void setUsuarioActual(String usuario) {
        lblUsuarioActual.setText("Usuario: " + (usuario != null ? usuario : "---"));
    }

    // NUEVO: Método para actualizar el número de productos bajo mínimo
    public void setProductosBajoMinimo(int cantidad) {
        lblProductosBajoMinimo.setText(String.valueOf(cantidad));
    }
}
