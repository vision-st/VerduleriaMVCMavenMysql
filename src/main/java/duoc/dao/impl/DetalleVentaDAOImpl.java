package duoc.dao.impl;

import duoc.dao.DetalleVentaDAO;
import duoc.modelo.DetalleVenta;
import duoc.util.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DetalleVentaDAOImpl implements DetalleVentaDAO {

    private final Connection conexion;

    public DetalleVentaDAOImpl() {
        this.conexion = ConexionBD.getConexion();
    }

    @Override
    public void insertarDetalle(DetalleVenta detalle) {
        String sql = "INSERT INTO detalle_venta(id_venta, id_producto, cantidad, precio_unitario) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, detalle.getIdVenta());
            stmt.setInt(2, detalle.getIdProducto());
            stmt.setInt(3, detalle.getCantidad());
            stmt.setDouble(4, detalle.getPrecioUnitario());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar detalle de venta", e);
        }
    }

    @Override
    public List<DetalleVenta> buscarPorVenta(int idVenta) {
        List<DetalleVenta> lista = new ArrayList<>();

        String sql = """
            SELECT dv.id_detalle_venta, dv.id_venta, dv.id_producto, dv.cantidad, dv.precio_unitario,
                   p.nombre AS nombre_producto
            FROM detalle_venta dv
            JOIN productos p ON dv.id_producto = p.id_producto
            WHERE dv.id_venta = ?
        """;

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idVenta);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    DetalleVenta d = new DetalleVenta(
                            rs.getInt("id_detalle_venta"),
                            rs.getInt("id_venta"),
                            rs.getInt("id_producto"),
                            rs.getInt("cantidad"),
                            rs.getDouble("precio_unitario")
                    );
                    d.setNombreProducto(rs.getString("nombre_producto"));
                    lista.add(d);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar detalles por venta", e);
        }

        return lista;
    }
}