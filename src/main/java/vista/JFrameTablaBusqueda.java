package vista;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class JFrameTablaBusqueda extends JFrame {
    private JTable tabla;
    private JTextField buscadorTextField;
    private TableRowSorter<TableModel> sorter;
    private TableModel modeloActual; // NUEVO: Guarda el modelo actual

    public JFrameTablaBusqueda(String titulo) {
        setTitle(titulo != null ? titulo : "Búsqueda");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Panel superior con buscador
        JPanel panelBusqueda = new JPanel(new BorderLayout());
        buscadorTextField = new JTextField();
        panelBusqueda.add(new JLabel("Buscar: "), BorderLayout.WEST);
        panelBusqueda.add(buscadorTextField, BorderLayout.CENTER);

        // Tabla vacía, el modelo se pondrá después
        tabla = new JTable();
        tabla.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(tabla);

        // Layout principal
        setLayout(new BorderLayout());
        add(panelBusqueda, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Listener para filtrar al escribir (el sorter se asigna cuando se ponga el modelo)
        buscadorTextField.getDocument().addDocumentListener(new DocumentListener() {
            private void filtrar() {
                if (sorter == null) return;
                String texto = buscadorTextField.getText();
                if (texto == null || texto.trim().isEmpty()) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
                }
            }
            public void insertUpdate(DocumentEvent e) { filtrar(); }
            public void removeUpdate(DocumentEvent e) { filtrar(); }
            public void changedUpdate(DocumentEvent e) { filtrar(); }
        });

        // NUEVO: Actualiza la tabla cuando el frame se hace visible
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                if (modeloActual != null) {
                    actualizarTabla(modeloActual);
                }
            }
        });

        // NUEVO: Actualiza la tabla cuando el frame se vuelve visible (por ejemplo, al cambiar de pestaña)
        tabla.addHierarchyListener(e -> {
            if ((e.getChangeFlags() & java.awt.event.HierarchyEvent.SHOWING_CHANGED) != 0) {
                if (tabla.isShowing() && modeloActual != null) {
                    actualizarTabla(modeloActual);
                }
            }
        });
    }

    public void actualizarTabla(TableModel model) {
        this.modeloActual = model; // Guarda el modelo actual
        tabla.setModel(model);
        sorter = new TableRowSorter<>(model);
        tabla.setRowSorter(sorter);
    }

    public JTable getTabla() {
        return tabla;
    }

    public JTextField getBuscadorTextField() {
        return buscadorTextField;
    }
}
