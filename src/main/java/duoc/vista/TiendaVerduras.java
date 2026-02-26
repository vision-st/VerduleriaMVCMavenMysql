package duoc.vista;

import duoc.controlador.ControladorProductos;
import duoc.modelo.CategoriaProducto;
import duoc.modelo.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TiendaVerduras extends JFrame {

    private final String rol;
    private final ControladorProductos controlador = new ControladorProductos();

    private final JTextField txtNombre = new JTextField(15);
    private final JComboBox<CategoriaProducto> cmbCategoria = new JComboBox<>(CategoriaProducto.values());
    private final JSpinner spStock = new JSpinner(new SpinnerNumberModel(1, 1, 10000, 1));
    private final JSpinner spValor = new JSpinner(new SpinnerNumberModel(1, 1, 1000000, 10));

    private final JButton btnAgregar = new JButton("Agregar");
    private final JButton btnEditar = new JButton("Editar");
    private final JButton btnEliminar = new JButton("Eliminar");
    private final JButton btnLimpiar = new JButton("Limpiar");

    // ✅ 5 columnas: incluye ID
    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID", "Nombre", "Categoría", "Stock", "Valor"}, 0
    ) {
        @Override public boolean isCellEditable(int row, int col) { return false; }
    };

    private final JTable tbl = new JTable(model);

    public TiendaVerduras(String rol) {
        this.rol = rol;

        setTitle("Verdulería al Paso - Rol: " + rol);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(780, 420);
        setLocationRelativeTo(null);

        setContentPane(crearUI());
        enlazarEventos();

        controlador.cargarProductosDesdeBD(model);
        ocultarColumnaId(tbl);
        aplicarRestriccionesPorRol();
    }

    private JPanel crearUI() {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel form = new JPanel(new GridLayout(2, 4, 8, 8));
        form.add(new JLabel("Nombre:"));
        form.add(txtNombre);
        form.add(new JLabel("Categoría:"));
        form.add(cmbCategoria);
        form.add(new JLabel("Stock:"));
        form.add(spStock);
        form.add(new JLabel("Valor:"));
        form.add(spValor);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.add(btnAgregar);
        actions.add(btnEditar);
        actions.add(btnEliminar);
        actions.add(btnLimpiar);

        tbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(tbl);

        root.add(form, BorderLayout.NORTH);
        root.add(scroll, BorderLayout.CENTER);
        root.add(actions, BorderLayout.SOUTH);
        return root;
    }

    private void enlazarEventos() {
        btnAgregar.addActionListener(e -> agregarProducto());
        btnEditar.addActionListener(e -> editarProducto());
        btnEliminar.addActionListener(e -> eliminarProducto());
        btnLimpiar.addActionListener(e -> limpiar());

        tbl.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int fila = tbl.getSelectedRow();
                if (fila >= 0) cargarFormularioDesdeTabla(fila);
            }
        });
    }

    private void aplicarRestriccionesPorRol() {
        if ("vendedor".equalsIgnoreCase(rol)) {
            btnEditar.setEnabled(false);
            btnEliminar.setEnabled(false);
            setTitle(getTitle() + " (Vista restringida)");
        }
    }

    private void ocultarColumnaId(JTable tabla) {
        tabla.getColumnModel().getColumn(0).setMinWidth(0);
        tabla.getColumnModel().getColumn(0).setMaxWidth(0);
        tabla.getColumnModel().getColumn(0).setPreferredWidth(0);
    }

    private void agregarProducto() {
        String nombre = txtNombre.getText().trim();
        String categoria = cmbCategoria.getSelectedItem().toString(); // enfoque guía: String
        int stock = (int) spStock.getValue();
        int valor = (int) spValor.getValue();

        String error = validar(nombre, stock, valor);
        if (error != null) {
            JOptionPane.showMessageDialog(this, error, "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Producto p = new Producto(nombre, categoria, stock, valor);
        controlador.agregarProducto(p, model);
        limpiar();
    }

    private void editarProducto() {
        int fila = tbl.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto para editar.");
            return;
        }

        int id = Integer.parseInt(model.getValueAt(fila, 0).toString()); // ✅ ID

        String nombre = txtNombre.getText().trim();
        String categoria = cmbCategoria.getSelectedItem().toString();
        int stock = (int) spStock.getValue();
        int valor = (int) spValor.getValue();

        String error = validar(nombre, stock, valor);
        if (error != null) {
            JOptionPane.showMessageDialog(this, error, "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Producto p = new Producto(nombre, categoria, stock, valor);
        controlador.editarProducto(id, p, model);
        limpiar();
    }

    private void eliminarProducto() {
        int fila = tbl.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto para eliminar.");
            return;
        }

        int ok = JOptionPane.showConfirmDialog(
                this,
                "¿Eliminar el producto seleccionado?",
                "Confirmación",
                JOptionPane.YES_NO_OPTION
        );

        if (ok == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(model.getValueAt(fila, 0).toString()); // ✅ ID
            controlador.eliminarProducto(id, model);
            limpiar();
        }
    }

    private void cargarFormularioDesdeTabla(int fila) {
        txtNombre.setText(model.getValueAt(fila, 1).toString());
        cmbCategoria.setSelectedItem(model.getValueAt(fila, 2));
        spStock.setValue(Integer.parseInt(model.getValueAt(fila, 3).toString()));
        spValor.setValue(Integer.parseInt(model.getValueAt(fila, 4).toString()));
    }

    private void limpiar() {
        txtNombre.setText("");
        cmbCategoria.setSelectedIndex(0);
        spStock.setValue(1);
        spValor.setValue(1);
        tbl.clearSelection();
    }

    private String validar(String nombre, int stock, int valor) {
        if (nombre.isEmpty()) return "El nombre es obligatorio.";
        if (stock <= 0) return "El stock debe ser mayor a 0.";
        if (valor <= 0) return "El valor debe ser positivo.";
        return null;
    }
}