package duoc.dao;

import duoc.modelo.Venta;

import java.util.List;

public interface VentaDAO {

    int registrarVenta(Venta venta);
    List<Venta> listarTodas();
    Venta buscarPorId(int idVenta);

}
