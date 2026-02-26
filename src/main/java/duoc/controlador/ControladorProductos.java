package duoc.controlador;

import duoc.conexion.ConexionBD;
import duoc.modelo.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ControladorProductos {

    public void cargarProductosDesdeBD(DefaultTableModel model) {
        String sql = "SELECT id, nombre, categoria, stock, valor FROM productos";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            model.setRowCount(0);

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("categoria"),
                        rs.getInt("stock"),
                        rs.getInt("valor")
                });
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar productos desde la BD.");
        }
    }

    public void agregarProducto(Producto producto, DefaultTableModel model) {
        String sql = "INSERT INTO productos (nombre, categoria, stock, valor) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, producto.getNombre());
            stmt.setString(2, producto.getCategoria());
            stmt.setInt(3, producto.getStock());
            stmt.setInt(4, producto.getValor());

            stmt.executeUpdate();
            cargarProductosDesdeBD(model);

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al agregar producto en la BD.");
        }
    }

    public void editarProducto(int id, Producto producto, DefaultTableModel model) {
        String sql = "UPDATE productos SET nombre=?, categoria=?, stock=?, valor=? WHERE id=?";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, producto.getNombre());
            stmt.setString(2, producto.getCategoria());
            stmt.setInt(3, producto.getStock());
            stmt.setInt(4, producto.getValor());
            stmt.setInt(5, id);

            stmt.executeUpdate();
            cargarProductosDesdeBD(model);

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al editar el producto en la BD.");
        }
    }

    public void eliminarProducto(int id, DefaultTableModel model) {
        String sql = "DELETE FROM productos WHERE id=?";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            cargarProductosDesdeBD(model);

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al eliminar el producto en la BD.");
        }
    }
}