package duoc.controlador;

import duoc.dao.ProductoDAO;
import duoc.dao.impl.ProductoDAOImpl;
import duoc.modelo.Producto;

import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ControladorProductos {

    private final ProductoDAO productoDAO = new ProductoDAOImpl();

    public void agregarProducto(String nombre, int idCategoria, int stock, int valor) {
        if (nombre == null || nombre.trim().isEmpty())
            throw new IllegalArgumentException("Nombre obligatorio.");

        if (stock <= 0) throw new IllegalArgumentException("Stock debe ser > 0.");
        if (valor <= 0) throw new IllegalArgumentException("Valor debe ser > 0.");

        Producto p = new Producto();
        p.setNombre(nombre.trim());
        p.setIdCategoria(idCategoria);
        p.setStock(stock);
        p.setValor(valor);

        productoDAO.insertar(p);
    }

    public void actualizarProducto(int id, String nombre, int idCategoria, int stock, int valor) {
        if (id <= 0) throw new IllegalArgumentException("ID inválido.");
        if (nombre == null || nombre.trim().isEmpty())
            throw new IllegalArgumentException("Nombre obligatorio.");

        Producto p = new Producto();
        p.setId(id);
        p.setNombre(nombre.trim());
        p.setIdCategoria(idCategoria);
        p.setStock(stock);
        p.setValor(valor);

        productoDAO.actualizar(p);
    }

    public void eliminarProductoPorId(int id) {
        if (id <= 0) throw new IllegalArgumentException("ID inválido.");
        productoDAO.eliminar(id);
    }

    public List<Producto> listarProductos() {
        return productoDAO.listarTodos();
    }

    public void cargarTabla(DefaultTableModel modelo) {
        modelo.setRowCount(0);
        for (Producto p : listarProductos()) {
            modelo.addRow(new Object[]{
                    p.getId(),
                    p.getNombre(),
                    p.getNombreCategoria(),
                    p.getStock(),
                    p.getValor()
            });
        }
    }
}