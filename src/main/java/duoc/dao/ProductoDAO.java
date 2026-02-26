package duoc.dao;

import duoc.modelo.Producto;

import java.util.List;

public interface ProductoDAO {

    void insertar(Producto producto);
    void actualizar(Producto producto);
    void eliminar(int idProducto);
    Producto buscarPorId(int idProducto);
    List<Producto> listarTodos();
    List<Producto> buscarPorNombre(String nombre);
}
