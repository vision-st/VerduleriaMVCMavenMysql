package duoc.controlador;

import duoc.dao.UsuarioDAO;
import duoc.dao.impl.UsuarioDAOImpl;
import duoc.modelo.Usuario;

import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ControladorUsuarios {

    private final UsuarioDAO usuarioDAO = new UsuarioDAOImpl();

    public Usuario autenticar(String correo, String contraseniaCifrada) {
        return usuarioDAO.autenticar(correo, contraseniaCifrada);
    }

    public void agregarUsuario(String nombre, String correo, String contraseniaCifrada, String rol, boolean activo) {
        if (nombre == null || correo == null || contraseniaCifrada == null ||
                nombre.trim().isEmpty() || correo.trim().isEmpty() || contraseniaCifrada.isEmpty()) {
            throw new IllegalArgumentException("Todos los campos deben estar completos.");
        }

        Usuario u = new Usuario();
        u.setNombre(nombre.trim());
        u.setCorreo(correo.trim());
        u.setContraseña(contraseniaCifrada);
        u.setRol(rol);
        u.setActivo(activo);

        usuarioDAO.insertar(u);
    }

    public void actualizarUsuario(int id, String nombre, String correo, String contraseniaCifrada, String rol, boolean activo) {
        if (id <= 0) throw new IllegalArgumentException("ID inválido.");

        Usuario u = new Usuario();
        u.setId(id);
        u.setNombre(nombre.trim());
        u.setCorreo(correo.trim());
        u.setContraseña(contraseniaCifrada);
        u.setRol(rol);
        u.setActivo(activo);

        usuarioDAO.actualizar(u);
    }

    public void eliminarUsuario(int id) {
        usuarioDAO.eliminar(id);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioDAO.listarTodos();
    }

    public void cargarTabla(DefaultTableModel modelo) {
        modelo.setRowCount(0);
        for (Usuario u : listarUsuarios()) {
            modelo.addRow(new Object[]{
                    u.getId(),
                    u.getNombre(),
                    u.getCorreo(),
                    u.getRol(),
                    u.isActivo() ? "Sí" : "No"
            });
        }
    }
}