package duoc.vista;

import duoc.controlador.ControladorVentas;
import duoc.modelo.DetalleVenta;
import duoc.modelo.Venta;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DetalleVentas extends JFrame {

    private final String rol;

    private final DefaultTableModel modelVentas = new DefaultTableModel(
            new String[]{"ID Venta", "Fecha", "Total"}, 0
    ) { @Override public boolean isCellEditable(int r, int c) { return false; } };

    private final DefaultTableModel modelDetalle = new DefaultTableModel(
            new String[]{"ID Detalle", "Producto", "Cantidad", "Precio Unit."}, 0
    ) { @Override public boolean isCellEditable(int r, int c) { return false; } };

    private final JTable tblVentas = new JTable(modelVentas);
    private final JTable tblDetalle = new JTable(modelDetalle);

    private final JButton btnVolver = new JButton("Volver");

    private final ControladorVentas controladorVentas = new ControladorVentas();

    public DetalleVentas(String rol) {
        this.rol = rol;

        setTitle("Reportes - Detalle de Ventas");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 480);
        setLocationRelativeTo(null);

        setContentPane(ui());
        eventos();

        cargarVentas();
    }

    private JPanel ui() {
        JPanel root = new JPanel(new BorderLayout(10,10));
        root.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        JSplitPane split = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(tblVentas),
                new JScrollPane(tblDetalle)
        );
        split.setResizeWeight(0.45);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(btnVolver);

        root.add(split, BorderLayout.CENTER);
        root.add(bottom, BorderLayout.SOUTH);
        return root;
    }

    private void eventos() {
        btnVolver.addActionListener(e -> { new TiendaVerduras(rol).setVisible(true); dispose(); });

        tblVentas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int fila = tblVentas.getSelectedRow();
                if (fila >= 0) {
                    int idVenta = Integer.parseInt(modelVentas.getValueAt(fila, 0).toString());
                    cargarDetalle(idVenta);
                }
            }
        });
    }

    private void cargarVentas() {
        modelVentas.setRowCount(0);
        for (Venta v : controladorVentas.listarVentas()) {
            modelVentas.addRow(new Object[]{ v.getId(), v.getFecha(), v.getTotal() });
        }
    }

    private void cargarDetalle(int idVenta) {
        modelDetalle.setRowCount(0);
        for (DetalleVenta d : controladorVentas.listarDetallePorVenta(idVenta)) {
            modelDetalle.addRow(new Object[]{
                    d.getId(),
                    d.getNombreProducto(),
                    d.getCantidad(),
                    d.getPrecioUnitario()
            });
        }
    }
}