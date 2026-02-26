package duoc.controlador;

import duoc.dao.DetalleVentaDAO;
import duoc.dao.ProductoDAO;
import duoc.dao.VentaDAO;
import duoc.dao.impl.DetalleVentaDAOImpl;
import duoc.dao.impl.ProductoDAOImpl;
import duoc.dao.impl.VentaDAOImpl;
import duoc.modelo.DetalleVenta;
import duoc.modelo.Producto;
import duoc.modelo.Venta;

import java.sql.Timestamp;
import java.util.List;

public class ControladorVentas {

    private final VentaDAO ventaDAO = new VentaDAOImpl();
    private final DetalleVentaDAO detalleVentaDAO = new DetalleVentaDAOImpl();
    private final ProductoDAO productoDAO = new ProductoDAOImpl();

    /**
     * Importante: siguiendo la guía, el "carrito" usa el atributo stock como "cantidad vendida".
     */
    public boolean realizarVenta(List<Producto> carrito) {
        try {
            if (carrito == null || carrito.isEmpty()) return false;

            double total = 0;
            for (Producto p : carrito) {
                total += p.getValor() * p.getStock(); // stock = cantidad vendida
            }

            Venta venta = new Venta();
            venta.setFecha(new Timestamp(System.currentTimeMillis()));
            venta.setTotal(total);

            int idVenta = ventaDAO.registrarVenta(venta);
            if (idVenta == -1) return false;

            for (Producto p : carrito) {
                DetalleVenta detalle = new DetalleVenta();
                detalle.setIdVenta(idVenta);
                detalle.setIdProducto(p.getId());
                detalle.setCantidad(p.getStock());
                detalle.setPrecioUnitario(p.getValor());
                detalleVentaDAO.insertarDetalle(detalle);

                Producto original = productoDAO.buscarPorId(p.getId());
                if (original != null) {
                    original.setStock(original.getStock() - p.getStock());
                    productoDAO.actualizar(original);
                }
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Venta> listarVentas() {
        return ventaDAO.listarTodas();
    }

    public List<DetalleVenta> listarDetallePorVenta(int idVenta) {
        return detalleVentaDAO.buscarPorVenta(idVenta);
    }
}