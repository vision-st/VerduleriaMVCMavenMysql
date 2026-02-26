package duoc.dao;

import duoc.modelo.DetalleVenta;

import java.util.List;

public interface DetalleVentaDAO {

    void insertarDetalle(DetalleVenta detalleVenta);
    List<DetalleVenta> buscarPorVenta(int idVenta);

}
