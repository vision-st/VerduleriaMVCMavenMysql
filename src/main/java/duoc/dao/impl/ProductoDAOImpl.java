package duoc.dao.impl;

import duoc.dao.ProductoDAO;
import duoc.modelo.Producto;
import duoc.util.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAOImpl implements ProductoDAO {

    private final Connection conexion;

    public ProductoDAOImpl() {
        this.conexion = ConexionBD.getConexion();
    }

    @Override
    public void insertar(Producto producto) {
        String sql = "INSERT INTO productos(nombre, id_categoria, stock, valor) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, producto.getNombre());
            stmt.setInt(2, producto.getIdCategoria());
            stmt.setInt(3, producto.getStock());
            stmt.setInt(4, producto.getValor());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar producto", e);
        }
    }

    @Override
    public void actualizar(Producto producto) {
        String sql = "UPDATE productos SET nombre=?, id_categoria=?, stock=?, valor=? WHERE id_producto=?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, producto.getNombre());
            stmt.setInt(2, producto.getIdCategoria());
            stmt.setInt(3, producto.getStock());
            stmt.setInt(4, producto.getValor());
            stmt.setInt(5, producto.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar producto", e);
        }
    }

    @Override
    public void eliminar(int idProducto) {
        String sql = "DELETE FROM productos WHERE id_producto=?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idProducto);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar producto", e);
        }
    }

    @Override
    public Producto buscarPorId(int idProducto) {
        String sql = """
            SELECT p.id_producto, p.nombre, p.id_categoria, c.nombre_categoria, p.stock, p.valor
            FROM productos p
            LEFT JOIN categorias c ON p.id_categoria = c.id_categoria
            WHERE p.id_producto = ?
        """;

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idProducto);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Producto(
                            rs.getInt("id_producto"),
                            rs.getString("nombre"),
                            rs.getInt("id_categoria"),
                            rs.getString("nombre_categoria"),
                            rs.getInt("stock"),
                            rs.getInt("valor")
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar producto por ID", e);
        }

        return null;
    }

    @Override
    public List<Producto> listarTodos() {
        List<Producto> lista = new ArrayList<>();

        String sql = """
            SELECT p.id_producto, p.nombre, p.id_categoria, c.nombre_categoria, p.stock, p.valor
            FROM productos p
            LEFT JOIN categorias c ON p.id_categoria = c.id_categoria
            ORDER BY p.id_producto DESC
        """;

        try (PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Producto(
                        rs.getInt("id_producto"),
                        rs.getString("nombre"),
                        rs.getInt("id_categoria"),
                        rs.getString("nombre_categoria"),
                        rs.getInt("stock"),
                        rs.getInt("valor")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar productos", e);
        }

        return lista;
    }

    @Override
    public List<Producto> buscarPorNombre(String nombre) {
        List<Producto> lista = new ArrayList<>();

        String sql = """
            SELECT p.id_producto, p.nombre, p.id_categoria, c.nombre_categoria, p.stock, p.valor
            FROM productos p
            LEFT JOIN categorias c ON p.id_categoria = c.id_categoria
            WHERE p.nombre LIKE ?
            ORDER BY p.nombre
        """;

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, "%" + nombre + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Producto(
                            rs.getInt("id_producto"),
                            rs.getString("nombre"),
                            rs.getInt("id_categoria"),
                            rs.getString("nombre_categoria"),
                            rs.getInt("stock"),
                            rs.getInt("valor")
                    ));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar productos por nombre", e);
        }

        return lista;
    }
}