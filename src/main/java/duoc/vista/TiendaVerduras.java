package duoc.vista;

import duoc.controlador.ControladorCategorias;
import duoc.controlador.ControladorProductos;
import duoc.modelo.Categoria;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TiendaVerduras extends JFrame {

    private final String rol;

    private final JTextField txtNombre = new JTextField(15);
    private final JComboBox<Categoria> cmbCategoria = new JComboBox<>();
    private final JSpinner spStock = new JSpinner(new SpinnerNumberModel(1, 1, 10000, 1));
    private final JSpinner spValor = new JSpinner(new SpinnerNumberModel(1, 1, 100000, 10));

    private final JButton btnAgregar = new JButton("Agregar");
    private final JButton btnEditar = new JButton("Editar");
    private final JButton btnEliminar = new JButton("Eliminar");
    private final JButton btnLimpiar = new JButton("Limpiar");

    private final JToolBar toolBar = new JToolBar();

    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID", "Nombre", "Categoría", "Stock", "Valor"}, 0
    ) { @Override public boolean isCellEditable(int r, int c) { return false; } };

    private final JTable tbl = new JTable(model);

    private final ControladorProductos controladorProductos = new ControladorProductos();
    private final ControladorCategorias controladorCategorias = new ControladorCategorias();

    private int filaSeleccionada = -1;

    public TiendaVerduras(String rol) {
        this.rol = rol;

        setTitle("Tienda Verduras - Rol: " + rol);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(780, 420);
        setLocationRelativeTo(null);

        setContentPane(crearUI());
        enlazarEventos();

        cargarCategorias();
        controladorProductos.cargarTabla(model);
        ocultarColumnaId();

        aplicarRestriccionesPorRol();
    }

    private JPanel crearUI() {
        JPanel root = new JPanel(new BorderLayout(10,10));
        root.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        // toolbar
        toolBar.setFloatable(false);
        JButton btnProductos = new JButton("Productos");
        JButton btnUsuarios = new JButton("Usuarios");
        JButton btnReportes = new JButton("Reportes");
        toolBar.add(btnProductos);
        toolBar.add(btnUsuarios);
        toolBar.add(btnReportes);

        btnProductos.addActionListener(e -> JOptionPane.showMessageDialog(this, "Ya estás en Productos."));
        btnUsuarios.addActionListener(e -> { new GestionUsuarios(rol).setVisible(true); dispose(); });
        btnReportes.addActionListener(e -> { new DetalleVentas(rol).setVisible(true); dispose(); });

        JPanel form = new JPanel(new GridLayout(2,4,8,8));
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

        root.add(toolBar, BorderLayout.NORTH);
        root.add(form, BorderLayout.CENTER);
        root.add(new JScrollPane(tbl), BorderLayout.SOUTH);

        // armamos un contenedor para tabla + botones
        JPanel bottom = new JPanel(new BorderLayout(10,10));
        bottom.add(new JScrollPane(tbl), BorderLayout.CENTER);
        bottom.add(actions, BorderLayout.SOUTH);

        JPanel mid = new JPanel(new BorderLayout(10,10));
        mid.add(form, BorderLayout.NORTH);
        mid.add(bottom, BorderLayout.CENTER);

        JPanel main = new JPanel(new BorderLayout(10,10));
        main.add(toolBar, BorderLayout.NORTH);
        main.add(mid, BorderLayout.CENTER);

        return main;
    }

    private void enlazarEventos() {
        btnAgregar.addActionListener(e -> agregar());
        btnEditar.addActionListener(e -> editar());
        btnEliminar.addActionListener(e -> eliminar());
        btnLimpiar.addActionListener(e -> limpiar());

        tbl.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                int fila = tbl.getSelectedRow();
                if (fila >= 0) {
                    filaSeleccionada = fila;
                    txtNombre.setText(model.getValueAt(fila, 1).toString());

                    // buscar categoria por nombre
                    String catTabla = model.getValueAt(fila, 2).toString().trim().toLowerCase();
                    for (int i = 0; i < cmbCategoria.getItemCount(); i++) {
                        Categoria c = cmbCategoria.getItemAt(i);
                        if (c.getNombre().toLowerCase().equals(catTabla)) {
                            cmbCategoria.setSelectedIndex(i);
                            break;
                        }
                    }

                    spStock.setValue(Integer.parseInt(model.getValueAt(fila, 3).toString()));
                    spValor.setValue(Integer.parseInt(model.getValueAt(fila, 4).toString()));
                }
            }
        });
    }

    private void aplicarRestriccionesPorRol() {
        // guía: admin tiene todo, empleado restringido
        boolean esAdmin = "admin".equalsIgnoreCase(rol);

        btnAgregar.setEnabled(esAdmin);
        btnEditar.setEnabled(esAdmin);
        btnEliminar.setEnabled(esAdmin);

        if (!esAdmin) setTitle(getTitle() + " (Vista restringida)");
    }

    private void ocultarColumnaId() {
        tbl.getColumnModel().getColumn(0).setMinWidth(0);
        tbl.getColumnModel().getColumn(0).setMaxWidth(0);
        tbl.getColumnModel().getColumn(0).setPreferredWidth(0);
    }

    private void cargarCategorias() {
        cmbCategoria.removeAllItems();
        for (Categoria c : controladorCategorias.listarCategorias()) {
            cmbCategoria.addItem(c);
        }
        if (cmbCategoria.getItemCount() > 0) cmbCategoria.setSelectedIndex(0);
    }

    private boolean validar() {
        if (txtNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debes ingresar nombre.");
            return false;
        }
        if (cmbCategoria.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Debes seleccionar categoría.");
            return false;
        }
        int stock = (int) spStock.getValue();
        int valor = (int) spValor.getValue();
        if (stock <= 0 || valor <= 0) {
            JOptionPane.showMessageDialog(this, "Stock y valor deben ser > 0.");
            return false;
        }
        return true;
    }

    private void agregar() {
        if (!validar()) return;

        Categoria cat = (Categoria) cmbCategoria.getSelectedItem();
        controladorProductos.agregarProducto(
                txtNombre.getText().trim(),
                cat.getId(),
                (int) spStock.getValue(),
                (int) spValor.getValue()
        );
        controladorProductos.cargarTabla(model);
        limpiar();
    }

    private void editar() {
        if (filaSeleccionada < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto.");
            return;
        }
        if (!validar()) return;

        int id = Integer.parseInt(model.getValueAt(filaSeleccionada, 0).toString());
        Categoria cat = (Categoria) cmbCategoria.getSelectedItem();

        controladorProductos.actualizarProducto(
                id,
                txtNombre.getText().trim(),
                cat.getId(),
                (int) spStock.getValue(),
                (int) spValor.getValue()
        );
        controladorProductos.cargarTabla(model);
        limpiar();
    }

    private void eliminar() {
        int fila = tbl.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto.");
            return;
        }

        int ok = JOptionPane.showConfirmDialog(this, "¿Eliminar producto?", "Confirmación",
                JOptionPane.YES_NO_OPTION);

        if (ok == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(model.getValueAt(fila, 0).toString());
            controladorProductos.eliminarProductoPorId(id);
            controladorProductos.cargarTabla(model);
            limpiar();
        }
    }

    private void limpiar() {
        txtNombre.setText("");
        if (cmbCategoria.getItemCount() > 0) cmbCategoria.setSelectedIndex(0);
        spStock.setValue(1);
        spValor.setValue(1);
        filaSeleccionada = -1;
        tbl.clearSelection();
    }
}