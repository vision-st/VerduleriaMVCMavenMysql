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

    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"Nombre", "Categoría", "Stock", "Valor"}, 0
    ) {
        @Override public boolean isCellEditable(int row, int col) { return false; }
    };

    private final JTable tbl = new JTable(model);

    private int filaSeleccionada = -1;

    public TiendaVerduras(String rol) {
        this.rol = rol;

        setTitle("Verdulería al Paso - Rol: " + rol);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(780, 420);
        setLocationRelativeTo(null);

        setContentPane(crearUI());
        enlazarEventos();

        controlador.cargarProductosDesdeBD(model);
        aplicarRestriccionesPorRol();
    }

    private JPanel crearUI() {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // FORM
        JPanel form = new JPanel(new GridLayout(2, 4, 8, 8));
        form.add(new JLabel("Nombre:"));
        form.add(txtNombre);
        form.add(new JLabel("Categoría:"));
        form.add(cmbCategoria);
        form.add(new JLabel("Stock:"));
        form.add(spStock);
        form.add(new JLabel("Valor:"));
        form.add(spValor);

        // BUTTONS
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.add(btnAgregar);
        actions.add(btnEditar);
        actions.add(btnEliminar);
        actions.add(btnLimpiar);

        // TABLE
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
                filaSeleccionada = tbl.getSelectedRow();
                if (filaSeleccionada >= 0) cargarFormularioDesdeTabla(filaSeleccionada);
            }
        });
    }

    private void aplicarRestriccionesPorRol() {
        // Semana 6: se restringen funciones según rol (admin/vendedor). :contentReference[oaicite:19]{index=19}
        if ("vendedor".equalsIgnoreCase(rol)) {
            btnEditar.setEnabled(false);
            btnEliminar.setEnabled(false);
            setTitle(getTitle() + " (Vista restringida)");
        }
    }

    private void agregarProducto() {
        String nombre = txtNombre.getText().trim();
        CategoriaProducto categoria = (CategoriaProducto) cmbCategoria.getSelectedItem();
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
        if (filaSeleccionada < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto para editar.");
            return;
        }

        String nombre = txtNombre.getText().trim();
        CategoriaProducto categoria = (CategoriaProducto) cmbCategoria.getSelectedItem();
        int stock = (int) spStock.getValue();
        int valor = (int) spValor.getValue();

        String error = validar(nombre, stock, valor);
        if (error != null) {
            JOptionPane.showMessageDialog(this, error, "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Producto p = new Producto(nombre, categoria, stock, valor);
        //controlador.editarProducto(filaSeleccionada, p, model);
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
            //controlador.eliminarProducto(fila, model);
            limpiar();
        }
    }

    private void cargarFormularioDesdeTabla(int fila) {
        txtNombre.setText(model.getValueAt(fila, 0).toString());
        cmbCategoria.setSelectedItem(model.getValueAt(fila, 1));
        spStock.setValue(Integer.parseInt(model.getValueAt(fila, 2).toString()));
        spValor.setValue(Integer.parseInt(model.getValueAt(fila, 3).toString()));
    }

    private void limpiar() {
        txtNombre.setText("");
        cmbCategoria.setSelectedIndex(0);
        spStock.setValue(1);
        spValor.setValue(1);
        filaSeleccionada = -1;
        tbl.clearSelection();
    }

    private String validar(String nombre, int stock, int valor) {
        if (nombre.isEmpty()) return "El nombre es obligatorio.";
        if (stock <= 0) return "El stock debe ser mayor a 0.";
        if (valor <= 0) return "El valor debe ser positivo.";
        return null;
    }
}
