package duoc.vista;

import duoc.controlador.ControladorUsuarios;
import duoc.modelo.Usuario;
import duoc.util.Seguridad;

import javax.swing.*;
import java.awt.*;

public class Login extends JFrame {

    private final JTextField txtCorreo = new JTextField(18);
    private final JPasswordField txtPass = new JPasswordField(18);
    private final JButton btnAcceder = new JButton("Acceder");
    private final JButton btnSalir = new JButton("Salir");

    private final ControladorUsuarios controladorUsuarios = new ControladorUsuarios();

    public Login() {
        setTitle("Login - Tienda Verduras (DAO)");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(380, 220);
        setLocationRelativeTo(null);
        setResizable(false);

        setContentPane(crearPanel());
        enlazar();
    }

    private JPanel crearPanel() {
        JPanel root = new JPanel(new BorderLayout(10,10));
        root.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

        JPanel form = new JPanel(new GridLayout(2,2,8,8));
        form.add(new JLabel("Correo:"));
        form.add(txtCorreo);
        form.add(new JLabel("Contraseña:"));
        form.add(txtPass);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.add(btnAcceder);
        actions.add(btnSalir);

        root.add(form, BorderLayout.CENTER);
        root.add(actions, BorderLayout.SOUTH);
        return root;
    }

    private void enlazar() {
        btnAcceder.addActionListener(e -> autenticar());
        btnSalir.addActionListener(e -> System.exit(0));
    }

    private void autenticar() {
        String correo = txtCorreo.getText().trim();
        String pass = new String(txtPass.getPassword());

        if (correo.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Completa correo y contraseña.");
            return;
        }

        String passHash = Seguridad.sha256Hex(pass);
        Usuario u = controladorUsuarios.autenticar(correo, passHash);

        if (u != null) {
            JOptionPane.showMessageDialog(this, "Bienvenido: " + u.getNombre() + " (" + u.getRol() + ")");
            new TiendaVerduras(u.getRol()).setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Credenciales inválidas o usuario inactivo.");
        }
    }
}