package duoc.dao;

import duoc.modelo.Categoria;
import duoc.modelo.Usuario;

import java.util.List;

public interface UsuarioDAO {

    Usuario autenticar(String correo, String contrasenia);
    void insertar(Usuario usuario);
    void actualizar(Usuario usuario);
    void eliminar(int idUsuario);
    Usuario buscarPorId(int idUsuario);
    List<Usuario> listarTodos();

}
