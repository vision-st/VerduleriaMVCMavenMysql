package duoc.vista;

import duoc.controlador.ControladorUsuarios;
import duoc.util.Seguridad;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class GestionUsuarios extends JFrame {

    private final String rol;

    private final JTextField txtNombre = new JTextField(14);
    private final JTextField txtCorreo = new JTextField(14);
    private final JPasswordField txtPass = new JPasswordField(14);
    private final JComboBox<String> cmbRol = new JComboBox<>(new String[]{"admin", "empleado"});
    private final JCheckBox chkActivo = new JCheckBox("Activo", true);

    private final JButton btnAgregar = new JButton("Agregar");
    private final JButton btnEditar = new JButton("Editar");
    private final JButton btnEliminar = new JButton("Eliminar");
    private final JButton btnVolver = new JButton("Volver");

    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID", "Nombre", "Correo", "Rol", "Activo"}, 0
    ) { @Override public boolean isCellEditable(int r, int c) { return false; } };

    private final JTable tbl = new JTable(model);

    private final ControladorUsuarios controlador = new ControladorUsuarios();
    private int filaSel = -1;

    public GestionUsuarios(String rol) {
        this.rol = rol;

        setTitle("Gestión de Usuarios - Rol: " + rol);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(820, 420);
        setLocationRelativeTo(null);

        setContentPane(ui());
        eventos();

        controlador.cargarTabla(model);

        boolean esAdmin = "admin".equalsIgnoreCase(rol);
        btnAgregar.setEnabled(esAdmin);
        btnEditar.setEnabled(esAdmin);
        btnEliminar.setEnabled(esAdmin);
    }

    private JPanel ui() {
        JPanel root = new JPanel(new BorderLayout(10,10));
        root.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        JPanel form = new JPanel(new GridLayout(2,5,8,8));
        form.add(new JLabel("Nombre"));
        form.add(new JLabel("Correo"));
        form.add(new JLabel("Contraseña"));
        form.add(new JLabel("Rol"));
        form.add(new JLabel("Estado"));

        form.add(txtNombre);
        form.add(txtCorreo);
        form.add(txtPass);
        form.add(cmbRol);
        form.add(chkActivo);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.add(btnAgregar);
        actions.add(btnEditar);
        actions.add(btnEliminar);
        actions.add(btnVolver);

        root.add(form, BorderLayout.NORTH);
        root.add(new JScrollPane(tbl), BorderLayout.CENTER);
        root.add(actions, BorderLayout.SOUTH);

        return root;
    }

    private void eventos() {
        btnVolver.addActionListener(e -> { new TiendaVerduras(rol).setVisible(true); dispose(); });

        btnAgregar.addActionListener(e -> {
            String passHash = Seguridad.sha256Hex(new String(txtPass.getPassword()));
            controlador.agregarUsuario(
                    txtNombre.getText().trim(),
                    txtCorreo.getText().trim(),
                    passHash,
                    cmbRol.getSelectedItem().toString(),
                    chkActivo.isSelected()
            );
            controlador.cargarTabla(model);
            limpiar();
        });

        btnEditar.addActionListener(e -> {
            if (filaSel < 0) { JOptionPane.showMessageDialog(this, "Selecciona un usuario."); return; }
            int id = Integer.parseInt(model.getValueAt(filaSel, 0).toString());
            String passHash = Seguridad.sha256Hex(new String(txtPass.getPassword()));
            controlador.actualizarUsuario(
                    id,
                    txtNombre.getText().trim(),
                    txtCorreo.getText().trim(),
                    passHash,
                    cmbRol.getSelectedItem().toString(),
                    chkActivo.isSelected()
            );
            controlador.cargarTabla(model);
            limpiar();
        });

        btnEliminar.addActionListener(e -> {
            int fila = tbl.getSelectedRow();
            if (fila < 0) { JOptionPane.showMessageDialog(this, "Selecciona un usuario."); return; }
            int ok = JOptionPane.showConfirmDialog(this, "¿Eliminar usuario?", "Confirmación",
                    JOptionPane.YES_NO_OPTION);
            if (ok == JOptionPane.YES_OPTION) {
                int id = Integer.parseInt(model.getValueAt(fila, 0).toString());
                controlador.eliminarUsuario(id);
                controlador.cargarTabla(model);
                limpiar();
            }
        });

        tbl.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int fila = tbl.getSelectedRow();
                if (fila >= 0) {
                    filaSel = fila;
                    txtNombre.setText(model.getValueAt(fila, 1).toString());
                    txtCorreo.setText(model.getValueAt(fila, 2).toString());
                    cmbRol.setSelectedItem(model.getValueAt(fila, 3).toString());
                    chkActivo.setSelected("Sí".equalsIgnoreCase(model.getValueAt(fila, 4).toString()));
                    txtPass.setText(""); // por seguridad, no se rellena
                }
            }
        });
    }

    private void limpiar() {
        txtNombre.setText("");
        txtCorreo.setText("");
        txtPass.setText("");
        cmbRol.setSelectedIndex(1);
        chkActivo.setSelected(true);
        filaSel = -1;
        tbl.clearSelection();
    }
}