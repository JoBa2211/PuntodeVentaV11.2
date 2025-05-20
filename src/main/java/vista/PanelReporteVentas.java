package vista;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.toedter.calendar.JDateChooser;
import controlador.JFrameTablaBusquedaControlador;
import tablesModels.ProductoTableModel;
import clases.Producto;
import java.util.List;
import DAO.ProductoDAO;

public class PanelReporteVentas extends JPanel {
    public JCheckBox chkDia, chkSemana, chkMes;
    public JLabel lblSubtotal, lblIVA, lblIEPS, lblTotal, lblCantidadTickets;
    public JButton btnGenerarReporte;
    public JDateChooser dateChooserInicio, dateChooserFin;
    public JCheckBox chkIntervaloPersonalizado;
    public JComboBox<String> comboProductos;
    public JFrameTablaBusquedaControlador tablaProductosControlador;
    private JFrameTablaBusqueda frameTablaProductos;

    public PanelReporteVentas() {
        setLayout(new BorderLayout());

        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel titulo = new JLabel("Reporte de Ventas");
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCentral.add(titulo);

        panelCentral.add(Box.createVerticalStrut(25));

        JPanel panelChecks = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        Font checkFont = new Font("Arial", Font.BOLD, 20);
        chkDia = new JCheckBox("Día");
        chkDia.setFont(checkFont);
        chkSemana = new JCheckBox("Semana");
        chkSemana.setFont(checkFont);
        chkMes = new JCheckBox("Mes");
        chkMes.setFont(checkFont);
        ButtonGroup group = new ButtonGroup();
        group.add(chkDia);
        group.add(chkSemana);
        group.add(chkMes);
        panelChecks.add(chkDia);
        panelChecks.add(chkSemana);
        panelChecks.add(chkMes);
        panelChecks.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCentral.add(panelChecks);
        chkDia.setSelected(false);
        chkSemana.setSelected(false);
        chkMes.setSelected(false);
        panelCentral.add(Box.createVerticalStrut(18));

        JPanel panelFechas = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        chkIntervaloPersonalizado = new JCheckBox("Intervalo personalizado");
        group.add(chkIntervaloPersonalizado);
        JLabel lblDe = new JLabel("De:");
        dateChooserInicio = new JDateChooser();
        dateChooserInicio.setDateFormatString("dd/MM/yyyy");
        JLabel lblA = new JLabel("a");
        dateChooserFin = new JDateChooser();
        dateChooserFin.setDateFormatString("dd/MM/yyyy");
        panelFechas.add(chkIntervaloPersonalizado);
        panelFechas.add(lblDe);
        panelFechas.add(dateChooserInicio);
        panelFechas.add(lblA);
        panelFechas.add(dateChooserFin);
        panelFechas.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCentral.add(panelFechas);
        panelCentral.add(Box.createVerticalStrut(18));

        JPanel panelProducto = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        comboProductos = new JComboBox<>();
        comboProductos.setFont(new Font("Arial", Font.PLAIN, 16));
        comboProductos.addItem("Todos los productos");
        panelProducto.add(comboProductos);
        panelProducto.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCentral.add(panelProducto);
        panelCentral.add(Box.createVerticalStrut(18));

        JPanel panelResumen = new JPanel();
        panelResumen.setLayout(new BoxLayout(panelResumen, BoxLayout.Y_AXIS));
        panelResumen.setAlignmentX(Component.CENTER_ALIGNMENT);
        Font labelFont = new Font("Arial", Font.BOLD, 16);
        lblSubtotal = new JLabel("Subtotal: $0.00", SwingConstants.CENTER);
        lblSubtotal.setFont(labelFont);
        lblSubtotal.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblIVA = new JLabel("IVA: $0.00", SwingConstants.CENTER);
        lblIVA.setFont(labelFont);
        lblIVA.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblIEPS = new JLabel("IEPS: $0.00", SwingConstants.CENTER);
        lblIEPS.setFont(labelFont);
        lblIEPS.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTotal = new JLabel("Total: $0.00", SwingConstants.CENTER);
        lblTotal.setFont(labelFont);
        lblTotal.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblCantidadTickets = new JLabel("Cantidad de Tickets: 0", SwingConstants.CENTER);
        lblCantidadTickets.setFont(labelFont);
        lblCantidadTickets.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelResumen.add(lblSubtotal);
        panelResumen.add(Box.createVerticalStrut(8));
        panelResumen.add(lblIVA);
        panelResumen.add(Box.createVerticalStrut(8));
        panelResumen.add(lblIEPS);
        panelResumen.add(Box.createVerticalStrut(8));
        panelResumen.add(lblTotal);
        panelResumen.add(Box.createVerticalStrut(8));
        panelResumen.add(lblCantidadTickets);
        panelCentral.add(panelResumen);
        panelCentral.add(Box.createVerticalStrut(20));

        btnGenerarReporte = new JButton("Generar Reporte");
        btnGenerarReporte.setFont(new Font("Arial", Font.BOLD, 20));
        btnGenerarReporte.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCentral.add(btnGenerarReporte);

        add(panelCentral, BorderLayout.CENTER);

        List<Producto> productos = new ProductoDAO().obtenerTodos();
        ProductoTableModel productoTableModel = new ProductoTableModel(productos);
        frameTablaProductos = new JFrameTablaBusqueda("Productos");
        tablaProductosControlador = new JFrameTablaBusquedaControlador(frameTablaProductos, productoTableModel);

        JPanel panelTabla = new JPanel(new BorderLayout());
        JTable tabla = tablaProductosControlador.getTabla();
        JScrollPane scroll = new JScrollPane(tabla);
        panelTabla.add(scroll, BorderLayout.CENTER);
        panelTabla.setPreferredSize(new Dimension(900, 400)); // Más ancho, altura estándar
        add(panelTabla, BorderLayout.EAST);

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tabla.getSelectedRow();
                if (row >= 0) {
                    int modelRow = tabla.convertRowIndexToModel(row);
                    ProductoTableModel model = (ProductoTableModel) tabla.getModel();
                    Producto prod = model.getProductoAt(modelRow);
                    if (prod != null) {
                        // Selecciona en la combobox usando el formato id - codigo - nombre
                        String clave = prod.getId() + " - " + prod.getCodigo() + " - " + prod.getNombre();
                        comboProductos.setSelectedItem(clave);
                    }
                }
            }
        });

        comboProductos.addActionListener(e -> {
            String seleccionado = (String) comboProductos.getSelectedItem();
            if (seleccionado != null && !"Todos los productos".equals(seleccionado)) {
                ProductoTableModel model = (ProductoTableModel) tabla.getModel();
                for (int i = 0; i < model.getRowCount(); i++) {
                    Producto prod = model.getProductoAt(i);
                    String clave = prod.getId() + " - " + prod.getCodigo() + " - " + prod.getNombre();
                    if (prod != null && seleccionado.equals(clave)) {
                        int viewRow = tabla.convertRowIndexToView(i);
                        tabla.getSelectionModel().setSelectionInterval(viewRow, viewRow);
                        tabla.scrollRectToVisible(tabla.getCellRect(viewRow, 0, true));
                        break;
                    }
                }
            } else {
                tabla.clearSelection();
            }
        });
    }
}
