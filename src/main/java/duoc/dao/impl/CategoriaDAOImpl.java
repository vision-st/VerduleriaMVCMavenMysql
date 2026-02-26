package duoc.dao.impl;

import duoc.dao.CategoriaDAO;
import duoc.modelo.Categoria;
import duoc.util.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAOImpl implements CategoriaDAO {

    private final Connection conexion;

    public CategoriaDAOImpl() {
        this.conexion = ConexionBD.getConexion();
    }

    @Override
    public void insertar(Categoria categoria) {
        String sql = "INSERT INTO categorias(nombre_categoria) VALUES(?)";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, categoria.getNombre());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar categoría", e);
        }
    }

    @Override
    public void actualizar(Categoria categoria) {
        String sql = "UPDATE categorias SET nombre_categoria=? WHERE id_categoria=?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, categoria.getNombre());
            stmt.setInt(2, categoria.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar categoría", e);
        }
    }

    @Override
    public void eliminar(int id) {
        String sql = "DELETE FROM categorias WHERE id_categoria=?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar categoría", e);
        }
    }

    @Override
    public Categoria buscarPorId(int id) {
        String sql = "SELECT id_categoria, nombre_categoria FROM categorias WHERE id_categoria=?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Categoria(
                            rs.getInt("id_categoria"),
                            rs.getString("nombre_categoria")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar categoría por id", e);
        }
        return null;
    }

    @Override
    public List<Categoria> listarTodos() {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT id_categoria, nombre_categoria FROM categorias ORDER BY nombre_categoria";

        try (PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Categoria(
                        rs.getInt("id_categoria"),
                        rs.getString("nombre_categoria")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar categorías", e);
        }

        return lista;
    }
}