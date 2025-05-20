package vista;

import main.Main;

import javax.swing.*;
import java.awt.*;
import javax.swing.text.PlainDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class PanelAgregarProducto extends JPanel {
    public JTextField txtCodigo;
    public JTextField txtNombre;
    public JTextField txtPrecioVenta;
    public JTextField txtPrecioCompra;
    public JComboBox<String> comboCategoria;
    public JTextField txtExistencias;
    public JTextField txtExistenciaMinima;
    public JTextField txtExistenciaMaxima;
    public JComboBox<String> comboProveedor;
    public JTextField txtIva;
    public JTextField txtIeps;
    public JCheckBox chkUnitario;
    public JCheckBox chkGranel;
    public JButton btnAgregar;
    public JButton btnEditar;    // Nuevo
    public JButton btnEliminar;  // Nuevo


    public PanelAgregarProducto() {
        // Panel izquierdo: formulario
        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new GridBagLayout());
        panelFormulario.setOpaque(false);
        panelFormulario.setPreferredSize(new Dimension(420, 700));
        // Margen en los bordes del formulario
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 0, 8, 0); // Sin margen horizontal entre label y campo
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        int y = 0;

        // Código
        gbc.gridx = 0; gbc.gridy = y; gbc.anchor = GridBagConstraints.LINE_END;
        panelFormulario.add(new JLabel("Código:"), gbc);
        txtCodigo = new JTextField(15);
        PlainDocument doc = new PlainDocument();
        doc.setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string == null) return;
                if ((fb.getDocument().getLength() + string.length()) <= 13) {
                    super.insertString(fb, offset, string, attr);
                }
            }
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text == null) return;
                if ((fb.getDocument().getLength() - length + text.length()) <= 13) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
        txtCodigo.setDocument(doc);
        gbc.gridx = 1; gbc.gridy = y; gbc.anchor = GridBagConstraints.LINE_START;
        panelFormulario.add(txtCodigo, gbc);

        y++;
        gbc.gridx = 0; gbc.gridy = y; gbc.anchor = GridBagConstraints.LINE_END;
        panelFormulario.add(new JLabel("Nombre:"), gbc);
        txtNombre = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = y; gbc.anchor = GridBagConstraints.LINE_START;
        panelFormulario.add(txtNombre, gbc);

        y++;
        gbc.gridx = 0; gbc.gridy = y; gbc.anchor = GridBagConstraints.LINE_END;
        panelFormulario.add(new JLabel("Precio Venta:"), gbc);
        txtPrecioVenta = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = y; gbc.anchor = GridBagConstraints.LINE_START;
        panelFormulario.add(txtPrecioVenta, gbc);

        y++;
        gbc.gridx = 0; gbc.gridy = y; gbc.anchor = GridBagConstraints.LINE_END;
        panelFormulario.add(new JLabel("Precio Compra:"), gbc);
        txtPrecioCompra = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = y; gbc.anchor = GridBagConstraints.LINE_START;
        panelFormulario.add(txtPrecioCompra, gbc);

        y++;
        gbc.gridx = 0; gbc.gridy = y; gbc.anchor = GridBagConstraints.LINE_END;
        panelFormulario.add(new JLabel("Categoría:"), gbc);
        comboCategoria = new JComboBox<>();
        gbc.gridx = 1; gbc.gridy = y; gbc.anchor = GridBagConstraints.LINE_START;
        panelFormulario.add(comboCategoria, gbc);

        y++;
        gbc.gridx = 0; gbc.gridy = y; gbc.anchor = GridBagConstraints.LINE_END;
        panelFormulario.add(new JLabel("Existencias:"), gbc);
        txtExistencias = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = y; gbc.anchor = GridBagConstraints.LINE_START;
        panelFormulario.add(txtExistencias, gbc);

        y++;
        gbc.gridx = 0; gbc.gridy = y; gbc.anchor = GridBagConstraints.LINE_END;
        panelFormulario.add(new JLabel("Existencia Mínima:"), gbc);
        txtExistenciaMinima = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = y; gbc.anchor = GridBagConstraints.LINE_START;
        panelFormulario.add(txtExistenciaMinima, gbc);

        y++;
        gbc.gridx = 0; gbc.gridy = y; gbc.anchor = GridBagConstraints.LINE_END;
        panelFormulario.add(new JLabel("Existencia Máxima:"), gbc);
        txtExistenciaMaxima = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = y; gbc.anchor = GridBagConstraints.LINE_START;
        panelFormulario.add(txtExistenciaMaxima, gbc);

        y++;
        gbc.gridx = 0; gbc.gridy = y; gbc.anchor = GridBagConstraints.LINE_END;
        panelFormulario.add(new JLabel("Proveedor:"), gbc);
        comboProveedor = new JComboBox<>();
        gbc.gridx = 1; gbc.gridy = y; gbc.anchor = GridBagConstraints.LINE_START;
        panelFormulario.add(comboProveedor, gbc);

        y++;
        gbc.gridx = 0; gbc.gridy = y; gbc.anchor = GridBagConstraints.LINE_END;
        panelFormulario.add(new JLabel("IVA (%):"), gbc);
        txtIva = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = y; gbc.anchor = GridBagConstraints.LINE_START;
        panelFormulario.add(txtIva, gbc);

        y++;
        gbc.gridx = 0; gbc.gridy = y; gbc.anchor = GridBagConstraints.LINE_END;
        panelFormulario.add(new JLabel("IEPS (%):"), gbc);
        txtIeps = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = y; gbc.anchor = GridBagConstraints.LINE_START;
        panelFormulario.add(txtIeps, gbc);

        y++;
        gbc.gridx = 0; gbc.gridy = y; gbc.anchor = GridBagConstraints.LINE_END;
        panelFormulario.add(new JLabel("Tipo:"), gbc);
        JPanel panelTipo = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        chkUnitario = new JCheckBox("Unitario");
        chkGranel = new JCheckBox("A granel");
        panelTipo.add(chkUnitario);
        panelTipo.add(chkGranel);
        gbc.gridx = 1; gbc.gridy = y; gbc.anchor = GridBagConstraints.LINE_START;
        panelFormulario.add(panelTipo, gbc);

        chkUnitario.addActionListener(e -> {
            if (chkUnitario.isSelected()) chkGranel.setSelected(false);
        });
        chkGranel.addActionListener(e -> {
            if (chkGranel.isSelected()) chkUnitario.setSelected(false);
        });

        y++;
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        btnAgregar = new JButton("Agregar Producto");
        btnAgregar.setBackground(new Color(46, 204, 113)); // Verde
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setFocusPainted(false);
        btnAgregar.setOpaque(true);
        btnAgregar.setBorderPainted(false);
        btnAgregar.setFont(new Font("Arial", Font.BOLD, 18)); // Texto más grande
        btnAgregar.setPreferredSize(new Dimension(220, 45)); // Botón más grande
        panelFormulario.add(btnAgregar, gbc);

        // Agregar botones Editar y Eliminar debajo del botón Agregar
        y++;
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.LINE_END;
        btnEditar = new JButton("Editar");
        btnEditar.setBackground(new Color(52, 152, 219)); // Azul
        btnEditar.setForeground(Color.WHITE);
        btnEditar.setFocusPainted(false);
        btnEditar.setOpaque(true);
        btnEditar.setBorderPainted(false);
        btnEditar.setFont(new Font("Arial", Font.BOLD, 16)); // Texto más grande
        btnEditar.setPreferredSize(new Dimension(110, 40)); // Botón más grande
        panelFormulario.add(btnEditar, gbc);

        gbc.gridx = 1; gbc.gridy = y; gbc.anchor = GridBagConstraints.LINE_START;
        btnEliminar = new JButton("Eliminar");
        btnEliminar.setBackground(new Color(231, 76, 60)); // Rojo
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFocusPainted(false);
        btnEliminar.setOpaque(true);
        btnEliminar.setBorderPainted(false);
        btnEliminar.setFont(new Font("Arial", Font.BOLD, 16)); // Texto más grande
        btnEliminar.setPreferredSize(new Dimension(110, 40)); // Botón más grande
        panelFormulario.add(btnEliminar, gbc);

        // --- El listener de selección de producto y llenado de campos ahora está en el controlador ---

        // Asegúrate de agregar el formulario al panel principal
        setLayout(new BorderLayout());
        add(panelFormulario, BorderLayout.CENTER);
    }
}
