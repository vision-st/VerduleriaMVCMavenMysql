package duoc.vista;

import duoc.controlador.ControladorUsuarios;
import duoc.modelo.Usuario;

import javax.swing.*;
import java.awt.*;

public class Login extends JFrame {

    private final JTextField txtUsuario = new JTextField(15);
    private final JPasswordField txtPass = new JPasswordField(15);
    private final JButton btnAcceder = new JButton("Acceder");
    private final JButton btnCancelar = new JButton("Cancelar");
    private final ControladorUsuarios controladorUsuarios;

    public Login(ControladorUsuarios controladorUsuarios){
        this.controladorUsuarios = controladorUsuarios;

        setTitle("Login Verduleria al paso");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(360, 220);
        setLocationRelativeTo(null);
        setResizable(false);

        setContentPane(crearPanel());
        enlazarEventos();
    }

    private JPanel crearPanel() {
        JPanel root = new JPanel(new BorderLayout(10,10));
        root.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

        JPanel form = new JPanel(new GridLayout(2,2,8,8));
        form.add(new JLabel("Usuario"));
        form.add(txtUsuario);
        form.add(new JLabel("Contraseña"));
        form.add(txtPass);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.add(btnAcceder);
        actions.add(btnCancelar);

        root.add(form, BorderLayout.CENTER);
        root.add(actions, BorderLayout.SOUTH);

        return root;
    }

    private void enlazarEventos() {
        btnAcceder.addActionListener(e -> authenticarUsuario());
        btnCancelar.addActionListener(e -> System.exit(0));
    }

    private void authenticarUsuario(){
        String nombre = txtUsuario.getText().trim();
        String pass = new String(txtPass.getPassword());

        Usuario usuario = controladorUsuarios.autenticar(nombre,pass);

        if(usuario != null){
            JOptionPane.showMessageDialog(this, "Bienvenido, rol: " + usuario.getRol());
            //TODO AQUI DEBEMOS MOSTRAR LA PANTALLA PRINCIPAL QUE SE LLAMARA TiendaVerduras()}
            new TiendaVerduras(usuario.getRol()).setVisible(true);
            //dispose();
        }else{
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos");
        }
    }
}
