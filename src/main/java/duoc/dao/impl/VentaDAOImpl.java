package duoc.dao.impl;

import duoc.dao.VentaDAO;
import duoc.modelo.Venta;
import duoc.util.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class VentaDAOImpl implements VentaDAO {

    private final Connection conexion;

    public VentaDAOImpl() {
        this.conexion = ConexionBD.getConexion();
    }

    @Override
    public int registrarVenta(Venta venta) {
        String sql = "INSERT INTO ventas(fecha, total) VALUES (?, ?)";

        try (PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setTimestamp(1, venta.getFecha());
            stmt.setDouble(2, venta.getTotal());

            int filas = stmt.executeUpdate();
            if (filas == 0) return -1;

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }

            return -1;

        } catch (SQLException e) {
            throw new RuntimeException("Error al registrar venta", e);
        }
    }

    @Override
    public List<Venta> listarTodas() {
        List<Venta> lista = new ArrayList<>();
        String sql = "SELECT id_venta, fecha, total FROM ventas ORDER BY id_venta DESC";

        try (PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Venta(
                        rs.getInt("id_venta"),
                        rs.getTimestamp("fecha"),
                        rs.getDouble("total")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar ventas", e);
        }

        return lista;
    }

    @Override
    public Venta buscarPorId(int idVenta) {
        String sql = "SELECT id_venta, fecha, total FROM ventas WHERE id_venta=?";

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idVenta);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Venta(
                            rs.getInt("id_venta"),
                            rs.getTimestamp("fecha"),
                            rs.getDouble("total")
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar venta por ID", e);
        }

        return null;
    }
}