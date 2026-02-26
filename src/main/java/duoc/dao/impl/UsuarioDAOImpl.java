package duoc.dao.impl;

import duoc.dao.UsuarioDAO;
import duoc.modelo.Usuario;
import duoc.util.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAOImpl implements UsuarioDAO {

    private final Connection conexion;

    public UsuarioDAOImpl() {
        this.conexion = ConexionBD.getConexion();
    }

    @Override
    public Usuario autenticar(String correo, String contrasenia) {
        // columna con ñ: se protege con backticks
        String sql = "SELECT * FROM usuarios WHERE correo = ? AND `contraseña` = ? AND activo = true";

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, correo);
            stmt.setString(2, contrasenia); // debe venir sha256Hex desde la vista/controlador

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                            rs.getInt("id_usuario"),
                            rs.getString("nombre"),
                            rs.getString("correo"),
                            "", // por seguridad, no devolvemos la contraseña
                            rs.getString("rol"),
                            rs.getBoolean("activo")
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al autenticar usuario", e);
        }

        return null;
    }

    @Override
    public void insertar(Usuario usuario) {
        String sql = "INSERT INTO usuarios(nombre, correo, `contraseña`, rol, activo) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getCorreo());
            stmt.setString(3, usuario.getContraseña()); // ✅ sin ñ (recomendado)
            stmt.setString(4, usuario.getRol());
            stmt.setBoolean(5, usuario.isActivo());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar usuario", e);
        }
    }

    @Override
    public void actualizar(Usuario usuario) {
        String sql = "UPDATE usuarios SET nombre=?, correo=?, `contraseña`=?, rol=?, activo=? WHERE id_usuario=?";

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getCorreo());
            stmt.setString(3, usuario.getContraseña()); // ✅ sin ñ (recomendado)
            stmt.setString(4, usuario.getRol());
            stmt.setBoolean(5, usuario.isActivo());
            stmt.setInt(6, usuario.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar usuario", e);
        }
    }

    @Override
    public void eliminar(int idUsuario) {
        String sql = "DELETE FROM usuarios WHERE id_usuario=?";

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar usuario", e);
        }
    }

    @Override
    public Usuario buscarPorId(int idUsuario) {
        String sql = "SELECT * FROM usuarios WHERE id_usuario=?";

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                            rs.getInt("id_usuario"),
                            rs.getString("nombre"),
                            rs.getString("correo"),
                            "",
                            rs.getString("rol"),
                            rs.getBoolean("activo")
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar usuario por ID", e);
        }

        return null;
    }

    @Override
    public List<Usuario> listarTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY id_usuario DESC";

        try (PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        "",
                        rs.getString("rol"),
                        rs.getBoolean("activo")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar usuarios", e);
        }

        return lista;
    }
}