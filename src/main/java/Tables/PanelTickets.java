package Tables;

import clases.Ticket;
import tablesModels.TicketTableModel;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class PanelTickets extends JPanel {
    private TicketTableModel model;
    private JTable table;
    private JComboBox<String> filtroComboBox;
    private Runnable actualizarTablaCallback;
    private JButton btnBorrar; // Nuevo botón

    public PanelTickets(List<Ticket> tickets) {
        setLayout(new BorderLayout());

        // Panel superior con botón "Borrar" y filtro
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnBorrar = new JButton("Borrar");
        topPanel.add(btnBorrar);

        filtroComboBox = new JComboBox<>(new String[]{"Día", "Semana", "Mes", "Año", "Todos"});
        topPanel.add(new JLabel("Filtrar por:"));
        topPanel.add(filtroComboBox);
        add(topPanel, BorderLayout.NORTH);

        model = new TicketTableModel(tickets);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Llama al callback de actualización cuando el panel se muestra
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                if (actualizarTablaCallback != null) {
                    actualizarTablaCallback.run();
                    // El controlador debe llamar a model.fireTableDataChanged() después de actualizar los datos
                }
            }
        });
    }

    public void setTickets(List<Ticket> tickets) {
        model.setTickets(tickets);
        // El controlador debe llamar a model.fireTableDataChanged() después de actualizar los datos
    }

    public Ticket getTicketSeleccionado() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            return model.getTicketAt(table.convertRowIndexToModel(row));
        }
        return null;
    }

    public JComboBox<String> getFiltroComboBox() {
        return filtroComboBox;
    }

    public JTable getTable() {
        return table;
    }

    // Permite que el controlador registre el callback de actualización
    public void setActualizarTablaCallback(Runnable callback) {
        this.actualizarTablaCallback = callback;
    }

    // Nuevo: getter para el botón borrar
    public JButton getBtnBorrar() {
        return btnBorrar;
    }

    public void setBtnBorrarEnabled(boolean enabled) {
        btnBorrar.setEnabled(enabled);
    }

    // Nuevo: permite al controlador refrescar la tabla
    public void fireTableDataChanged() {
        model.fireTableDataChanged();
    }
}
